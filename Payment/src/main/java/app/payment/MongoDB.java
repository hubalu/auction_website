package app.payment;

import java.util.ArrayList;

import org.bson.Document;

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
            while (true) {
                try {
                    Thread.sleep(500);
                    mongoClient = MongoClients.create("mongodb://mongo:27017");
                } catch (Exception e) {
                    System.out.println("Failed to connect to mongodb...try again...");
                    continue;
                }
                System.out.println("Connected to mongodb successfully!!!");
                break;
            }
            db = mongoClient.getDatabase(database_path);
            System.out.println("connection established to bankBalance database");
            boolean bankBalanceExists = db.listCollectionNames().into(new ArrayList()).contains("bankBalance");

            if (!bankBalanceExists) {
                db.createCollection("bankBalance");
                System.out.println("bankBalance collection created successfully");
            }

        } catch (Exception e) {
            System.out.println("error in database initialization");
        }
    }

    public boolean insertToBankBalance(String userId, double addedAmount) {
        try {
            MongoCollection<Document> balance = db.getCollection("bankBalance");
            Document doc = new Document();
            Document query = new Document("userId", userId);
            Document userAccount = balance.find(query).first();
            if (userAccount == null){
                doc.append("userId", userId);
                doc.append("amount", addedAmount);
                balance.insertOne(doc);
            }else{
                double currentAmount = userAccount.getDouble("amount");
                balance.updateOne(Filters.eq("userId", userId), Updates.set("amount", currentAmount + addedAmount));
            }
            System.out.println(userId + " inserted " + addedAmount + " money successfully");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean makePayment(String userId, double paymentAmount) {
        try {
            MongoCollection<Document> balance = db.getCollection("bankBalance");
            Document query = new Document("userId", userId);

            double currentAmount = balance.find(query).first().getDouble("amount");
            if (currentAmount < paymentAmount) {
                throw new RuntimeException("current balance of user account is less than the payment amount!");
            }
            balance.updateOne(Filters.eq("userId", userId), Updates.set("amount", currentAmount - paymentAmount));
            System.out.println(userId + " successfully make payment $" + paymentAmount);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public double viewBalance(String userId) {
        try {
            MongoCollection<Document> balance = db.getCollection("bankBalance");
            Document query = new Document("userId", userId);
            double currentAmount = balance.find(query).first().getDouble("amount");
            return currentAmount;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
