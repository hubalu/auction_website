package app.timertasks;

import app.auction.AuctionManagement;
import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.TimerTask;

public class NewBidAlert extends TimerTask {
    String prev_bidder_id;
    String getPrev_bidder_email;

    String auction_id;

    Channel channel;
    public NewBidAlert(String auction_id, String prev_bidder_id, String prev_bidder_email, Channel channel){
        this.prev_bidder_id = prev_bidder_id;
        this.getPrev_bidder_email = prev_bidder_email;
        this.channel = channel;
        this.auction_id = auction_id;
    }
    public void run(){
        try {
            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("type", "new bid");
            jsonObj.addProperty("auction_id", auction_id);
            jsonObj.addProperty("user_id", prev_bidder_id);
            jsonObj.addProperty("email", getPrev_bidder_email);
            channel.basicPublish("", AuctionManagement.QUEUE_NAME, null, jsonObj.toString().getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
