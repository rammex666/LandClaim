package fr.rammex.landClaim.data;

import fr.rammex.landClaim.LandClaim;
import fr.rammex.landClaim.ranks.Ranks;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;

public class DataManager {

    private static String dbname ;
    private static File dataFolder;

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

    public static Connection getSQLConnection() {
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


            // Table pour les lands (data)
            String landDataTable = "CREATE TABLE IF NOT EXISTS lands_data (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "`owner_uuid` VARCHAR(36) NOT NULL," +
                    "`name` TEXT," +
                    "`member` INTEGER" +
                    ");";

            // Table pour les lands (chunks)
            String landLocTable = "CREATE TABLE IF NOT EXISTS lands_loc (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "`loc` TEXT," +
                    "`name` TEXT," +
                    "`owner_uuid` VARCHAR(36) NOT NULL" +
                    ");";

            // Table pour les lands (permissions)
            String landPermTable = "CREATE TABLE IF NOT EXISTS lands_perm (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "`land_id` INTEGER NOT NULL," +
                    "`status` TEXT," +
                    "`permission` INTEGER NOT NULL" +
                    ");";

            // Table pour les joueurs (data)
            String playerDataTable = "CREATE TABLE IF NOT EXISTS players_data (" +
                    "`uuid` VARCHAR(36) PRIMARY KEY NOT NULL," +
                    "`land_name` TEXT," +
                    "`land_rank` TEXT" +
                    ");";

            // Table pour les joueurs (permissions)
            String playerPermTable = "CREATE TABLE IF NOT EXISTS players_perm (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "`player_uuid` VARCHAR(36) NOT NULL," +
                    "`land_id` INTEGER NOT NULL," +
                    "`permission` INTEGER NOT NULL," +
                    "`status` TEXT" +
                    ");";

            // Table pour les joueurs (trust)
            String playerTrustTable = "CREATE TABLE IF NOT EXISTS players_trust (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "`player_uuid` VARCHAR(36) NOT NULL," +
                    "`land_id` INTEGER NOT NULL" +
                    ");";

            // Table pour les joueurs (inviation)
            String playerInvitationTable = "CREATE TABLE IF NOT EXISTS players_invitation (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "`player_uuid` VARCHAR(36) NOT NULL," +
                    "`land_id` INTEGER NOT NULL" +
                    ");";

            // Table pour les ranks
            String ranksTable = "CREATE TABLE IF NOT EXISTS ranks (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "`rank_name` TEXT NOT NULL," +
                    "`land_id` TEXT NOT NULL" +
                    "`permission` INTEGER NOT NULL" +
                    ");";

            s.executeUpdate(landLocTable);
            s.executeUpdate(landDataTable);
            s.executeUpdate(landPermTable);
            s.executeUpdate(playerDataTable);
            s.executeUpdate(playerPermTable);
            s.executeUpdate(playerTrustTable);
            s.executeUpdate(playerInvitationTable);
            s.executeUpdate(ranksTable);

            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialize();
    }

    public static void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (ps != null) ps.close();
            if (rs != null) rs.close();
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Failed to close database connection", ex);
        }
    }

    public static String getLandOwner(String name) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM lands_data WHERE name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            String result = rs.getString("owner_uuid");
            close(ps, rs);
            return result;
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to get land owner", ex);
        }
        return null;
    }

    public static Boolean isPlayerInLand(String uuid, String land_name) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM players_data WHERE uuid = ? AND land_name = ?");
            ps.setString(1, uuid);
            ps.setString(2, land_name);
            ResultSet rs = ps.executeQuery();
            Boolean result = rs.next();
            close(ps, rs);
            return result;
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to check if player is in land", ex);
        }
        return false;
    }

    public static void addLand(String loc, String owner_uuid, String name) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO lands_loc (loc, name, owner_uuid) VALUES (?, ?, ?)");
            ps.setString(1, loc);
            ps.setString(2, name);
            ps.setString(3, owner_uuid);
            ps.executeUpdate();
            close(ps, null);
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to add land", ex);
        }
    }

    public static boolean isChunkClaimed(String loc) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM lands_loc WHERE loc = ?");
            ps.setString(1, loc);
            ResultSet rs = ps.executeQuery();
            boolean result = rs.next();
            close(ps, rs);
            return result;
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to check if chunk is claimed", ex);
        }
        return false;
    }

    public static boolean isNameUsed(String name) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM lands_data WHERE name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            boolean result = rs.next();
            close(ps, rs);
            return result;
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to check if name is used", ex);
        }
        return false;
    }

    public static void addPlayer(String uuid, String land_name, String land_rank) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO players_data (uuid, land_name, land_rank) VALUES (?, ?, ?)");
            ps.setString(1, uuid);
            ps.setString(2, land_name);
            ps.setString(3, land_rank);
            ps.executeUpdate();
            close(ps, null);
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to add player", ex);
        }
    }

    public static void createLand(String owner_uuid, String name) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO lands_data (owner_uuid, name, member) VALUES (?, ?, ?)");
            ps.setString(1, owner_uuid);
            ps.setString(2, name);
            ps.setInt(3, 1);
            ps.executeUpdate();
            close(ps, null);
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to create land", ex);
        }
    }

    public static boolean asLand(String uuid) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM players_data WHERE uuid = ?");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            boolean result = rs.next();
            close(ps, rs);
            return result;
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to check if player has land", ex);
        }
        return false;
    }

    public static void addPermission(String land_id, String status, int permission) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO lands_perm (land_id, status, permission) VALUES (?, ?, ?)");
            ps.setString(1, land_id);
            ps.setString(2, status);
            ps.setInt(3, permission);
            ps.executeUpdate();
            close(ps, null);
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to add permission", ex);
        }
    }

    public static boolean playerAsPermission(String uuid, String land_id, int permission) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM players_perm WHERE player_uuid = ? AND land_id = ? AND permission = ?");
            ps.setString(1, uuid);
            ps.setString(2, land_id);
            ps.setInt(3, permission);
            ResultSet rs = ps.executeQuery();
            boolean result = rs.next();
            close(ps, rs);
            return result;
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to check if player has permission", ex);
        }
        return false;
    }

    public static boolean landPermission(String land_id, int permission) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM lands_perm WHERE land_id = ? AND permission = ?");
            ps.setString(1, land_id);
            ps.setInt(2, permission);
            ResultSet rs = ps.executeQuery();
            boolean result = rs.next();
            close(ps, rs);
            return result;
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to check if land has permission", ex);
        }
        return false;
    }

    public static boolean isOwner(String uuid, String land_name) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM lands_data WHERE owner_uuid = ? AND name = ?");
            ps.setString(1, uuid);
            ps.setString(2, land_name);
            ResultSet rs = ps.executeQuery();
            boolean result = rs.next();
            close(ps, rs);
            return result;
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to check if player is owner", ex);
        }
        return false;
    }

    public static void deleteLand(String owner_uuid, String name) {
        try (Connection connection = getSQLConnection()) {

            PreparedStatement ps = connection.prepareStatement("DELETE FROM lands_data WHERE owner_uuid = ? AND name = ?");
            ps.setString(1, owner_uuid);
            ps.setString(2, name);
            ps.executeUpdate();
            ps.close();

            PreparedStatement ps2 = connection.prepareStatement("DELETE FROM players_data WHERE land_name = ?");
            ps2.setString(1, name);
            ps2.executeUpdate();
            ps2.close();

            PreparedStatement ps3 = connection.prepareStatement("DELETE FROM lands_perm WHERE land_id = (SELECT id FROM lands_data WHERE name = ?)");
            ps3.setString(1, name);
            ps3.executeUpdate();
            ps3.close();

            PreparedStatement ps4 = connection.prepareStatement("DELETE FROM players_invitation WHERE land_id = (SELECT id FROM lands_data WHERE name = ?)");
            ps4.setString(1, name);
            ps4.executeUpdate();
            ps4.close();

            PreparedStatement ps5 = connection.prepareStatement("DELETE FROM players_trust WHERE land_id = (SELECT id FROM lands_data WHERE name = ?)");
            ps5.setString(1, name);
            ps5.executeUpdate();
            ps5.close();

            PreparedStatement ps6 = connection.prepareStatement("DELETE FROM players_perm WHERE land_id = (SELECT id FROM lands_data WHERE name = ?)");
            ps6.setString(1, name);
            ps6.executeUpdate();
            ps6.close();

            PreparedStatement ps7 = connection.prepareStatement("DELETE FROM lands_loc WHERE name = ?");
            ps7.setString(1, name);
            ps7.executeUpdate();
            ps7.close();

        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to delete land", ex);
        }
    }

    public static String getChunkLandName(String loc) {
        try (Connection connection = LandClaim.instance.getDatabase("lands").getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM lands_loc WHERE loc = ?");
            ps.setString(1, loc);
            ResultSet rs = ps.executeQuery();
            String result = rs.getString("name");
            LandClaim.instance.getDatabase("lands").close(ps, rs);
            return result;
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to get land name", ex);
        }
        return null;
    }

    public static Ranks getPlayerRank(String uuid, String land_name) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM players_data WHERE uuid = ? AND land_name = ?");
            ps.setString(1, uuid);
            ps.setString(2, land_name);
            ResultSet rs = ps.executeQuery();
            Ranks result = Ranks.valueOf(rs.getString("land_rank"));
            close(ps, rs);
            return result;
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to get player rank", ex);
        }
        return null;
    }

    public static boolean hasRankPermission(int permId, String rankName, String landId){
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM ranks WHERE rank_name = ? AND land_id = ? AND permission = ?");
            ps.setString(1, rankName);
            ps.setString(2, landId);
            ps.setInt(3, permId);
            ResultSet rs = ps.executeQuery();
            boolean result = rs.next();
            close(ps, rs);
            return result;
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to check if rank has permission", ex);
        }
        return false;
    }

    public static String getPlayerUUID(String playerName) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT uuid FROM players WHERE name = ?");
            ps.setString(1, playerName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String uuid = rs.getString("uuid");
                close(ps, rs);
                return uuid;
            }
            close(ps, rs);
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to get player UUID", ex);
        }
        return null;
    }

    public static void addPlayerTrust(String player_uuid, String land_id) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO players_trust (player_uuid, land_id) VALUES (?, ?)");
            ps.setString(1, player_uuid);
            ps.setString(2, land_id);
            ps.executeUpdate();
            close(ps, null);
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to add player trust", ex);
        }
    }

    public static boolean isPlayerAlreadyTrusted(String player_uuid, String land_id) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM players_trust WHERE player_uuid = ? AND land_id = ?");
            ps.setString(1, player_uuid);
            ps.setString(2, land_id);
            ResultSet rs = ps.executeQuery();
            boolean result = rs.next();
            close(ps, rs);
            return result;
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to check if player is already trusted", ex);
        }
        return false;
    }

    public static void unTrustPlayer(String player_uuid, String land_id) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM players_trust WHERE player_uuid = ? AND land_id = ?");
            ps.setString(1, player_uuid);
            ps.setString(2, land_id);
            ps.executeUpdate();
            close(ps, null);
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to untrust player", ex);
        }
    }

    public static void removePlayer(String player_uuid, String land_name) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM players_data WHERE uuid = ? AND land_name = ?");
            ps.setString(1, player_uuid);
            ps.setString(2, land_name);
            ps.executeUpdate();
            close(ps, null);
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to remove player", ex);
        }
    }

    public static void renameLand(String owner_uuid, String oldName, String newName) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("UPDATE lands_data SET name = ? WHERE owner_uuid = ? AND name = ?");
            ps.setString(1, newName);
            ps.setString(2, owner_uuid);
            ps.setString(3, oldName);
            ps.executeUpdate();
            close(ps, null);
        } catch (SQLException ex) {
            LandClaim.instance.getLogger().log(Level.SEVERE, "Unable to rename land", ex);
        }
    }

}
