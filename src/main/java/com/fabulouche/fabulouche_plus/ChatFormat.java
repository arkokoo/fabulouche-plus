package com.fabulouche.fabulouche_plus;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFormat implements Listener {

    @EventHandler
    public void chatFormat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        World world = player.getLocation().getWorld();
        String colorPrefix;
        double x = player.getLocation().getX();
        double z = player.getLocation().getZ();

        if (world.getName().equals("world_nether")) {
            colorPrefix = "§4";
        } else if (world.getName().equals("world_the_end")) {
            colorPrefix = "§5";
        } else if (x >= -50 && x <= 50 && z >= -50 && z <= 50) {
            colorPrefix = "§e";
        } else if (z > 0) {
            colorPrefix = "§9";
        } else {
            colorPrefix = "§c";
        }
        // Utiliser la zone pour formater le message de chat
        String message = colorPrefix + "■§r " + player.getDisplayName() + " » " + event.getMessage();
        event.setFormat(message);
    }
}
