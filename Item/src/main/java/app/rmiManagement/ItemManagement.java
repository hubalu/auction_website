package app.rmiManagement;

import app.database.Database;

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

        db = new Database("Item");
        db.createTableIfNotExists("item");
    }

    public void upload_item(String user_id, String item_name, String description, String category) throws RemoteException{
        // Need to create a unique item id
        System.out.println(user_id + "/" + item_name + "/" + description + "/" + category);
        String sql = "INSERT INTO item (UserId, ItemName, Description, Category) VALUES ("
                + user_id + "," + item_name + ", " + description + "," + category +")";
        db.executeSQL(sql);
    }

    public void remove_item(String item_id){
        String sql = "DELETE FROM item where ItemId = " + item_id;
        db.executeSQL(sql);
    }

    public boolean update_item(String field, Object value) {
        return true;
    }

    public void flag_item(int item_id) {
        String sql = "UPDATE item SET Flag = TRUE where ItemId = " + item_id;
        db.executeSQL(sql);
    }

    public List<String> search_item(String key_word, String Category, String sort_key, String order) {
        return null;
    }

    public void update_category(String action, String category) {

    }


}
