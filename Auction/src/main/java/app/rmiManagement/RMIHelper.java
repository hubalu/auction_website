package app.rmiManagement;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIHelper {
    public RemoteUserManagement getRemUserManagement() throws InterruptedException {
        RemoteUserManagement remoteObj = null;
        Registry registry;
        while (true){
            try { // get the registry
                Thread.sleep(500);
                registry = LocateRegistry.getRegistry("user", 12345);
                // look up the remote object in the RMI Registry
                remoteObj = (RemoteUserManagement) (registry.lookup("userManagement"));
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
