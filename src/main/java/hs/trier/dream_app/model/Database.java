package hs.trier.dream_app.model;

import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    public static final String DB_LOC = Objects.requireNonNull(Database.class.getResource("/hs/trier/dream_app/database/dream-app.db")).toExternalForm();

    public static Connection connect() {
        String dbPrefix = "jdbc:sqlite:";
        Connection connection;
        SQLiteConfig config;

        try {
            config = new SQLiteConfig();
            config.setPragma(SQLiteConfig.Pragma.FOREIGN_KEYS, "true");

            connection = DriverManager.getConnection(dbPrefix + DB_LOC, config.toProperties());
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not establish connection to database at " + DB_LOC + ".");
            return null;
        }
        return connection;
    }

    public static boolean initializeDatabase() {
        return checkDrivers() && checkConnection();
    }

    private static boolean checkConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new org.sqlite.JDBC());
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not start SQLite Drivers.");
            return false;
        }
    }

    private static boolean checkDrivers() {
        try (Connection connection = connect()) {

            return connection != null;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not connect to database.");
            return false;
        }
    }
}