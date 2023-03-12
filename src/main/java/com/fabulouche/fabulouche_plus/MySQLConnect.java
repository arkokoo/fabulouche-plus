package com.fabulouche.fabulouche_plus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnect {
    private Connection conn;

    public MySQLConnect(String hostname, String username, String password, String database) throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://" + hostname + "/" + database + "?user=" + username + "&password=" + password);
    }

    public Connection getConnection() {
        return conn;
    }
}
