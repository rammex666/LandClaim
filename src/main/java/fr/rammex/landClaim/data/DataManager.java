package fr.rammex.landClaim.data;

import fr.rammex.landClaim.LandClaim;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;

public class DataManager {

    private final String dbname;
    private final File dataFolder;

    public DataManager(String databaseName, File folder) {
        dbname = databaseName;
        dataFolder = folder;
    }

    public void initialize() {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM lands_loc");
            ResultSet rs = ps.executeQuery();
            close(ps, rs);
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to retrieve connection", ex);
        }
    }

    public Connection getSQLConnection() {
        File folder = new File(dataFolder, dbname + ".db");
        if (!folder.exists()) {
            try {
                folder.createNewFile();
            } catch (IOException e) {
                LandClaim.instance.getLogger().log(Level.SEVERE, "File write error: " + dbname + ".db");
            }
        }
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:" + folder);
        } catch (SQLException | ClassNotFoundException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
        }
        return null;
    }

    public void load() {
        try (Connection connection = getSQLConnection()) {
            Statement s = connection.createStatement();
            String landTable = "CREATE TABLE IF NOT EXISTS lands_loc (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "`loc` TEXT," +
                    "`name` TEXT," +
                    "`owner_uuid` VARCHAR(36) NOT NULL" +
                    ");";
            s.executeUpdate(landTable);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialize();
    }

    public void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (ps != null) ps.close();
            if (rs != null) rs.close();
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Failed to close database connection", ex);
        }
    }

    public void addLand(String loc, String owner_uuid, String name) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO lands_loc (loc, name ,owner_uuid) VALUES (?, ?, ?)");
            ps.setString(1, loc);
            ps.setString(2, owner_uuid);
            ps.executeUpdate();
            close(ps, null);
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to add land", ex);
        }
    }

    public boolean isChunkClaimed(String loc) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM lands_loc WHERE loc = ?");
            ps.setString(1, loc);
            ResultSet rs = ps.executeQuery();
            boolean result = rs.next();
            close(ps, rs);
            return result; // Return the actual result of the query
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to check if chunk is claimed", ex);
        }
        return false;
    }

}
