package app.rmiManagement;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.Permission;

public class RMIHelper {

    private class MySecurityManager extends SecurityManager {
        @Override
        public void checkPermission(Permission perm) {
            return;
        }
    }
    public RMIHelper(){
//        if (System.getSecurityManager() == null) {
//            SecurityManager sm = new MySecurityManager();
//            System.setSecurityManager(sm);
//        }
    }
    public RemoteItemManagement getRemItemManagement(){
        RemoteItemManagement remoteObj = null;
        Registry registry;
        try{
            // get the registry
            registry= LocateRegistry.getRegistry("172.17.0.4", 12345);
            // look up the remote object in the RMI Registry
            remoteObj= (RemoteItemManagement)(registry.lookup("itemManagement"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return remoteObj;
    }

    private Remote registryLookUp(String serverAddress, String port, String lookupName){
        Remote remoteObj = null;
        Registry registry;
        try{
//            String serverAddress=(InetAddress.getLocalHost()).toString();
//            String serverPort="12345";
            // get the registry
            registry= LocateRegistry.getRegistry(
                    serverAddress,
                    (new Integer(port)).intValue()
            );
            // look up the remote object in the RMI Registry
//            itemManagement= (registry.lookup("rmiServer"));
            remoteObj= (registry.lookup(lookupName));
            // call the remote method
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return remoteObj;
    }
}
