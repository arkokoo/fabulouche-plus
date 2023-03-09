/*
 * package com.fabulouche.fabulouche_plus;
 * 
 * import org.bukkit.entity.Player;
 * import org.bukkit.event.EventHandler;
 * import org.bukkit.event.Listener;
 * import org.bukkit.event.player.PlayerMoveEvent;
 * 
 * import com.sk89q.worldguard.WorldGuard;
 * import com.sk89q.worldguard.protection.regions.ProtectedRegion;
 * import com.sk89q.worldguard.protection.regions.RegionQuery;
 * 
 * public class RegionHandler implements Listener {
 * 
 * private final String regionName = "centre"; // Replace with the name of the
 * region you want to handle
 * 
 * @EventHandler
 * public void onPlayerMove(PlayerMoveEvent event) {
 * Player player = event.getPlayer();
 * RegionQuery query =
 * WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
 * 
 * for (ProtectedRegion region :
 * query.getApplicableRegions(player.getLocation())) {
 * if (region.getId().equalsIgnoreCase(regionName)) {
 * if (!player.hasPermission("fabulouche.wl")) { // Replace with your own
 * permission node
 * event.setCancelled(true);
 * player.sendMessage("You are not allowed to leave this region!");
 * return;
 * }
 * }
 * }
 * }
 * }
 */