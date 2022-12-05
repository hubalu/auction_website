package app.user;

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
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
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
        mongoClient = MongoClients.create("mongodb://mongo_frontend:27017");
        database = mongoClient.getDatabase("LoginService");

        collection = database.getCollection("userData");

    }

    public User getUserByUsername(String username) {
        try {
            Document userData = (Document) collection.find(eq("_id", username)).first();
            if (userData == null){
                return null;
            }
            System.out.println(userData);
            String fetchedUsername = userData.get("_id").toString();
            String salt =  userData.get("salt").toString();
            String hashedPassword = userData.get("hashedPassword").toString();
            UserType userType = UserType.valueOf(userData.get("userType").toString());
            boolean deactivate = userData.get("deactivated") != null;
            int userID = (int) userData.get("userId");
            System.out.println(userID);
            if (userID < 0){
                return null;
            }
            User user = new User(fetchedUsername, salt, hashedPassword, userID, userType, deactivate);
            return user;
        } catch (MongoException e) {
            System.err.println("Unable to insert due to an error: " + e);
            return null;
        }
    }

    public boolean addUser(String username, String password, UserType userType, int userID){
        if (userID < 0) {
            return false;
        }
        Document user = createDBUserObject(username, password, userType, userID);
        System.out.println(user);

        try {
            collection.insertOne(user);
            System.out.println("Insert success");
            return true;
        } catch (MongoException e) {
            System.err.println("Unable to insert due to an error: " + e);
            return false;
        }
    }

    public boolean updateUser(String username, String newPassword) {
        User user = getUserByUsername(username);

        try {
            String newSalt = BCrypt.gensalt();
            String newHashedPassword = BCrypt.hashpw(newPassword, newSalt);

            collection.updateOne(Filters.eq("_id", username), Updates.set("salt", newSalt));
            collection.updateOne(Filters.eq("_id", username), Updates.set("hashedPassword", newHashedPassword));
            return true;
        } catch (MongoException e) {
            System.err.println("Unable to update due to an error: " + e);
            return false;
        }
    }

    public boolean deactivateUser(String username, boolean deactivate){
        try {
            if (deactivate) {
                collection.updateOne(Filters.eq("_id", username), Updates.set("deactivated", true));
            } else{
                collection.updateOne(Filters.eq("_id", username), Updates.unset("deactivated"));
            }
            return true;
        } catch (MongoException e) {
            System.err.println("Unable to update due to an error: " + e);
            return false;
        }
    }

    public boolean deleteUser(String username){
        try {
            collection.deleteOne(new Document("_id", username));
            return true;
        } catch (MongoException e) {
            System.err.println("Unable to update due to an error: " + e);
            return false;
        }
    }

    public void printCollection(){
        FindIterable<User> iterDoc = collection.find();
        Iterator it = iterDoc.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }
    public static Document createDBUserObject(String username, String password, UserType userType, int userID) {
        Document userObj = new Document();
        String newSalt = BCrypt.gensalt();
        String newHashedPassword = BCrypt.hashpw(password, newSalt);

        userObj.append("_id", username);
        userObj.append("salt", newSalt);
        userObj.append("hashedPassword", newHashedPassword);
        userObj.append("userType", userType.toString());
        userObj.append("userId", userID);

        return userObj;
    }

}
