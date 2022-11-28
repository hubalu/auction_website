package app.auction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import static com.mongodb.client.model.Filters.eq;


public class MongoDB {
	private MongoClient mongoClient;
	private MongoDatabase db;
	MongoCollection<Document> auctionDocs;


	
	public MongoDB(String database_path) {
		try {
			mongoClient = MongoClients.create();
			db = mongoClient.getDatabase("auctionSite");
			System.out.println("connection established to auctionSite database");
			boolean auctionExists = db.listCollectionNames().into(new ArrayList()).contains("app/auction");
			boolean watchlistExists = db.listCollectionNames().into(new ArrayList()).contains("watchlist");
			
			if (auctionExists == false) {
				db.createCollection("app/auction");
				System.out.println("auction collection created successfully");
			}
			auctionDocs = db.getCollection("app/auction");

			if (watchlistExists == false) {
				db.createCollection("watchlist");
				System.out.println("watchlist collection created successfully");
			}
			
		
		} catch (Exception e) {
            System.out.println("error in database initialization"); 
    }
		
	}
	
	public void insertToAuction(String auctionId, String itemId, String itemName, Double startingPrice, Double buyNowPrice, String startTime, String expireTime, String sellerId) {
		try {
			Document doc = new Document();
			doc.append("auctionId", auctionId);
			doc.append("itemId", itemId);
			doc.append("itemName", itemName);
			doc.append("startingPrice", startingPrice);
			doc.append("buyNowPrice", buyNowPrice);
			doc.append("startTime", startTime);
			doc.append("expireTime", expireTime);
			doc.append("sellerId", sellerId);
			doc.append("active", true);
			db.getCollection("app/auction").insertOne(doc);
			System.out.println("added successfully");
		} catch (Exception e) {
	            System.out.println("error in insertToAuction"); 
	    }
	}
	
	public void placeBid(String auctionId, String userId, double bidPrice) {
		try {
			MongoCollection<Document> auction = db.getCollection("app/auction");
			auction.updateOne(eq("auctionId", auctionId), Updates.set("currentBid", bidPrice));
			auction.updateOne(eq("auctionId", auctionId), Updates.set("bidderId", userId));
		} catch (Exception e) {
			System.out.println("error in placeBid"); 
		}
	}
	
	public List<AuctionDesc> getAuctions() {
		try {
			MongoCollection<Document> auction = db.getCollection("app/auction");
			//FindIterable<Document> iterDoc = auction.find();
			//return iterDoc;
			List<AuctionDesc> results = new ArrayList<>();
			for(Document doc : auction.find(eq("active", true))){
				results.add(getAuctionDesc(doc.getObjectId("_id")));
			};
			return results;
		} catch (Exception e) {
			System.out.println("error in getAuctions");
			return null;
		}
		
	}
	
	public void endAuction(String auctionId) {
		try {
			MongoCollection<Document> auction = db.getCollection("app/auction");
			Document query = new Document("auctionId", auctionId);
			auction.deleteOne(query);
		} catch (Exception e) {
			System.out.println("error in endAuction");
		}
		
	}
	
	public void updateAuction(String auctionId, String userId, String param, String newVal) {
		
		MongoCollection<Document> auction = db.getCollection("app/auction");
		Document query = new Document("auctionId", auctionId);
		
		if(auction.find(query).first().getString("sellerId").equals(userId)){
			try {
				
				if(param.equals("startingPrice") || param.equals("buyNowPrice")) {
					auction.updateOne(eq("auctionId", auctionId), Updates.set(param, Double.valueOf(newVal)));
				} 
				else {
					auction.updateOne(eq("auctionId", auctionId), Updates.set(param, newVal));
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
		        watchlist.findOneAndUpdate(eq("itemId", itemId), Updates.pushEach("watchlist", users));
		       
				System.out.println("added successfully");
			}else {
				users.add(userId);
				watchlist.findOneAndUpdate(eq("itemId", itemId), Updates.pushEach("watchlist", users));
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

	public AuctionDesc getAuctionDesc(ObjectId objectId){
		AuctionDesc auctionDesc = null;
		try{
			Document doc = auctionDocs.find(eq("_id", objectId)).first();
			if(doc == null){
				throw new RuntimeException("Auction id couldn't be found");
			}
			auctionDesc = new AuctionDesc(objectId.toString(), doc.getString("itemId"), doc.getString("itemName"),
					doc.getDouble("startingPrice"), doc.getDouble("buyNowPrice"), doc.getDouble("currentBid"), doc.getString("bidderId"),
					doc.getString("startTime"), doc.getString("expireTime"), doc.getString("sellerId"));
		}catch (Exception e){
			e.printStackTrace();
		}
		return auctionDesc;
	}
}
