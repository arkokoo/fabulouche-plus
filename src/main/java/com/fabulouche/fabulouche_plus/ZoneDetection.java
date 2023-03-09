package com.fabulouche.fabulouche_plus;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class ZoneDetection implements Listener {
    public Map<Player, String> playerZones = new HashMap<>(); // Stocke la zone actuelle de chaque joueur

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
            World world = player.getLocation().getWorld();
            if (world.getName().equals("world")) {
                playerZones.put(player, newZone);
                if (newZone.equals("N")) {
                    player.sendTitle("§cNord", "", 10, 20, 10);
                    player.playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, SoundCategory.MASTER, 1.0f,
                            1.0f);
                    player.setPlayerListName(" §c■§r " + player.getName());
                } else if (newZone.equals("S")) {
                    player.sendTitle("§9Sud", "", 10, 20, 10);
                    player.playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, SoundCategory.MASTER, 1.0f,
                            1.0f);
                    player.setPlayerListName(" §9■§r " + player.getName());
                } else {
                    player.sendTitle("§eCentre", "", 10, 20, 10);
                    player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_HURT, SoundCategory.MASTER, 1.0f,
                            1.0f);
                    player.setPlayerListName(" §e■§r " + player.getName());
                }
            }
        }
    }
}