package com.fabulouche.fabulouche_plus;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * fabulouche-plus java plugin
 */
public class Plugin extends JavaPlugin {
  private static final Logger LOGGER = Logger.getLogger("fabulouche-plus");

  @Override
  public void onEnable() {
    LOGGER.info("fabulouche-plus enabled");

    // Enregistrer l'écouteur d'événements ZoneDetection
    getServer().getPluginManager().registerEvents(new ZoneDetection(), this);

    // Enregistrer l'écouteur d'événements DeathHead
    getServer().getPluginManager().registerEvents(new DeathHead(), this);

    // Enregistrer l'écouteur d'événements RevivePlayer
    getServer().getPluginManager().registerEvents(new RevivePlayer(), this);
  }

  @Override
  public void onDisable() {
    LOGGER.info("fabulouche-plus disabled");
  }
}
