package app.rmiManagement;

import app.database.Database;
import app.item.Item;

import java.rmi.*;
import java.rmi.registry.*;
import java.net.*;
import java.util.List;

//test
public class ItemManagement extends java.rmi.server.UnicastRemoteObject implements RemoteItemManagement {
    int port;
    String address;
    Registry registry;

    public Database db;



    public ItemManagement() throws RemoteException {
        try {
            // get the address of this host.
            address = (InetAddress.getLocalHost()).toString();
//            address = "item";
        } catch (Exception e) {
            throw new RemoteException("can't get inet address.");
        }
        port = 12345;  // our port
        System.out.println("using address=" + address + ",port=" + port);
        try {
            // create the registry and bind the name and object.
            registry = LocateRegistry.createRegistry(port);
            registry.rebind("itemManagement", this);
        } catch (RemoteException e) {
            throw e;
        }

        db = new Database("test");
        db.createTableIfNotExists("item");
    }

    public boolean upload_item(String user_id, String item_name, String description, String category) throws RemoteException{
        // Need to create a unique item id
        System.out.println(user_id + "/" + item_name + "/" + description + "/" + category);
        String sql = "INSERT INTO item (UserId, ItemName, Description, Category) VALUES ("
                + user_id + ",'" + item_name + "', '" + description + "','" + category +"')";
        return db.modifySQL(sql);
    }

    public boolean remove_item(String item_id) throws RemoteException{
        String sql = "DELETE FROM item where ItemId = " + item_id;
        return db.modifySQL(sql);
    }

    public boolean update_item(String field, Object value) throws RemoteException{
        return true;
    }

    public boolean flag_item(int item_id) throws RemoteException{
        String sql = "UPDATE item SET Flag = TRUE where ItemId = " + item_id;
        return db.modifySQL(sql);
    }

    public List<Item> get_flag_items() throws RemoteException{
        String sql = "SELECT * FROM item where Flag = TRUE";
        return db.querySQL(sql);
    }

    public List<Item> search_item(String key_word, String Category, String sort_key, boolean desc) throws RemoteException {
        String sql;
        if(key_word != null){
            sql = "SELECT * FROM item where itemname LIKE '%" + key_word + "%'";
        } else if (Category != null) {
            sql = "SELECT * FROM item where category = '" + Category +"'";
        } else {
            sql = "SELECT * FROM item";
        }

        if (sort_key == null){
            System.out.println("Error! Sort key couldn't be null");
            return null;
        }
        sql = sql + " ORDER BY " + sort_key;

        if(desc){
            sql = sql + " DESC";
        }else{
            sql = sql + " ASC";
        }
        return db.querySQL(sql);
    }

    public boolean update_category(String prevName, String newName){
        String sql = "UPDATE item SET category = '" + newName + "' WHERE category = '" + prevName + "'";
        return db.modifySQL(sql);

    }

    public boolean delete_category(String deleteCategory){
        String sql = "UPDATE item SET category = NULL WHERE category = '" + deleteCategory + "'";
        return db.modifySQL(sql);
    }

}
