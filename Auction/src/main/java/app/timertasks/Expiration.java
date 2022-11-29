package app.timertasks;

import app.auction.MongoDB;

import java.util.TimerTask;

public class Expiration extends TimerTask {

    String auctionId;
    MongoDB db;

    public Expiration(String auctionId, MongoDB db){
        this.auctionId = auctionId;
        this.db = db;
    }

    public void run(){
        db.setInActive(auctionId);
    }
}
