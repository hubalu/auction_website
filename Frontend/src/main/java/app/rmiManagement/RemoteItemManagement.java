package app.rmiManagement;

import app.item.Item;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteItemManagement extends Remote {
    boolean upload_item(String user_id, String item_name, String description, String category) throws RemoteException;

    boolean remove_item(String item_id) throws RemoteException;

    boolean flag_item(String item_id) throws RemoteException;

    List<Item> get_flag_items() throws RemoteException;

    List<Item> search_item(String key_word, String Category, String sort_key, boolean desc) throws RemoteException;

    boolean update_category(String prevName, String newName) throws RemoteException;
    boolean delete_category(String deleteCategory) throws RemoteException;
}
