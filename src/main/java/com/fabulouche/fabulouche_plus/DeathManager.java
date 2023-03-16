package com.fabulouche.fabulouche_plus;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;

public class DeathManager implements Listener {
    public ItemStack playerHead;

    public void deathHead() {
        playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
        meta.setDisplayName("Tête de %s");
        meta.setLore(Collections.singletonList("Permet de le ressusciter"));
        playerHead.setItemMeta(meta);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        double x = player.getLocation().getX();
        double z = player.getLocation().getZ();
        World world = player.getLocation().getWorld();
        if (!(x >= -50 && x <= 50 && z >= -50 && z <= 50) || !(world.getName().equals("world"))) {
            long playTime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
            if (playTime >= 216000) {
                ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
                SkullMeta meta = (SkullMeta) head.getItemMeta();
                meta.setOwningPlayer(player);
                meta.setDisplayName(String.format("Tête de %s", player.getName()));
                meta.setLore(Collections.singletonList("Permet de le ressusciter"));
                head.setItemMeta(meta);
                event.getDrops().add(head);

                Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(player.getName(),
                        event.getDeathMessage(),
                        new java.util.Date(System.currentTimeMillis() + 3600000), null);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.getInventory().clear();
                        player.kickPlayer("Vous etes mort. Raison : " + event.getDeathMessage()
                                + ". Vous pourrez revenir dans 1 heure.");
                    }
                }.runTaskLater(JavaPlugin.getPlugin(Plugin.class), 20L);

            }
        }
    }
}