package app.rmiManagement;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemotePaymentManagement extends Remote {
	 boolean insertToBankBalance(String userId, int initialAmount) throws RemoteException;
	 boolean makePayment(String userId, double amount) throws RemoteException;
}