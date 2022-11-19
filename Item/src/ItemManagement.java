import java.util.List;
import java.rmi.*;
import java.rmi.registry.*;
import java.net.*;

//test
public class ItemManagement extends java.rmi.server.UnicastRemoteObject implements RemoteItemManagement {
    int port;
    String address;
    Registry registry;

    public static void main(String args[]) {
        try {
            ItemManagement service = new ItemManagement();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public ItemManagement() throws RemoteException {
        try {
            // get the address of this host.
            address = (InetAddress.getLocalHost()).toString();
        } catch (Exception e) {
            throw new RemoteException("can't get inet address.");
        }
        port = 5678;  // our port
        System.out.println("using address=" + address + ",port=" + port);
        try {
            // create the registry and bind the name and object.
            registry = LocateRegistry.createRegistry(port);
            registry.rebind("itemManagement", this);
        } catch (RemoteException e) {
            throw e;
        }
    }

    public boolean upload_item(String user_id, String item_name, String description, String category) throws RemoteException{
        // Need to create a unique item id
        System.out.println(user_id + "/" + item_name + "/" + description + "/" + category);
        return true;
    }

//    public boolean update_item(String field, Object value) {
//        return true;
//    }
//
//    public void flag_item(int item_id) {
//    }
//
//    public List<String> search_item(String key_word, String Category, String sort_key, String order) {
//        return null;
//    }
//
//    public void update_category(String action, String category) {
//
//    }
//
//    public boolean add_to_watchlist(int item_id, int user_id) {
//        return true;
//    }
}
