package app.rmiManagement;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemotePaymentManagement extends Remote {
	boolean insertToBankBalance(String userId, double initialAmount) throws RemoteException;
	boolean makePayment(String userId, double amount) throws RemoteException;
	double viewBalance(String userId) throws RemoteException;
}