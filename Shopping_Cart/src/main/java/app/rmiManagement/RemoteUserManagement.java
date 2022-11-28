package app.rmiManagement;

import app.user.cartItem;

import java.rmi.Remote;
import java.util.List;

public interface RemoteUserManagement extends Remote{
    boolean addCart(int user_id, int auction_id, String item_name, double buy_now_price);
    List<cartItem> getCart(int user_id);
    boolean clearCart(int user_id);
}
