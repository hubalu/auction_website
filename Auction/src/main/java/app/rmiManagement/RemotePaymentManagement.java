package app.rmiManagement;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemotePaymentManagement extends Remote {
    boolean insertToBankBalance(String userId, double initialAmount) throws RemoteException;
    boolean makePayment(String userId, double amount) throws RemoteException;
    double viewBalance(String userId) throws RemoteException;
}