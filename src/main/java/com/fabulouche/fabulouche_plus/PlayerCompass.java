package com.fabulouche.fabulouche_plus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
                    if (player.getExp() != 0) {
                        lore.set(0, "§aenabled");
                        updateLore(event, lore);
                        // Retire 1 d'xp par tick
                        player.giveExp(-1);
                        // Change l'aiguille de la boussole pour pointer vers le joueur ciblé
                        player.setCompassTarget(compassPlayers.get(player).getLocation());
                    } else {
                        player.setCompassTarget(Bukkit.getWorld("world").getSpawnLocation());
                        lore.set(0, "§cdisabled");
                        updateLore(event, lore);
                        cancel(); // arrête la boucle lorsque le joueur n'a plus d'XP
                    }
                }
            };

            // On vérifie que le traqueur est activé et qu'il est utilisé sur un bloc ou
            // dans l'air
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                if (compassPlayers.get(player) != null) {
                    if (player.getTotalExperience() == 0) {
                        // Informe le joueur du problème
                        player.sendMessage("§7Vous n'avez plus d'xp.");
                    }
                    if (usingLoops.containsKey(player)) {
                        // Si une boucle est déjà en cours d'exécution, on ne fait rien
                        player.setCompassTarget(Bukkit.getWorld("world").getSpawnLocation());
                        lore.set(0, "§cdisabled");
                        updateLore(event, lore);
                        usingLoops.get(player).cancel();
                        usingLoops.remove(player);
                    } else {
                        usingLoops.put(player, usingLoop);
                        usingLoop.runTaskTimer(plugin, 0L, 1L); // exécute la tâche toutes les 1
                        // tick (20
                        // fois par seconde)
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
                }
                compassPlayers.put(player, players.get(new Random().nextInt(players.size())));
                player.sendMessage(
                        "§7Vous avez sélectionné " + compassPlayers.get(player).getName() + "§7 comme cible.");
            }
        }
    }

    public void updateLore(PlayerInteractEvent e, List<String> lore) {
        e.getItem().getItemMeta().setLore(lore); // enregistrer la nouvelle lore
        e.getItem().setItemMeta(e.getItem().getItemMeta()); // mettre à jour l'item avec le nouveau ItemMeta
    }
}