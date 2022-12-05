package app.user;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;


import static com.mongodb.client.model.Filters.eq;

public class CategoryDao {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection collection;

    public CategoryDao(){
        mongoClient = MongoClients.create("mongodb://mongo_frontend:27017");
        database = mongoClient.getDatabase("CategoryDatabase");

        collection = database.getCollection("categoryData");
    }

    public ArrayList<String> getCategories() {
        try {
            if (collection.countDocuments() == 0) {
                ArrayList<String> defaultCategories = new ArrayList<String>(Arrays.asList(
                        "Books", "Business & Industrial", "Clothing, Shoes & Accessories",
                        "Collectibles", "Consumer Electronics", "Crafts", "Dolls & Bears",
                        "Home & Garden", "Motors", "Pet Supplies", "Sporting Goods", "Sports Mem, Cards & Fan Shop",
                        "Toys & Hobbies", "Antiques", "Computers/Tablets & Networking"));
                for (String category : defaultCategories){
                    try {
                        Document categoryObj = new Document();
                        categoryObj.append("categoryName", category);
                        collection.insertOne(categoryObj);
                        System.out.println("Insert " + category + " success");
                    } catch (MongoException e) {
                        System.out.println("Unable to insert due to an error: " + e);
                    }
                }
                return defaultCategories;
            }
            ArrayList<String> output = new ArrayList<String>();
            for (Object doc: collection.find()) {
                Document docu = (Document) doc;
                output.add(docu.get("categoryName").toString());
            }
            Collections.sort(output);
            return output;
        } catch (MongoException e) {
            System.out.println("Unable to get all categories due to an error: " + e);
            return null;
        }
    }

    public boolean addCategory(String category){
        try {
            Document categoryObj = new Document();
            categoryObj.append("categoryName", category);
            collection.insertOne(categoryObj);
            System.out.println("Insert success");
            return true;
        } catch (MongoException e) {
            System.out.println("Unable to get all categories due to an error: " + e);
            return false;
        }
    }

    public boolean deleteCategory(String category) {
        try {
            Document categoryObj = new Document();
            categoryObj.append("categoryName", category);
            collection.deleteOne(categoryObj);
            System.out.println("Deletion success");
            return true;
        } catch (MongoException e) {
            System.err.println("Unable to update due to an error: " + e);
            return false;
        }
    }

    public boolean updateCategory(String oldCategory, String newCategory){
        try {
            collection.updateOne(Filters.eq("categoryName", oldCategory), Updates.set("categoryName", newCategory));
            return true;
        } catch (MongoException e) {
            System.out.println("Unable to update due to an error: " + e);
            return false;
        }
    }
}
