package app.rmiManagement;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import app.item.Item;

public interface RemoteItemManagement extends Remote {
    boolean upload_item(String user_id, String item_name, String description, String category) throws RemoteException;

    boolean remove_item(String item_id);

    boolean update_item(String field, Object value);

    boolean flag_item(int item_id);

    List<Item> search_item(String key_word, String Category, String sort_key, boolean desc);

    boolean update_category(String prevName, String newName);
    boolean delete_category(String deleteCategory);
}
