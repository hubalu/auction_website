package app.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.BSON;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.mindrot.jbcrypt.BCrypt;


import static com.mongodb.client.model.Filters.eq;

public class UserDao {

//    private final List<User> users = List.of(
//            //        Username    Salt for hash                    Hashed password (the password is "password" for all users)
//            new User("perwendel", "$2a$10$h.dl5J86rGH7I8bD9bZeZe", "$2a$10$h.dl5J86rGH7I8bD9bZeZeci0pDt0.VwFTGujlnEaZXPf/q7vM5wO"),
//    );
//
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection collection;
    private CodecRegistry pojoCodecRegistry;

    public UserDao(){
        mongoClient = new MongoClient("localhost", 27017);
        database = mongoClient.getDatabase("LoginService");
        //database.createCollection("userData");
        /*this.pojoCodecRegistry =
                fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        fromProviders(PojoCodecProvider.builder().automatic(true).build())); */

        collection = database.getCollection("userData");

    }


    public User getUserByUsername(String username) {
        try {
            Document userData = (Document) collection.find(eq("_id", username)).first();
            if (userData == null){
                return null;
            }
            String fetchedUsername = userData.get("_id").toString();
            String salt =  userData.get("salt").toString();
            String hashedPassword = userData.get("hashedPassword").toString();
            User user = new User(fetchedUsername, salt, hashedPassword );
            //System.out.println("Success! Inserted document id: " + result.getInsertedId());
            System.out.println("Insert success");
            return user;
        } catch (MongoException e) {
            System.err.println("Unable to insert due to an error: " + e);
            return null;
        }
        //return users.stream().filter(b -> b.getUsername().equals(username)).findFirst().orElse(null);
    }

    public boolean addUser(String username, String password){
        System.out.println("Building Object");
        Document user = createDBUserObject(username, password);
        System.out.println(user);
        //System.out.println(user.getUsername() + user.getSalt() + user.getHashedPassword());
        try {
            System.out.println("Trying to Write");
            collection.insertOne(user);
            //System.out.println("Success! Inserted document id: " + result.getInsertedId());
            System.out.println("Insert success");
            return true;
        } catch (MongoException e) {
            System.err.println("Unable to insert due to an error: " + e);
            return false;
        }
        /*
        // Check if username already exists
        DBObject query = BasicDBObjectBuilder.start().add("_id", username).get();
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()){
            System.out.println(cursor.next());
        }*/

        //users.add(new User("perwendel", "$2a$10$h.dl5J86rGH7I8bD9bZeZe", "$2a$10$h.dl5J86rGH7I8bD9bZeZeci0pDt0.VwFTGujlnEaZXPf/q7vM5wO"));
    }
    public void printCollection(){
        FindIterable<User> iterDoc = collection.find();
        Iterator it = iterDoc.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }
    public static Document createDBUserObject(String username, String password) {
        Document userObj = new Document();
        System.out.println("Ojbect Started");
        String newSalt = BCrypt.gensalt();
        System.out.println(newSalt);
        String newHashedPassword = BCrypt.hashpw(password, newSalt);
        System.out.println("Password Hashed");

        //User user = new User(username, newSalt, newHashedPassword);
        userObj.append("_id", username);
        userObj.append("salt", newSalt);
        userObj.append("hashedPassword", newHashedPassword);
        System.out.println("Data Appeneded");
        return userObj;
    }

}
