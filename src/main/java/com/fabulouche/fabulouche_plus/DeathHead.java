package com.fabulouche.fabulouche_plus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collections;

public class DeathHead implements Listener {
    public ItemStack playerHead;

    public DeathHead() {
        playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
        meta.setDisplayName("Tête de %s");
        meta.setLore(Collections.singletonList("Permet de le ressusciter"));
        playerHead.setItemMeta(meta);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(player);
        meta.setDisplayName(String.format("Tête de %s", player.getName()));
        meta.setLore(Collections.singletonList("Permet de le ressusciter"));
        head.setItemMeta(meta);
        event.getDrops().add(head);

        Bukkit.getServer().getBanList(org.bukkit.BanList.Type.NAME).addBan(player.getName(),
                "Vous êtes mort et êtes banni pendant 30 minutes",
                new java.util.Date(System.currentTimeMillis() + 1800000), null);
        player.getInventory().clear();
        player.kickPlayer("Banned");
    }
}