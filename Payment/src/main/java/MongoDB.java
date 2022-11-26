import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

import com.mongodb.BasicDBObject;
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
			db = mongoClient.getDatabase("bankBalance");
			System.out.println("connection established to bankBalance database");
			boolean bankBalanceExists = db.listCollectionNames().into(new ArrayList()).contains("bankBalance");
			
			if (bankBalanceExists == false) {
				db.createCollection("bankBalance");
				System.out.println("bankBalance collection created successfully");
			}
		
		} catch (Exception e) {
            		System.out.println("error in database initialization"); 
    		}	
	}
	
	public void insertToBankBalance(String userId, double initialAmount) {
		try {
			Document doc = new Document();
			doc.append("userId", userId);
			doc.append("amount", initialAmount);
			db.getCollection("bankBalance").insertOne(doc);
			System.out.println("added successfully");
		} catch (Exception e) {
	            System.out.println("error in insertToBankBalance"); 
	    }
	}

	public void makePayment(String userId, double paymentAmount) {
		try {
			MongoCollection<Document> balance = db.getCollection("bankBalance");
			Document query = new Document("userId", userId);
			
		    	double currentAmount = balance.find(query).first().getDouble("amount");
			balance.updateOne(Filters.eq("userId", userId), Updates.set("amount", currentAmount - paymentAmount));
			
		} catch (Exception e) {
			System.out.println("error in makePayment"); 
		}
	}
}
