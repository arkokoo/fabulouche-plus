package com.fabulouche.fabulouche_plus;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import java.util.HashMap;

public class ZoneDetection implements Listener {
    private HashMap<Player, String> playerZones = new HashMap<>(); // Stocke la zone actuelle de chaque joueur

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        double x = event.getTo().getX();
        double z = event.getTo().getZ();
        String newZone;

        // Stocke la nouvelle zone dans laquelle le joueur se trouve
        if (x >= -50 && x <= 50 && z >= -50 && z <= 50) {
            newZone = "C";
        } else if (z > 0) {
            newZone = "S";
        } else {
            newZone = "N";
        }

        String currentZone = playerZones.get(player);

        // Si la zone a changé, afficher un message au joueur
        if (!newZone.equals(currentZone)) {
            playerZones.put(player, newZone);
            if (newZone.equals("N")) {
                player.sendTitle("§4Nord", "", 10, 20, 10);
                player.playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, SoundCategory.MASTER, 1.0f,
                        1.0f);
            } else if (newZone.equals("S")) {
                player.sendTitle("§3Sud", "", 10, 20, 10);
                player.playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, SoundCategory.MASTER, 1.0f,
                        1.0f);
            } else {
                player.sendTitle("§eCentre", "", 10, 20, 10);
                player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_HURT, SoundCategory.MASTER, 1.0f,
                        1.0f);
            }
        }
    }
}