import java.util.Iterator;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.FindIterable;

public class PaymentTest {

	public static void main(String[] args) {
		
		PaymentManagement pm = new PaymentManagement();
		//pm.insertToBankBalance("1001", 100);
		pm.makePayment("1002", 30);
		
	}
}
	
