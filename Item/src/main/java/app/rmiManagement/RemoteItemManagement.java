package app.rmiManagement;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteItemManagement extends Remote {
    void upload_item(String user_id, String item_name, String description, String category) throws RemoteException;
}
