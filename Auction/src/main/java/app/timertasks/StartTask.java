package app.timertasks;

import app.auction.MongoDB;

import java.util.TimerTask;

public class StartTask extends TimerTask {

    String auctionId;
    MongoDB db;

    public StartTask(String auctionId, MongoDB db){
        this.auctionId = auctionId;
        this.db = db;
    }

    public void run(){
        System.out.println("auction start!: " + auctionId);
        db.changeActiveStatus(auctionId, true);
    }
}