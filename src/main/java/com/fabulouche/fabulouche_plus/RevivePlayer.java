package com.fabulouche.fabulouche_plus;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class RevivePlayer implements Listener {

    @EventHandler
    public void onBlockPlaced(final BlockPlaceEvent event) {
        final Block block = event.getBlock();

        // Vérifier si le bloc posé est une tête de joueur et est à la position voulue
        if (block.getType() == Material.PLAYER_HEAD && isAtLocation(block.getLocation(), 1000, 65, 0)) {
            Skull skull = (Skull) block.getState();
            final String owner = skull.getOwningPlayer().getName();
            Bukkit.getLogger().info("Une tête de joueur a été trouvée aux coordonnées 120 78 -41.");

            // Vérifier que la tête de joueur appartient à un joueur banni
            if (isPlayerBanned(owner)) {

                // Attendre 5 secondes avant de faire apparaître les éclairs
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        final Location location = block.getLocation();
                        // Faire apparaître les 4 éclairs
                        location.add(-2, 1, -2);
                        location.getWorld().strikeLightning(location);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                location.add(4, 0, 0);
                                location.getWorld().strikeLightning(location);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        location.subtract(0, 0, -4);
                                        location.getWorld().strikeLightning(location);
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                location.add(-4, 0, 0);
                                                location.getWorld().strikeLightning(location);

                                                new BukkitRunnable() {
                                                    @Override
                                                    public void run() {
                                                        // Vérifier si la tête de joueur est toujours posée
                                                        if (block.getType() == Material.PLAYER_HEAD
                                                                && isAtLocation(block.getLocation(), 1000, 65, 0)) {
                                                            unbanPlayer(owner); // Débanir le joueur
                                                            block.setType(Material.AIR); // Détruire la tête de joueur

                                                            Player player = event.getPlayer();
                                                            String playerName = player.getName();
                                                            Bukkit.broadcastMessage("§e" + owner
                                                                    + "§7 est revenu à la vie grâce à §e" + playerName
                                                                    + "§7 !");
                                                            location.add(2, -1, -2);
                                                            location.getWorld().strikeLightning(location);
                                                            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                                                onlinePlayer.playSound(onlinePlayer.getLocation(),
                                                                        "minecraft:entity.allay.death",
                                                                        SoundCategory.MASTER, 10, 0);
                                                            }
                                                        } else {
                                                            Bukkit.getLogger().info(
                                                                    "La tête de joueur a été retirée avant la fin de la résurrection.");
                                                        }
                                                    }
                                                }.runTaskLater(JavaPlugin.getPlugin(Plugin.class), 50L); // Attendre
                                                                                                         // 50ticks
                                            }
                                        }.runTaskLater(JavaPlugin.getPlugin(Plugin.class), 20L); // Attendre 1 seconde
                                    }
                                }.runTaskLater(JavaPlugin.getPlugin(Plugin.class), 20L); // Attendre 1 seconde
                            }
                        }.runTaskLater(JavaPlugin.getPlugin(Plugin.class), 20L); // Attendre 1 seconde
                    }
                }.runTaskLater(JavaPlugin.getPlugin(Plugin.class), 100L); // Attendre 5 secondes (100 ticks)

            } else {
                Bukkit.getLogger().info("La tête de joueur trouvée n'appartient pas à un joueur banni.");
            }
        }
    }

    private boolean isAtLocation(Location location, double x, double y, double z) {
        return location.getWorld().getName().equals("world") // Vérifier que l'objet est dans le monde "world"
                && location.getBlockX() == x // Vérifier la position X
                && location.getBlockY() == y // Vérifier la position Y
                && location.getBlockZ() == z; // Vérifier la position Z
    }

    private void unbanPlayer(String playerName) {
        Bukkit.getServer().getBanList(org.bukkit.BanList.Type.NAME).pardon(playerName);
    }

    private boolean isPlayerBanned(String playerName) {
        return Bukkit.getServer().getBanList(org.bukkit.BanList.Type.NAME).isBanned(playerName);
    }

}