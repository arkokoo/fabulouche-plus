package com.fabulouche.fabulouche_plus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetParrain implements CommandExecutor {
    private Connection conn;

    public CommandSetParrain(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cette commande ne peut être exécutée que par un administrateur.");
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage("Utilisation: /parrain <Parrain> <Filleul>");
            return true;
        }
        String pseudoParrain = args[0];
        String pseudoFilleul = args[1];

        Player player1 = Bukkit.getServer().getPlayer(pseudoParrain);
        Player player2 = Bukkit.getServer().getPlayer(pseudoFilleul);

        if (player1.hasPermission("fabulouche.op") || player2.hasPermission("fabulouche.op")
                || player2.hasPermission("fabulouche.jail")) {
            sender.sendMessage("Aucun joueur ne doit être un administrateur.");
            return true;
        }
        int idParrain = getIdFromPseudo(pseudoParrain);
        if (idParrain == -1) {
            sender.sendMessage("Le joueur " + pseudoParrain + " n'existe pas dans la base de données.");
            return true;
        }
        int idFilleul = getIdFromPseudo(pseudoFilleul);
        if (idFilleul == -1) {
            sender.sendMessage("Le joueur " + pseudoFilleul + " n'existe pas dans la base de données.");
            return true;
        }
        if (player2.hasPermission("fabulouche.sud") || player2.hasPermission("fabulouche.nord")) {
            sender.sendMessage("Le joueur " + pseudoFilleul + " est déjà dans une équipe.");
            return true;
        }
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("INSERT INTO parrainages (idParrain, idFilleul) VALUES (?, ?)");
            stmt.setInt(1, idParrain);
            stmt.setInt(2, idFilleul);
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            if (rowsAffected == 1) {
                sender.sendMessage(
                        "Le parrainage entre " + pseudoParrain + " et " + pseudoFilleul + " a été enregistré.");
                if (player1.hasPermission("fabulouche.nord")) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                            "lp user " + pseudoFilleul + " group set nord");
                    Bukkit.broadcastMessage(
                            "§c" + pseudoFilleul + "§e vient de se faire parrainer par §c" + pseudoParrain + "§e. ");
                } else if (player1.hasPermission("fabulouche.sud")) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                            "lp user " + pseudoFilleul + " group set sud");
                    Bukkit.broadcastMessage(
                            "§9" + pseudoFilleul + "§e vient de se faire parrainer par §9" + pseudoParrain + "§e. ");
                }
            } else {
                sender.sendMessage("Erreur lors de l'enregistrement du parrainage.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage("Erreur lors de l'enregistrement du parrainage.");
        }
        return true;
    }

    private int getIdFromPseudo(String pseudo) {
        int id = -1;
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT id FROM utilisateurs WHERE pseudo = ?");
            stmt.setString(1, pseudo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
}