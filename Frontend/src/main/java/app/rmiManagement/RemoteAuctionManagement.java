package app.rmiManagement;

import app.auction.AuctionDesc;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteAuctionManagement extends Remote {
    boolean listForAuction(String itemId, String itemName, Double startingPrice,
                           Double buyNowPrice, String startTime, String expireTime, String sellerId) throws RemoteException;

    boolean placeBid(String auctionId, String userId, double bidPrice) throws RemoteException;
    List<AuctionDesc> getAuctions(boolean soonest_first) throws RemoteException;
    List<AuctionDesc> getAuctionsByUser(String userId) throws RemoteException;
    boolean addToWatchlist(String auctionId, String userId) throws RemoteException;

    void endAuction(String auctionId) throws RemoteException;

    boolean checkOnAuction(String itemId) throws RemoteException;
}
