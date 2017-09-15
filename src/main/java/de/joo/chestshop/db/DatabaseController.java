package de.joo.chestshop.db;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import de.exlll.databaselib.submit.PluginSqlTaskSubmitter;
import de.joo.chestshop.config.GeneralConfig;
import de.joo.chestshop.plugin.ChestShop;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Calendar;
import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * Created by Johannes on 25.04.2017.
 */
public class DatabaseController extends PluginSqlTaskSubmitter {
    private DatabaseConfig config;
    private static GeneralConfig generalConfig = ChestShop.generalConfig;
    public DatabaseController(ChestShop plugin) {
        super(plugin);
        config = new DatabaseConfig(Paths.get(plugin.getDataFolder()+"/database.yml"));
        try {
            config.loadAndSave();
        } catch (IOException e) {
            e.printStackTrace();
        }
        createTables();
    }

    public class DatabaseConfig extends Configuration {

        @Comment("Table name for item storage.")
        private String Items = "chestshop_items";

        @Comment("Table name for users")
        private String User = "chestshop_users";
/*
        @Comment({"Table name for transaction logging.", "Has to be enabled in the config."})
        private String TransactionLog = "chestshop_transactionlog";

        @Comment({"Table name for shop creation and destroying logging.", "Has to be enabled in the config."})
        private String BuildLog = "chestshop_transactionlog";*/

        public DatabaseConfig(Path configPath) {
            super(configPath);
        }

        public String getUserTable() {
            return User;
        }

        public String getItemTable() { return Items; }
    }

    private void createTables() {
        try (Statement stmt = getConnection().createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS `$UserTable` (" +
                    "  `id` INT(11) NOT NULL AUTO_INCREMENT," +
                    "  `uuid` CHAR(32) NOT NULL," +
                    "  PRIMARY KEY (`id`)," +
                    "  UNIQUE INDEX `uuid` (`uuid`)" +
                    ")" +
                    "  COLLATE='utf8_general_ci'" +
                    "  ENGINE=InnoDB;";
            query = query.replace("$UserTable", config.getUserTable());
            stmt.execute(query);

            query = "CREATE TABLE IF NOT EXISTS `$ItemTable` (" +
                    "  `id` INT(11) NOT NULL AUTO_INCREMENT," +
                    "  `itemCode` VARCHAR(200) NOT NULL," +
                    "  PRIMARY KEY (`id`)," +
                    "  UNIQUE INDEX `itemCode` (`itemCode`)" +
                    ")" +
                    "  COLLATE='utf8_general_ci'" +
                    "  ENGINE=InnoDB;";
            query = query.replace("$ItemTable", config.getItemTable());
            stmt.execute(query);
/*
            if(generalConfig.isLog_Transaction_To_Database()) {
                query = "CREATE TABLE IF NOT EXISTS `$TransactionLog` (" +
                        "`id` INT(11) NOT NULL AUTO_INCREMENT, " +
                        "`owner` CHAR(36) NOT NULL," +
                        "`client` CHAR (36) NOT NULL," +
                        "`itemID` INT(11) NOT NULL," +
                        "`amount` INT(11) NOT NULL," +
                        "`money` DECIMAL(11,2) NOT NULL, " +
                        "`isBuy` TINYINT(1) NOT NULL," +
                        "`time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP()," +
                        "`servername` CHAR(36)," +
                        "`worldname` CHAR(36)," +
                        "`x` INT(11), `y` INT(11), `z` INT(11))" +
                        "  COLLATE='utf8_general_ci'" +
                        "  ENGINE=InnoDB;";
                query = query.replace("$TransactionLog", config.getTransactionLog());
                stmt.execute(query);
            }
            if(generalConfig.isLog_Shops_To_Database()) {
                query = "CREATE TABLE IF NOT EXISTS `$ShopLog` (" +
                        "`id` INT(11) NOT NULL AUTO_INCREMENT," +
                        "`owner` CHAR(36) NOT NULL," +
                        "`itemID` INT(11) NOT NULL," +
                        "`amount` INT(11) NOT NULL," +
                        "`buyMoney` DECIMAL(11,2), " +
                        "`sellMoney` DECIMAL(11,2), " +
                        "`action` CHAR(10) NOT NULL," + // Update | Creation | Destroyed
                        "`time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP()," +
                        "`servername` CHAR(36)," +
                        "`worldname` CHAR(36)," +
                        "`x` INT(11), `y` INT(11), `z` INT(11))" +
                        "  COLLATE='utf8_general_ci'" +
                        "  ENGINE=InnoDB;";
                query = query.replace("$ShopLog", config.getBuildLog());
                stmt.execute(query);
            }*/
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void getID(final UUID uuid, BiConsumer<Integer, Exception> callback) {
        submitConnectionTask(connection -> {
            int id = -1;
            PreparedStatement pStmt = connection.prepareStatement(
                    "SELECT id FROM " + config.getUserTable() + " WHERE uuid=?");
            pStmt.setString(1, uuid.toString());
            ResultSet rs = pStmt.executeQuery();
            if(rs.next()) {
                id = rs.getInt("id");
            } else {
                PreparedStatement pStmt2 = connection.prepareStatement(
                        "INSERT INTO " + config.getUserTable() + " (uuid) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
                pStmt2.setString(1, uuid.toString());
                pStmt2.executeUpdate();
                ResultSet rs2 = pStmt2.getGeneratedKeys();
                if (rs2.next()) {
                    id = rs.getInt(1);
                } else {
                    id = -1;
                }
                rs2.close();
                pStmt2.close();
            }
            rs.close();
            pStmt.close();

            return id;
        }, callback);
    }

    public int getIDSync(UUID uuid) throws SQLException {
        System.out.println("SpielerID " + Calendar.getInstance().getTimeInMillis());
        Connection connection = getSyncConnection();
        int id = -1;
        PreparedStatement pStmt = connection.prepareStatement(
                "SELECT id FROM " + config.getUserTable() + " WHERE uuid=?");
        pStmt.setString(1, uuid.toString());
        ResultSet rs = pStmt.executeQuery();
        if(rs.next()) {
            id = rs.getInt("id");
        } else {
            PreparedStatement pStmt2 = connection.prepareStatement(
                    "INSERT INTO " + config.getUserTable() + " (uuid) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            pStmt2.setString(1, uuid.toString());
            pStmt2.executeUpdate();
            ResultSet rs2 = pStmt2.getGeneratedKeys();
            if (rs2.next()) {
                id = rs.getInt(1);
            } else {
                id = -1;
            }
            rs2.close();
            pStmt2.close();
        }
        rs.close();
        pStmt.close();
        System.out.println("SpielerID " + Calendar.getInstance().getTimeInMillis());
        return id;
    }

    public void getUUID(final int id, BiConsumer<String, Exception> callback) {
        submitConnectionTask(connection -> {
            String uuid = "";
            PreparedStatement pStmt = connection.prepareStatement(
                    "SELECT uuid FROM " + config.getUserTable() + " WHERE id=?");
            pStmt.setInt(1, id);
            ResultSet rs = pStmt.executeQuery();
            if(rs.next()) {
                uuid = rs.getString(1);
            } else {
                uuid = null;
            }
            rs.close();
            pStmt.close();

            return uuid;
        }, callback);
    }

    public void getItemIDfromCode(String string, BiConsumer<Integer, Exception> callback) {
        submitConnectionTask(connection -> {
            System.out.println("ItemCode: " + string);
            int id = -1;
            PreparedStatement pStmt = connection.prepareStatement(
                    "SELECT id FROM " + config.getItemTable() + " WHERE itemCode = ?");
            pStmt.setString(1, string);
            ResultSet rs = pStmt.executeQuery();
            if(rs.next()) {
                id = rs.getInt("id");
            } else {
                System.out.println("Item neu anlegen.");
                PreparedStatement pStmt2 = connection.prepareStatement(
                        "INSERT INTO " + config.getItemTable() + " (itemCode) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
                pStmt2.setString(1, string);
                pStmt2.executeUpdate();
                ResultSet rs2 = pStmt2.getGeneratedKeys();
                System.out.println("rs2.length " + rs2.getFetchSize());
                if(rs2.next()) {
                    id = rs2.getInt(1);
                }
                rs2.close();
                pStmt2.close();
            }
            rs.close();
            pStmt.close();
            return id;
        }, callback);
    }

    public int getItemIDfromCodeSync(String string) throws SQLException {
        System.out.println("ItemID " + Calendar.getInstance().getTimeInMillis());
        Connection connection = getSyncConnection();
        int id = -1;
        PreparedStatement pStmt = connection.prepareStatement(
                "SELECT id FROM " + config.getItemTable() + " WHERE itemCode = ?");
        pStmt.setString(1, string);
        ResultSet rs = pStmt.executeQuery();
        if(rs.next()) {
            id = rs.getInt("id");
        } else {
            System.out.println("Item neu anlegen.");
            PreparedStatement pStmt2 = connection.prepareStatement(
                    "INSERT INTO " + config.getItemTable() + " (itemCode) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            pStmt2.setString(1, string);
            pStmt2.executeUpdate();
            ResultSet rs2 = pStmt2.getGeneratedKeys();
            System.out.println("rs2.length " + rs2.getFetchSize());
            if(rs2.next()) {
                id = rs2.getInt(1);
            }
            rs2.close();
            pStmt2.close();
        }
        rs.close();
        pStmt.close();
        System.out.println("ItemID " + Calendar.getInstance().getTimeInMillis());
        return id;

    }

    public void getCodeFromItemID(int id, BiConsumer<String, Exception> callback) {
        submitConnectionTask(connection -> {
            String code = null;
            PreparedStatement pStmt = connection.prepareStatement(
                    "SELECT itemCode FROM " + config.getItemTable() + " WHERE id = ?");
            pStmt.setInt(1,id);
            ResultSet rs = pStmt.executeQuery();
            if(rs.next()) {
                code = rs.getString("itemCode");
            } else {
                code = null;
            }
            rs.close();
            pStmt.close();
            return code;
        }, callback);
    }

    public String getCodeFromItemIDSync(int id) throws SQLException {
        Connection connection = getSyncConnection();
        String code = null;
        PreparedStatement pStmt = connection.prepareStatement(
                "SELECT itemCode FROM " + config.getItemTable() + " WHERE id = ?");
        pStmt.setInt(1,id);
        ResultSet rs = pStmt.executeQuery();
        if(rs.next()) {
            code = rs.getString("itemCode");
        } else {
            code = null;
        }
        rs.close();
        pStmt.close();
        return code;

    }

    protected synchronized Connection getSyncConnection() throws SQLException {
        return super.getConnection();
    }
}
