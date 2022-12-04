package app.timertasks;

import app.auction.AuctionManagement;
import app.auction.MongoDB;
import app.rmiManagement.RemotePaymentManagement;
import app.rmiManagement.RemoteUserManagement;
import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class Expiration extends TimerTask {

    String auctionId;
    MongoDB db;

    Channel channel;

    RemoteUserManagement rmiUser;
    RemotePaymentManagement rmiPayment;

    public Expiration(String auctionId, Channel channel, MongoDB db, RemoteUserManagement rmiUser, RemotePaymentManagement rmiPayment){
        this.auctionId = auctionId;
        this.db = db;
        this.channel = channel;
        this.rmiUser = rmiUser;
        this.rmiPayment = rmiPayment;
    }

    public void run(){
        db.changeActiveStatus(auctionId, false);
        String winner = db.getWinner(auctionId);
        if(winner == null){
            return;
        }
        try{
            rmiPayment.makePayment(winner, db.getPrice(auctionId));
            List<String> user_ids = new ArrayList<>();
            user_ids.add(winner);
            List<String> emails = rmiUser.getEmailList(user_ids);
            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("type", "auction wins");
            jsonObj.addProperty("auction_id", auctionId);
            jsonObj.addProperty("user_id", winner);
            jsonObj.addProperty("email", emails.get(0));
            channel.basicPublish("", AuctionManagement.QUEUE_NAME, null, jsonObj.toString().getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
