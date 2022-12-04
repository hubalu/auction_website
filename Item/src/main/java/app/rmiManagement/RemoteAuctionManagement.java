package app.rmiManagement;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteAuctionManagement extends Remote {
    boolean checkOnAuction(String itemId) throws RemoteException;
}
