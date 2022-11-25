package app.database;

import app.item.Item;

import java.sql.*;
import java.util.LinkedList;
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
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database_path);
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
                    " (ItemId SERIAL PRIMARY KEY," +
                    " UserId INT NOT NULL," +
                    "ItemName TEXT NOT NULL," +
                    "Description TEXT," +
                    "Category TEXT, " +
                    "Flag BOOLEAN DEFAULT FALSE,"+
                    "UploadTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL)";

            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }
    }

    public boolean modifySQL(String sql){
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Item> querySQL(String sql){
        Statement stmt;
        List<Item> res = new LinkedList<>();
        try{
            stmt = conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(sql);
            while (queryResult.next()){
//                String temp = "";
//                temp = temp + "Item ID:" + queryResult.getInt("ItemId") + " ";
//                temp = temp + "Owner ID" + queryResult.getInt("UserId") + " ";
//                temp = temp + "Name" + queryResult.getString("ItemName") + " ";
//                temp = temp + "Description" + queryResult.getString("Description") + " ";
//                temp = temp + "is inappropriate? " + queryResult.getBoolean("Flag");
//                res.add(temp);
                Item item = new Item(
                        Integer.toString(queryResult.getInt("ItemId")),
                        Integer.toString(queryResult.getInt("UserId")),
                        queryResult.getString("ItemName"),
                        queryResult.getString("Description"),
                        queryResult.getString("Category"),
                        queryResult.getBoolean("Flag"),
                        queryResult.getTimestamp("UploadTime").toString());
                res.add(item);
            }
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return res;
    }
}

