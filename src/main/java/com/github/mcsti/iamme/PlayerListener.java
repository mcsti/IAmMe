package com.github.mcsti.iamme;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.*;

import java.util.Map;
import java.util.UUID;

public class PlayerListener implements Listener {
    
    private Map<UUID, Integer> waiting;
    
    public PlayerListener(Map<UUID, Integer> waiting) {
        this.waiting = waiting;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerAnimation(PlayerAnimationEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()) &&
                !event.getMessage().startsWith("/register") && !event.getMessage().startsWith("/login"));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerEditBook(PlayerEditBookEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerFish(PlayerFishEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerHarvestBlock(PlayerHarvestBlockEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerItemMend(PlayerItemMendEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeashEntity(PlayerLeashEntityEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickupArrow(PlayerPickupArrowEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPortal(PlayerPortalEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRecipeDiscover(PlayerRecipeDiscoverEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerShearEntity(PlayerShearEntityEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerStatisticIncrement(PlayerStatisticIncrementEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTakeLecternBook(PlayerTakeLecternBookEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerVelocity(PlayerVelocityEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(waiting.containsKey(event.getPlayer().getUniqueId()));
    }
}
