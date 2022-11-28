package app.rmiManagement;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteUserManagement extends Remote {

    List<String> getEmailList(List<String> ids) throws RemoteException;
}
