package com.fabulouche.fabulouche_plus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerCompass implements Listener {
    private final Plugin plugin = JavaPlugin.getPlugin(Plugin.class);

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        // On vérifie si le joueur fait un clic droit avec le traqueur
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()
                || !item.getItemMeta().hasLore())
            return;

        ItemMeta itemMeta = item.getItemMeta();
        String itemName = itemMeta.getDisplayName();

        // On vérifie que le traqueur est activé et qu'il est utilisé sur un bloc ou
        // dans l'air
        if ((itemName.equals("§dTraqueur") && item.getType() == Material.COMPASS
                && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
                && itemMeta.getPersistentDataContainer().has(
                        new NamespacedKey(plugin, "playerCompass"),
                        PersistentDataType.INTEGER)
                && itemMeta.getPersistentDataContainer().get(
                        new NamespacedKey(plugin, "playerCompass"),
                        PersistentDataType.INTEGER) == 1)) {

            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

            // On retire le joueur de l'événement
            players.remove(player);

            if (players.isEmpty()) {
                // Informe le joueur du problème
                player.sendMessage("Vous êtes le seul joueur présent sur le serveur.");
            } else if (player.getLevel() == 0) {
                // Informe le joueur du problème
                player.sendMessage("Vous n'avez plus d'xp.");
            } else {
                // On choisit un joueur au hasard parmi les joueurs restants
                Player randomPlayer = players.get(new Random().nextInt(players.size()));

                // Informe le joueur du nouveau joueur ciblé par la boussole
                player.sendMessage("La boussole pointe maintenant vers " + randomPlayer.getName());

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.getLevel() > 0) {
                            // Change l'aiguille de la boussole pour pointer vers le joueur aléatoire
                            player.setCompassTarget(randomPlayer.getLocation());

                            // Retire 1 d'xp par tick
                            player.giveExp(-1);
                        } else {
                            player.setCompassTarget(player.getLocation());
                            cancel(); // arrête la boucle lorsque le joueur n'a plus d'XP
                        }
                    }
                }.runTaskTimer(plugin, 0L, 1L); // exécute la tâche toutes les 1
                                                // tick (20
                                                // fois par seconde)
            }

        }
    }

}
