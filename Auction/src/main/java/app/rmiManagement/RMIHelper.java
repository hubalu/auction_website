package app.rmiManagement;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIHelper {
    public RemoteUserManagement getRemUserManagement(){
        RemoteUserManagement remoteObj = null;
        Registry registry;
        try{
            // get the registry
            registry= LocateRegistry.getRegistry("user", 12345);
            // look up the remote object in the RMI Registry
            remoteObj= (RemoteUserManagement)(registry.lookup("userManagement"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return remoteObj;
    }
}
