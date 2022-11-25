import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClientURI;
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
			System.out.println("connection established");
			boolean collectionExists = db.listCollectionNames().into(new ArrayList()).contains("auction");
			if (collectionExists == false) {
				db.createCollection("auction");
				System.out.println("auction collection created successfully");
			}
		
		} catch (Exception e) {
            System.out.println("error"); 
    }
		
	}
	
	public void insertToAuction(String auctionId, String itemId, double startingPrice, double buyNowPrice, double currentBid, String bidderId, String startTime, String expireTime) {
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
	
//	public void updateAuction() {
//		
//	}
	
	


	

}
