package app.rmiManagement;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIHelper {
    public static RemoteAuctionManagement getRemAuctionManagement() {
        RemoteAuctionManagement remoteObj = null;
        Registry registry;
        while (true){
            try { // get the registry
                Thread.sleep(500);
                registry = LocateRegistry.getRegistry("auction", 12345);
                // look up the remote object in the RMI Registry
                remoteObj = (RemoteAuctionManagement) (registry.lookup("auctionManagement"));
            } catch (Exception e) {
                System.out.println("connecting to User RMI failed...try again...");
                continue;
            }
            System.out.println("connected to User RMI successfully!!!");
            break;
        }
        return remoteObj;
    }
}
