package app.payment;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.FindIterable;

public class PaymentTest {

	public static void main(String[] args) throws RemoteException {
		
		PaymentManagement pm = new PaymentManagement();
		// basic test case
		pm.insertToBankBalance("1005", 100);
		pm.makePayment("1004", 18);
		
		// edge test case 1 - makepayment with the userId that does not exist
		// show error
		//pm.makePayment("1008", 30.8); 
		
		// edge test case 2 - currentAmount < paymentAmount
		// throw an error and currentAmount will not change
		//pm.makePayment("1001", 1000);
		
		
	}
}
//http://localhost:4567/login	
//docker compose build --no-cache
//docker compose up