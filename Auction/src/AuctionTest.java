import java.util.Iterator;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.FindIterable;

public class AuctionTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MongoDB mongo = new MongoDB("");
		
		//mongo.insertToAuction("1004", "106", 200.0, 460.5, 211, "1", "11/23/2022", "12/06/2022");
		//mongo.placeBid("1004", "5", 250);
//		Iterator it = iterDoc.iterator();
//	      while (it.hasNext()) {
//	         System.out.println(it.next());
//	      }
		
		mongo.endAuction("1004");
		
		List allAuctions = mongo.getAuctions();
		for (int i = 0; i < allAuctions.size(); i++) {
			 
            // Print all elements of List
            System.out.println(allAuctions.get(i));
        }
	}

}
