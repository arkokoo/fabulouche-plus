package com.fabulouche.fabulouche_plus;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class playerManager implements Listener {

    public boolean notAllowed(Player player) {
        if (player.hasPermission("fabulouche.jail")) {
            return true;
        } else if (player.hasPermission("fabulouche.nonwl")) {
            return true;
        }
        return false;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (!notAllowed(player)) return;

        
        event.setCancelled(true);
    }

}
