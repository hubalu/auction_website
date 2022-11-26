import java.util.List;

public class PaymentManagement {

	private MongoDB db;
	
	public PaymentManagement() {
		try {
			db = new MongoDB("");
		} catch (Exception e){
			System.out.println("error in PaymentManagement"); 
		}
	}
	public boolean insertToBankBalance(String userId, int initialAmount) {
		try {
			this.db.insertToBankBalance(userId, initialAmount);
			System.out.println("Successfully insert into bank account of userId " + userId + " with $" +initialAmount);
			return true;

		} catch (Exception e){
			System.out.println("error in placeBid"); 
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
	
//	public boolean updateAuction(String auctionId, String userId, String param, String newVal) {
//		try {
//			this.db.updateAuction(auctionId, userId, param, newVal);
//			System.out.println("Succesfully updated auction item " + auctionId + " with " + param + " = " + newVal);
//			return true;
//		} catch (Exception e){
//			System.out.println(e);
//			System.out.println("error in updateAuction"); 
//			return false;
//		}
//	}
	
	
	
}
