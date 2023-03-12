package com.fabulouche.fabulouche_plus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.entity.Player;

public class BddUser {
    private Connection conn;

    public BddUser(Connection conn) {
        this.conn = conn;
    }

    public void registerPlayer(Player player) {
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("SELECT * FROM utilisateurs WHERE uuid = ?");
            stmt.setString(1, player.getUniqueId().toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // L'enregistrement existe déjà, on met à jour le pseudo
                updatePlayerName(player);
            } else {
                // L'enregistrement n'existe pas, on insère un nouvel enregistrement
                stmt = conn.prepareStatement("INSERT INTO utilisateurs (id, uuid, pseudo) VALUES (?, ?, ?)");
                stmt.setInt(1, getNextId());
                stmt.setString(2, player.getUniqueId().toString());
                stmt.setString(3, player.getName());
                stmt.executeUpdate();
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerName(Player player) {
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE utilisateurs SET pseudo = ? WHERE uuid = ?");
            stmt.setString(1, player.getName());
            stmt.setString(2, player.getUniqueId().toString());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getNextId() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT MAX(id) FROM utilisateurs");
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int maxId = rs.getInt(1);
        stmt.close();
        return maxId + 1;
    }
}