package com.fabulouche.fabulouche_plus;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class ResurrectionDetector implements Listener {
    public static void detectAndResurrect() {
        World world = Bukkit.getWorld("world");
        Location location = new Location(world, 1000, 83, 0);
        Collection<Entity> nearbyEntities = world.getNearbyEntities(location, 1, 1, 1);

        for (Entity entity : nearbyEntities) {
            if (entity instanceof Item) {
                ItemStack item = ((Item) entity).getItemStack();
                if (item.getType() == Material.PLAYER_HEAD) {
                    SkullMeta meta = (SkullMeta) item.getItemMeta();
                    if (meta.hasOwner() && meta.getOwningPlayer().getName().equalsIgnoreCase("YoCFlopman")) {
                        entity.remove();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.sendTitle("Ressuscité", "", 10, 40, 10);
                        }
                        return;
                    }
                }
            }
        }
    }
}