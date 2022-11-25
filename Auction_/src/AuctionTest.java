import java.util.Iterator;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.FindIterable;

public class AuctionTest {

	public static void main(String[] args) {
		
		AuctionManagement auc = new AuctionManagement();
	
		auc.listForAuction("1001", "106", 200.0, 460.5, 211, "1", "11/23/2022", "12/06/2022", "2");
		//auc.placeBid("1006", "6", 300);
		//auctionId, userId, param, newVal
		//auc.updateAuction("1007", "2", "expireTime", "12/11/2022");
		
	}
}
	
