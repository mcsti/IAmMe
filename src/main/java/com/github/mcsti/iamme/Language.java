package com.github.mcsti.iamme;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Language {
    
    private static final Map<Lang, Map<Path, String>> DEFAULT_LANG = new HashMap<Lang, Map<Path, String>>() {{
            put(Lang.EN, new HashMap<Path, String>() {{
                put(Path.KICKED_REGISTERED, "&4You are too long to register you");
                put(Path.KICKED_LOGIN, "&4You are too long to login you");
            
                put(Path.PLEASE_REGISTER, "&ePlease register you with: /register <password> <confirmation password>");
                put(Path.PLEASE_LOGIN, "&ePlease login you with: /login <password>");
            
                put(Path.ERROR_ALREADY_CONNECTED, "&cYou are already connected");
                put(Path.ERROR_ALREADY_REGISTERED, "&cYou are already registered");
                put(Path.ERROR_MISSING_PASSWORD_CONFIRMATION, "&cPlease confirm your password");
                put(Path.ERROR_MISSING_ARGUMENT, "&cMaybe have you make a mistake in syntax ?");
                put(Path.ERROR_PASSWORD_NOT_EQUALS, "&cThe password and the confirmation is not seem to be equals");
                put(Path.ERROR_PASSWORD_INCORRECT, "&cPassword is wrong !");
    
                put(Path.SUCCESS, "&aYou are logged in, good game !");
            }});
            
            put(Lang.FR, new HashMap<Path, String>() {{
                put(Path.KICKED_REGISTERED, "&4Vous avez mis trop de temps pour vous enregistrer");
                put(Path.KICKED_LOGIN, "&4Vous avez mis trop de temps pour vous connecter");
            
                put(Path.PLEASE_REGISTER, "&eVeuillez vous enregistrer avec la commande: &f&o/register <mot de passe> <confirmation mot de passe>");
                put(Path.PLEASE_LOGIN, "&eVeuillez vous connecter avec la commande: &f&o/login <mot de passe>");
            
                put(Path.ERROR_ALREADY_CONNECTED, "&cVous êtes déjà connecter");
                put(Path.ERROR_ALREADY_REGISTERED, "&cVous êtes déjà enregistrer");
                put(Path.ERROR_MISSING_PASSWORD_CONFIRMATION, "&cVeuillez confirmer le mot de passe");
                put(Path.ERROR_MISSING_ARGUMENT, "&cN'avez vous pas fait une erreur de syntaxe ?");
                put(Path.ERROR_PASSWORD_NOT_EQUALS, "&cLe mot de passe et la confirmation de mot de passe ne corresponde pas");
                put(Path.ERROR_PASSWORD_INCORRECT, "&cLe mot de passe entrer est incorrect");
                
                put(Path.SUCCESS, "&aVous êtes connecter bon jeu !");
            }});
    }};
    
    public static void generate(File destination) {
        for (Lang lang : DEFAULT_LANG.keySet()) {
            File out = new File(destination, lang.toString() + ".yml");
            if (!out.exists()) {
                try {
                    FileConfiguration configuration = YamlConfiguration.loadConfiguration(out);
                    DEFAULT_LANG.get(lang).forEach((path, content) -> configuration.set(path.toString(), content));
                    configuration.save(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private Lang active;
    private YamlConfiguration configuration;
    
    public Language(Lang lang, File sourceFolder) {
        this.active = lang;
        this.configuration = YamlConfiguration.loadConfiguration(new File(sourceFolder, lang.toString()+".yml"));
    }
    
    public String get(Path path) {
        return configuration.getString(path.toString());
    }
    
    public String getAndColorize(Path path) {
        return ChatColor.translateAlternateColorCodes('&', get(path));
    }
    
    enum Lang {
        EN, FR;
    
        @Override
        public String toString() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
    
    enum Path {
        KICKED_REGISTERED("kicked.registered"),
        KICKED_LOGIN("kicked.login"),
        
        PLEASE_REGISTER("please.register"),
        PLEASE_LOGIN("please.login"),
    
        ERROR_ALREADY_CONNECTED("error.already.connected"),
        ERROR_ALREADY_REGISTERED("error.already.registered"),
        ERROR_MISSING_PASSWORD_CONFIRMATION("error.missing.password"),
        ERROR_MISSING_ARGUMENT("error.missing.argument"),
        ERROR_PASSWORD_NOT_EQUALS("error.password.confirmation_not_equals"),
        ERROR_PASSWORD_INCORRECT("error.password.incorrect"),
        
        SUCCESS("success");
        
        String identifier;
    
        Path(String identifier) {
            this.identifier = identifier;
        }
    
        @Override
        public String toString() {
            return identifier;
        }
    }
}
