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

    ChatFormat chat = new ChatFormat();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        double x = event.getTo().getX();
        double z = event.getTo().getZ();
        String teamColor = chat.getTeam(player);
        String newZone = "/";

        // Stocke la nouvelle zone dans laquelle le joueur se trouve
        if (player.getLocation().getWorld().getName().equals("world_nether")) {
            player.setPlayerListName(" §4■ " + teamColor + player.getName());
        } else if (player.getLocation().getWorld().getName().equals("world_the_end")) {
            player.setPlayerListName(" §5■ " + teamColor + player.getName());
        } else if (x >= -50 && x <= 50 && z >= -50 && z <= 50) {
            newZone = "C";
            player.setPlayerListName(" §e■ " + teamColor + player.getName());
        } else if (z > 0) {
            newZone = "S";
            player.setPlayerListName(" §9■ " + teamColor + player.getName());
        } else {
            newZone = "N";
            player.setPlayerListName(" §c■ " + teamColor + player.getName());

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
                } else if (newZone.equals("S")) {
                    player.sendTitle("§9Sud", "", 10, 20, 10);
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
}