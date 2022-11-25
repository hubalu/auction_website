import java.util.List;

public class AuctionManagement {

	private MongoDB db;
	
	public AuctionManagement() {
		try {
			db = new MongoDB("");
		} catch (Exception e){
			System.out.println("error in AuctionManagement"); 
		}
	}
	
	public void listForAuction(String auctionId, String itemId, double startingPrice,
					 double buyNowPrice, double currentBid, String bidderId, String startTime, String expireTime, String sellerId) {
		try {
			this.db.insertToAuction(auctionId, itemId, startingPrice, buyNowPrice, currentBid, bidderId, startTime, expireTime, sellerId);
			System.out.println("Successfully added item " + itemId + " to auction");
		} catch (Exception e){
			System.out.println("error in listForAuction"); 
		}
	}
	
	public void placeBid(String auctionId, String userId, double bidPrice) {
		try {
			this.db.placeBid(auctionId, userId, bidPrice);
			System.out.println("Successfully placed bid on " + auctionId);

		} catch (Exception e){
			System.out.println("error in placeBid"); 
		}
	}
	
	public List getAuctions() {
		try {
			List listOfAuctions = this.db.getAuctions();
			return listOfAuctions;
		} catch (Exception e){
			System.out.println("error in getAuctions"); 
			return null;
		}
	}
	
	public void endAuction(String auctionId) {
		try {
			this.db.endAuction(auctionId);
			System.out.println("Successfully ended auction on " + auctionId);
		} catch (Exception e){
			System.out.println("error in endAuction"); 
		}
	}
	
	public void updateAuction(String auctionId, String userId, String param, String newVal) {
		try {
			this.db.updateAuction(auctionId, userId, param, newVal);
			System.out.println("Succesfully updated auction item " + auctionId + " with " + param + " = " + newVal);
		} catch (Exception e){
			System.out.println(e);
			System.out.println("error in updateAuction"); 
		}
	}

}
