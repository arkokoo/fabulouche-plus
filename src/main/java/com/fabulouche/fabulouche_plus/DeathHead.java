package com.fabulouche.fabulouche_plus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collections;

public class DeathHead implements Listener {
    private final ItemStack head;

    public DeathHead() {
        head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setDisplayName("Tête de %s");
        meta.setLore(Collections.singletonList("Permet de le ressusciter"));
        head.setItemMeta(meta);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(player);
        meta.setDisplayName(String.format(meta.getDisplayName(), player.getName()));
        head.setItemMeta(meta);
        event.getDrops().add(head);
    }
}