package com.fabulouche.fabulouche_plus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class RevivePlayer implements Listener {

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        Player player = event.getPlayer();

        // Vérifier si l'objet jeté est une tête de joueur
        if (item.getType() == Material.PLAYER_HEAD) {
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            String owner = meta.getOwningPlayer().getName();
            player.sendMessage("Vous venez de jeter une tête de joueur.");

            // Vérifier si la tête de joueur appartient à un joueur banni
            if (isPlayerBanned(owner)) {
                event.setCancelled(true); // Annuler l'événement de jet d'objet
                unbanPlayer(owner); // Débanir le joueur
                player.sendMessage(owner + " est revenu à la vie !");
            } else {
                player.sendMessage("Vous ne pouvez pas jeter la tête de ce joueur car il n'est pas banni.");
            }
        }
    }

    private void unbanPlayer(String playerName) {
        Bukkit.getServer().getBanList(org.bukkit.BanList.Type.NAME).pardon(playerName);
    }

    private boolean isPlayerBanned(String playerName) {
        return Bukkit.getServer().getBanList(org.bukkit.BanList.Type.NAME).isBanned(playerName);
    }

}