package app.payment;

import app.rmiManagement.RemotePaymentManagement;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;


public class PaymentManagement extends java.rmi.server.UnicastRemoteObject implements RemotePaymentManagement{

	private MongoDB db;
	int port;
    String address;
    Registry registry;
    
    public PaymentManagement() throws RemoteException {
    	try {
    		db = new MongoDB("");
    		try {
                // get the address of this host.
                address = (InetAddress.getLocalHost()).toString();
            } catch (Exception e) {
                throw new RemoteException("can't get inet address.");
            }
            port = 54321;  // our port
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
	
	public boolean insertToBankBalance(String userId, double initialAmount) {
		try {
			this.db.insertToBankBalance(userId, initialAmount);
			System.out.println("Successfully insert into bank account of userId " + userId + " with $" +initialAmount);
			return true;

		} catch (Exception e){
			System.out.println("error in insertToBankBalance"); 
			return false;
		}
	}
	
	public boolean makePayment(String userId, double amount) {
		try {
			this.db.makePayment(userId, amount);
			System.out.println("Successfully make payment $" + amount);
			return true;
		} catch (Exception e){
			System.out.println(e);
			System.out.println("error in makePayment"); 
			return false;
		}
	}
	
	public double viewBalance(String userId) {
		double amount = 0;
		try {
			amount = this.db.viewBalance(userId);
			System.out.println("Current balance $" + amount);
		} catch (Exception e){
			System.out.println(e);
			System.out.println("error in viewBalance"); 
		}
		return amount;
	}
}
