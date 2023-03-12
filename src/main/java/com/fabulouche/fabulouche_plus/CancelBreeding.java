package com.fabulouche.fabulouche_plus;

import org.bukkit.event.Listener;

import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityBreedEvent;

public class CancelBreeding implements Listener {

    @EventHandler
    public void onBreeding(EntityBreedEvent event) {
        if (event.getEntity() instanceof Villager) {
            event.setCancelled(true);
        }
    }
}
