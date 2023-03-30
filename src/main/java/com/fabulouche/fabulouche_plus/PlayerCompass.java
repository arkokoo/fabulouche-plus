package com.fabulouche.fabulouche_plus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerCompass implements Listener {
    private final Plugin plugin = JavaPlugin.getPlugin(Plugin.class);
    public Map<Player, Player> compassPlayers = new HashMap<>(); // Stocke la zone actuelle de chaque joueur
    public Map<Player, BukkitRunnable> usingLoops = new HashMap<>(); // Stocke les boucles en cours d'exécution pour
                                                                     // chaque joueur

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();
        ItemMeta itemMeta = item.getItemMeta();
        String itemName = itemMeta.getDisplayName();

        // On vérifie si le joueur fait un clic droit avec le traqueur
        if (item != null && item.getType() == Material.COMPASS && itemName.equals("§d§lTraqueur")) {

            List<String> lore = itemMeta.getLore(); // obtenir la lore actuelle

            BukkitRunnable usingLoop = new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.getLocation().getWorld().getName().equals("world")) {
                        if (player.getWorld() != compassPlayers.get(player).getWorld()) {
                            player.sendMessage("§7Le joueur ciblé est dans un autre monde.");
                            player.setCompassTarget(Bukkit.getWorld("world").getSpawnLocation());
                            lore.set(0, "§cdisabled");
                            itemMeta.setLore(lore); // Met à jour la lore de l'ItemMeta
                            item.setItemMeta(itemMeta); // Met à jour l'item avec le nouvel ItemMeta
                            cancel(); // arrête la boucle lorsque le joueur n'a plus d'XP

                        } else if (player.getTotalExperience() != 0) {
                            lore.set(0, "§aenabled");
                            itemMeta.setLore(lore); // Met à jour la lore de l'ItemMeta
                            item.setItemMeta(itemMeta); // Met à jour l'item avec le nouvel ItemMeta
                            // Retire 1 d'xp par tick
                            player.giveExp(-5);
                            // Change l'aiguille de la boussole pour pointer vers le joueur ciblé
                            player.setCompassTarget(compassPlayers.get(player).getLocation());
                        } else {
                            player.sendMessage("§7Vous n'avez plus assez d'expérience.");
                            player.setCompassTarget(Bukkit.getWorld("world").getSpawnLocation());
                            lore.set(0, "§cdisabled");
                            itemMeta.setLore(lore); // Met à jour la lore de l'ItemMeta
                            item.setItemMeta(itemMeta); // Met à jour l'item avec le nouvel ItemMeta
                            cancel(); // arrête la boucle lorsque le joueur n'a plus d'XP
                        }
                    } else {
                        player.sendMessage("§7Vous n'êtes pas dans l'Overworld.");
                        player.setCompassTarget(Bukkit.getWorld("world").getSpawnLocation());
                        lore.set(0, "§cdisabled");
                        itemMeta.setLore(lore); // Met à jour la lore de l'ItemMeta
                        item.setItemMeta(itemMeta); // Met à jour l'item avec le nouvel ItemMeta
                        cancel(); // arrête la boucle lorsque le joueur n'a plus d'XP
                    }
                }
            };

            // On vérifie que le traqueur est activé et qu'il est utilisé sur un bloc ou
            // dans l'air
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                if (compassPlayers.get(player) != null) {
                    if (usingLoops.containsKey(player)) {
                        // Si une boucle est déjà en cours d'exécution, on ne fait rien
                        player.setCompassTarget(Bukkit.getWorld("world").getSpawnLocation());
                        lore.set(0, "§cdisabled");
                        itemMeta.setLore(lore); // Met à jour la lore de l'ItemMeta
                        item.setItemMeta(itemMeta); // Met à jour l'item avec le nouvel ItemMeta
                        usingLoops.get(player).cancel();
                        usingLoops.remove(player);
                    } else {
                        usingLoops.put(player, usingLoop);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
                        usingLoop.runTaskTimer(plugin, 0L, 5L); // exécute la tâche toutes les 1 tick
                        // (20 fois par seconde)
                    }
                } else {
                    player.sendMessage("§7Aucun joueur n'est sélectionné.");
                }
            } else if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
                // On récupère un joueur aléatoire
                List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                // On retire le joueur de l'événement
                players.remove(player);

                if (players.isEmpty()) {
                    // Informe le joueur du problème
                    player.sendMessage("§7Vous êtes le seul joueur présent sur le serveur.");
                } else {
                    if (player.getInventory().contains(Material.AMETHYST_SHARD)) {
                        player.getInventory().removeItem(new ItemStack(Material.AMETHYST_SHARD, 1));
                        compassPlayers.put(player, players.get(new Random().nextInt(players.size())));
                        player.sendMessage(
                                "§7Vous avez sélectionné " + compassPlayers.get(player).getName() + "§7 comme cible.");
                    }
                }
            }
        }
    }
}