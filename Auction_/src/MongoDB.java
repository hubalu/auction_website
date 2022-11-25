import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class MongoDB {
	private MongoClient mongoClient;
	private MongoDatabase db;
	
	public MongoDB(String database_path) {
		try {
			mongoClient = MongoClients.create();
			db = mongoClient.getDatabase("auctionSite");
			System.out.println("connection established to auctionSite database");
			boolean auctionExists = db.listCollectionNames().into(new ArrayList()).contains("auction");
			boolean watchlistExists = db.listCollectionNames().into(new ArrayList()).contains("watchlist");
			
			if (auctionExists == false) {
				db.createCollection("auction");
				System.out.println("auction collection created successfully");
			}
			
			if (watchlistExists == false) {
				db.createCollection("watchlist");
				System.out.println("watchlist collection created successfully");
			}
			
		
		} catch (Exception e) {
            System.out.println("error in database initialization"); 
    }
		
	}
	
	public void insertToAuction(String auctionId, String itemId, double startingPrice, double buyNowPrice, double currentBid, 
							String bidderId, String startTime, String expireTime, String sellerId) {
		try {
			Document doc = new Document();
			doc.append("auctionId", auctionId);
			doc.append("itemId", itemId);
			doc.append("startingPrice", startingPrice);
			doc.append("buyNowPrice", buyNowPrice);
			doc.append("currentBid", currentBid);
			doc.append("bidderId", bidderId);
			doc.append("startTime", startTime);
			doc.append("expireTime", expireTime);
			doc.append("sellerId", sellerId);
			db.getCollection("auction").insertOne(doc);
			System.out.println("added successfully");
		} catch (Exception e) {
	            System.out.println("error in insertToAuction"); 
	    }
	}
	
	public void placeBid(String auctionId, String userId, double bidPrice) {
		try {
			MongoCollection<Document> auction = db.getCollection("auction");
			auction.updateOne(Filters.eq("auctionId", auctionId), Updates.set("currentBid", bidPrice));
			auction.updateOne(Filters.eq("auctionId", auctionId), Updates.set("bidderId", userId));
		} catch (Exception e) {
			System.out.println("error in placeBid"); 
		}
	}
	
	public List getAuctions() {
		try {
			MongoCollection<Document> auction = db.getCollection("auction");
			//FindIterable<Document> iterDoc = auction.find();
			//return iterDoc;
			List results = new ArrayList<>();
			auction.find().into(results);
			return results;
		} catch (Exception e) {
			System.out.println("error in getAuctions");
			return null;
		}
		
	}
	
	public void endAuction(String auctionId) {
		try {
			MongoCollection<Document> auction = db.getCollection("auction");
			Document query = new Document("auctionId", auctionId);
			auction.deleteOne(query);
		} catch (Exception e) {
			System.out.println("error in endAuction");
		}
		
	}
	
	public void updateAuction(String auctionId, String userId, String param, String newVal) {
		
		MongoCollection<Document> auction = db.getCollection("auction");
		Document query = new Document("auctionId", auctionId);
		
		if(auction.find(query).first().getString("sellerId").equals(userId)){
			try {
				
				if(param.equals("startingPrice") || param.equals("buyNowPrice")) {
					auction.updateOne(Filters.eq("auctionId", auctionId), Updates.set(param, Double.valueOf(newVal)));
				} 
				else {
					auction.updateOne(Filters.eq("auctionId", auctionId), Updates.set(param, newVal));
				}
				
			} catch (Exception e) {
				System.out.println("error in updateAuction database");
			}
		}
	
	}
	
	public void addToWatchlist(String itemId, String userId) {
		try {
			MongoCollection<Document> watchlist = db.getCollection("watchlist");
			List<String> users = new ArrayList<String>();
			if(!itemExistInWatchlist(itemId)) {
				Document doc = new Document();
				doc.append("itemId", itemId);
				 
		        users.add(userId);
		        watchlist.insertOne(doc);
		        watchlist.findOneAndUpdate(Filters.eq("itemId", itemId), Updates.pushEach("watchlist", users));
		       
				System.out.println("added successfully");
			}else {
				users.add(userId);
				watchlist.findOneAndUpdate(Filters.eq("itemId", itemId), Updates.pushEach("watchlist", users));
			}
			
			
			
		} catch (Exception e) {
			System.out.println("error in addToWatchlist");
		}
	}
	
	public boolean itemExistInWatchlist(String itemId) {
	    FindIterable<Document> iterable = db.getCollection("watchlist")
	                                        .find(new Document("itemId", itemId));
	    return iterable.first() != null;
	}
	
}
