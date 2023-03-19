package com.fabulouche.fabulouche_plus;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * fabulouche-plus java plugin
 */
public class Plugin extends JavaPlugin implements Listener {
  private static final Logger LOGGER = Logger.getLogger("fabulouche-plus");
  private Connection conn;
  private BddUser bddUser;

  @Override
  public void onEnable() {
    LOGGER.info("fabulouche-plus enabled");

    // Enregistrer l'écouteur d'événements ZoneDetection
    getServer().getPluginManager().registerEvents(new ZoneDetection(), this);

    // Enregistrer l'écouteur d'événements DeathHead
    getServer().getPluginManager().registerEvents(new DeathManager(), this);

    // Enregistrer l'écouteur d'événements RevivePlayer
    getServer().getPluginManager().registerEvents(new RevivePlayer(), this);

    // Enregistrer l'écouteur d'événements ChatFormat
    getServer().getPluginManager().registerEvents(new ChatFormat(), this);

    // Enregistrer l'écouteur d'événements CancelBreeding
    getServer().getPluginManager().registerEvents(new CancelBreeding(), this);

    // Enregistrer l'écouteur d'événements PlayerCompass
    getServer().getPluginManager().registerEvents(new PlayerCompass(), this);

    try {
      MySQLConnect db = new MySQLConnect("minecraft3175.omgserv.com", "minecraft_398320", "c4cHn4kCXT3aFm!C",
          "minecraft_398320");
      conn = db.getConnection();
      bddUser = new BddUser(conn);
      LOGGER.info("Connected to MySQL database");

      // Enregistrer la commande /parrain après l'initialisation de la variable "conn"
      getCommand("parrain").setExecutor(new CommandSetParrain(conn));
    } catch (SQLException e) {
      LOGGER.severe("Error connecting to MySQL database: " + e.getMessage());
    }
    getServer().getPluginManager().registerEvents(this, this);
  }

  @Override
  public void onDisable() {
    try {
      if (conn != null && !conn.isClosed()) {
        conn.close();
        LOGGER.info("Closed connection to MySQL database");
      }
    } catch (SQLException e) {
      LOGGER.severe("Error closing MySQL connection: " + e.getMessage());
    }
    LOGGER.info("fabulouche-plus disabled");
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    bddUser.registerPlayer(player);
  }
}