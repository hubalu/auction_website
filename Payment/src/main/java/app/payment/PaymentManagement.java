package app.payment;

import app.rmiManagement.RemotePaymentManagement;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;


public class PaymentManagement extends java.rmi.server.UnicastRemoteObject implements RemotePaymentManagement {

    private MongoDB db;
    int port;
    String address;
    Registry registry;

    public PaymentManagement() throws RemoteException {
        try {
            db = new MongoDB("Bank");
            try {
                // get the address of this host.
                address = (InetAddress.getLocalHost()).toString();
            } catch (Exception e) {
                throw new RemoteException("can't get inet address.");
            }
            port = 12345;  // our port
            System.out.println("using address=" + address + ",port=" + port);
            try {
                // create the registry and bind the name and object.
                registry = LocateRegistry.createRegistry(port);
                registry.rebind("paymentManagement", this);
            } catch (RemoteException e) {
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean insertToBankBalance(String userId, double initialAmount) throws RemoteException {
        return this.db.insertToBankBalance(userId, initialAmount);
    }

    public boolean makePayment(String userId, double amount) throws RemoteException {
        return this.db.makePayment(userId, amount);
    }

    public double viewBalance(String userId) throws RemoteException{
        return this.db.viewBalance(userId);
    }
}
