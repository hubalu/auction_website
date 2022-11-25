package app.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author skr
 */
public class Database {

    private Connection conn;
    private Set<String> table_names;

    public Database(String database_path){
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/" + database_path);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }
        System.out.println("Database opened successfully");
    }

    public void createTableIfNotExists(String table_name){
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + table_name +
                    " (ItemId INT PRIMARY KEY," +
                    " UserId INT NOT NULL," +
                    "ItemName TEXT NOT NULL)" +
                    "Description TEXT" +
                    "Category TEXT" +
                    "Flag BOOLEAN DEFAULT FALSE";

            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }
    }


    public void insertIntoTable(String user_id, String item_name, String description, String category){
        //TODO 5. Use the sqlite connection to insert a new record into the
        //the database.
        //The timestamp can be insertion time and doesn't have to be the actual
        //fetch time
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String sql = "INSERT INTO item (UserId, ItemName, Description, Category) VALUES ("
                    + user_id + "," + item_name + ", " + description + "," + category +")";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void executeSQL(String sql){
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }
    }
}

