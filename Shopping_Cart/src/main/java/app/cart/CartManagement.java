package app.cart;

import app.rmiManagement.RemoteCartManagement;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.List;

public class CartManagement extends java.rmi.server.UnicastRemoteObject implements RemoteCartManagement {

    private Database db;
    int port;
    String address;
    Registry registry;

    public CartManagement() throws RemoteException {
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
            registry.rebind("cartManagement", this);
        } catch (RemoteException e) {
            throw e;
        }

        this.db = new Database();
        this.db.createDatabase();
        this.db.createTableIfNotExists();
    }

    public boolean addCart(int user_id, String auction_id, String item_name, double buy_now_price){
        try {
            boolean exist = this.db.checkIfItemExist(user_id, auction_id);
            if (!exist) {   //prevent repeated adding to cart
                this.db.insertIntoTable(user_id, auction_id, item_name, buy_now_price);
            }
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public List<cartItem> getCart(int user_id) {
        try {
            return this.db.getCartByUser(user_id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public boolean clearCart(int user_id){
        try {
            List<cartItem> items = this.db.getCartByUser(user_id);
            for (cartItem item : items) {
                this.db.clearItemCart(item.getAuction_id());
            }
            //this.db.clearUserCart(user_id);
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
}
