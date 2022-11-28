package app.rmiManagement;

import app.auction.AuctionDesc;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteAuctionManagement extends Remote {
    boolean listForAuction(String itemId, String itemName, Double startingPrice,
                           Double buyNowPrice, String startTime, String expireTime, String sellerId) throws RemoteException;

    boolean placeBid(String auctionId, String userId, double bidPrice, String prev_bidder_email) throws RemoteException;
    List<AuctionDesc> getAuctions() throws RemoteException;
}
