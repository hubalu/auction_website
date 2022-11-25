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
	
	public boolean listForAuction(String auctionId, String itemId, double startingPrice,
					 double buyNowPrice, double currentBid, String bidderId, String startTime, String expireTime, String sellerId) {
		try {
			this.db.insertToAuction(auctionId, itemId, startingPrice, buyNowPrice, currentBid, bidderId, startTime, expireTime, sellerId);
			System.out.println("Successfully added item " + itemId + " to auction");
			return true;
		} catch (Exception e){
			System.out.println("error in listForAuction"); 
			return false;
		}
	}
	
	public boolean placeBid(String auctionId, String userId, double bidPrice) {
		try {
			this.db.placeBid(auctionId, userId, bidPrice);
			System.out.println("Successfully placed bid on " + auctionId);
			return true;

		} catch (Exception e){
			System.out.println("error in placeBid"); 
			return false;
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
	
	public boolean endAuction(String auctionId) {
		try {
			this.db.endAuction(auctionId);
			System.out.println("Successfully ended auction on " + auctionId);
			return true;
		} catch (Exception e){
			System.out.println("error in endAuction"); 
			return false;
		}
	}
	
	public boolean updateAuction(String auctionId, String userId, String param, String newVal) {
		try {
			this.db.updateAuction(auctionId, userId, param, newVal);
			System.out.println("Succesfully updated auction item " + auctionId + " with " + param + " = " + newVal);
			return true;
		} catch (Exception e){
			System.out.println(e);
			System.out.println("error in updateAuction"); 
			return false;
		}
	}
	
	public boolean addToWatchlist(String itemId, String userId) {
		try {
			this.db.addToWatchlist(itemId, userId);
			System.out.println("Succesfully added userId " + userId + " to watchlist_" + itemId);
			return true;
		}catch (Exception e){
			System.out.println(e);
			System.out.println("error in addToWatchlist"); 
			return false;
		}
	}
	

}
