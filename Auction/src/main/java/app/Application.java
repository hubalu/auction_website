package app;

import app.auction.AuctionManagement;

public class Application {
    public static void main(String args[]) {
        try {
            AuctionManagement auctionManagement = new AuctionManagement();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}