package app.timertasks;

import app.auction.AuctionManagement;
import app.auction.MongoDB;
import app.rmiManagement.RemoteUserManagement;
import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.TimerTask;

public class OneDayAlert extends TimerTask {

    String auction_id;
    Channel channel;
    MongoDB db;
    RemoteUserManagement rmiUser;

    public OneDayAlert(String auctionId, Channel channel, MongoDB db, RemoteUserManagement rmiUser){
        this.auction_id = auctionId;
        this.channel = channel;
        this.db = db;
        this.rmiUser = rmiUser;
    }

    public void run(){
        try {
            List<String> user_ids = db.getWatchList(auction_id);
            List<String> emails = rmiUser.getEmailList(user_ids);
            for(int i = 0; i < user_ids.size(); i++){
                JsonObject jsonObj = new JsonObject();
                jsonObj.addProperty("type", "alert one day");
                jsonObj.addProperty("auction_id", auction_id);
                jsonObj.addProperty("user_id", user_ids.get(i));
                jsonObj.addProperty("email", emails.get(i));
                channel.basicPublish("", AuctionManagement.QUEUE_NAME, null, jsonObj.toString().getBytes(StandardCharsets.UTF_8));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
