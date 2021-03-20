package com.github.mcsti.iamme;

import net.minecraft.server.v1_16_R3.PlayerConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public final class Main extends JavaPlugin implements Listener, CommandExecutor {
    
    private Connection connection;
    private Map<UUID, String> cachedPlayers = new HashMap<>();
    private Map<UUID, Integer>        waiting       = new HashMap<>();
    private byte[]   salt;
    private Language language;
    
    @Override
    public void onEnable() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            getLogger().severe("SQLITE driver not found,");
            getLogger().severe("plugin will be disabled and no authentication will be enable on the server,");
            getLogger().log(Level.SEVERE, "please contact the developer of the plugin", e);
            Bukkit.getPluginManager().disablePlugin(this);
        }
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:database.db");
    
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            
            statement.executeUpdate(
                    "create table if not exists iamme_user (" +
                            "`uuid` TEXT NOT NULL," +
                            "`password` LONGTEXT NOT NULL" +
                        ")");
            
            ResultSet resultSet = statement.executeQuery("SELECT * FROM iamme_user;");
    
            while (resultSet.next()) {
                cachedPlayers.put(UUID.fromString(resultSet.getString("uuid")), resultSet.getString("password"));
            }
            getLogger().info("Loaded "+cachedPlayers.size()+" players from database");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        
        // Cancel output of sensible information
        org.apache.logging.log4j.core.Logger logger;
        logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
        logger.addFilter(new LogFilter());
    
        File saltFile = new File(".salt");
        if (!saltFile.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(saltFile))) {
                SecureRandom random = new SecureRandom();
                byte[] salt = new byte[16];
                random.nextBytes(salt);
                this.salt = salt;
                
                writer.write(new String(this.salt));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                this.salt = Files.readAllBytes(saltFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        File languagesFolder = new File(getDataFolder(), "languages");
        Language.generate(languagesFolder);
        
        this.language = new Language(Language.Lang.FR, languagesFolder);
    
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(waiting), this);
        Objects.requireNonNull(Bukkit.getPluginCommand("login")).setExecutor(this);
        Objects.requireNonNull(Bukkit.getPluginCommand("register")).setExecutor(this);
        
        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid : waiting.keySet()) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null) {
                        waiting.remove(uuid);
                        continue;
                    }
                    boolean alreadyRegister = cachedPlayers.containsKey(uuid);
                    if (waiting.get(uuid) == 0) {
                        if (alreadyRegister) {
                            player.kickPlayer(language.getAndColorize(Language.Path.KICKED_LOGIN));
                        } else {
                            player.kickPlayer(language.getAndColorize(Language.Path.KICKED_REGISTERED));
                        }
                    }
                    if (alreadyRegister) {
                        player.sendMessage(language.getAndColorize(Language.Path.PLEASE_LOGIN));
                    } else {
                        player.sendMessage(language.getAndColorize(Language.Path.PLEASE_REGISTER));
                    }
                    waiting.merge(uuid, 1, (integer, integer2) -> integer - integer2);
                }
            }
        }.runTaskTimer(this, 0L, 20*10);
    }
    
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    
    @Override
    public boolean onCommand(
            CommandSender sender, Command command, String label, String[] args
    ) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (!waiting.containsKey(player.getUniqueId())) {
            player.sendMessage(language.getAndColorize(Language.Path.ERROR_ALREADY_CONNECTED));
            return true;
        }
        if (label.equalsIgnoreCase("register")) {
            if (cachedPlayers.containsKey(player.getUniqueId())) {
                player.sendMessage(language.getAndColorize(Language.Path.ERROR_ALREADY_REGISTERED));
                return true;
            }
            if (args.length < 2) {
                player.sendMessage(language.getAndColorize(Language.Path.ERROR_MISSING_PASSWORD_CONFIRMATION));
                return true;
            }
            if (!args[0].equals(args[1])) {
                player.sendMessage(language.getAndColorize(Language.Path.ERROR_PASSWORD_NOT_EQUALS));
                return true;
            }
            cachedPlayers.put(player.getUniqueId(), args[0]);
        
            try {
                PreparedStatement statement = connection
                        .prepareStatement("INSERT INTO iamme_user (`uuid`, `password`) VALUES ( ?, ? )");
                statement.setString(1, player.getUniqueId().toString());
                statement.setString(2, new String(getHash(args[0])));
                statement.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
    
            player.sendMessage(language.getAndColorize(Language.Path.SUCCESS));
            waiting.remove(player.getUniqueId());
        } else if (label.equalsIgnoreCase("login")) {
            if (args.length < 1) {
                player.sendMessage(language.getAndColorize(Language.Path.ERROR_MISSING_ARGUMENT));
                return true;
            }
            try {
                PreparedStatement statement = connection
                        .prepareStatement("SELECT `password` FROM iamme_user WHERE `uuid` = ?");
                statement.setString(1, player.getUniqueId().toString());
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String savedPassword = resultSet.getString("password");
                    if (savedPassword.equals(new String(getHash(args[0])))) {
                        player.sendMessage(language.getAndColorize(Language.Path.SUCCESS));
                        waiting.remove(player.getUniqueId());
                    } else {
                        player.sendMessage(language.getAndColorize(Language.Path.ERROR_PASSWORD_INCORRECT));
                        return true;
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return super.onCommand(sender, command, label, args);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        waiting.put(event.getPlayer().getUniqueId(), 10);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        waiting.remove(event.getPlayer().getUniqueId());
    }
    
    private byte[] getHash(String in) {
        return getHash(in, this.salt);
    }
    
    private byte[] getHash(String in, byte[] salt) {
        KeySpec spec = new PBEKeySpec(in.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = null;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            return factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
}
