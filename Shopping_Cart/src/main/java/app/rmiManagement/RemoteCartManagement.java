package app.rmiManagement;

import app.cart.cartItem;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteCartManagement extends Remote{
    boolean addCart(int user_id, String auction_id, String item_name, double buy_now_price) throws RemoteException;
    List<cartItem> getCart(int user_id) throws RemoteException;
    boolean clearCart(int user_id) throws RemoteException;
}
