package com.fabulouche.fabulouche_plus;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFormat implements Listener {

    public String getTeam(Player player) {
        String color;
        if (player.hasPermission("fabulouche.op")) {
            color = "§5";
        } else if (player.hasPermission("fabulouche.nord")) {
            color = "§c";
        } else if (player.hasPermission("fabulouche.sud")) {
            color = "§9";
        } else if (player.hasPermission("fabulouche.jail")) {
            color = "§8";
        } else {
            color = "§7";
        }
        return color;
    }

    @EventHandler
    public void chatFormat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        World world = player.getLocation().getWorld();
        String colorPrefix;
        double x = player.getLocation().getX();
        double z = player.getLocation().getZ();

        // Affiche la zone dans laquelle se trouve le joueur
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
        String playerName = player.getDisplayName();
        if (playerName.contains("YoCJu") || playerName.contains("YoCVivoy")) {
            playerName = playerName + "§6 ⭐";
        }

        // Utiliser la zone pour formater le message de chat
        String message = String.format("%s■ %s%s §r» %s", colorPrefix, getTeam(player), playerName,
                event.getMessage().replaceAll("%", "%%"));
        event.setFormat(message);

        // Vérifie si le message commence par "!"
        if (event.getMessage().startsWith("!")) {
            String teamPermission = "";

            if (player.hasPermission("fabulouche.sud") && player.hasPermission("fabulouche.nord")) {
                event.setCancelled(true);
                player.sendMessage("Vous êtes OP, vous n'avez pas de chat de team !");
                return;
            }
            // Vérifie si le joueur appartient à l'équipe sud
            if (player.hasPermission("fabulouche.sud")) {
                teamPermission = "fabulouche.sud";
            }
            // Vérifie si le joueur appartient à l'équipe nord
            else if (player.hasPermission("fabulouche.nord")) {
                teamPermission = "fabulouche.nord";
            }
            // Si le joueur n'appartient à aucune équipe, annule l'événement
            else {
                event.setCancelled(true);
                player.sendMessage("Vous n'êtes dans aucune équipe !");
                return;
            }

            // Envoie le message uniquement aux joueurs ayant la permission correspondante
            message = String.format("%s■ %s%s §r» %s", colorPrefix, getTeam(player), playerName,
                    event.getMessage().replaceFirst("!", ""));
            String teamPrefix = "§d[TEAM]§r";
            message = String.format("%s %s", teamPrefix, message);
            Bukkit.getServer().broadcast(message, teamPermission);

            // Annule l'événement pour éviter que le message soit envoyé dans le chat global
            event.setCancelled(true);
        }
    }
}
