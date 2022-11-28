import java.util.Iterator;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.FindIterable;

public class PaymentTest {

	public static void main(String[] args) {
		
		PaymentManagement pm = new PaymentManagement();
		// basic test case
		pm.insertToBankBalance("1001", 100);
		pm.makePayment("1001", 30.8);
		
		// edge test case 1 - makepayment with the userId that does not exist
		// show error
		pm.makePayment("1008", 30.8); 
		
		// edge test case 2 - currentAmount < paymentAmount
		// throw an error and currentAmount will not change
		pm.makePayment("1001", 1000);
		
		
	}
}
	
