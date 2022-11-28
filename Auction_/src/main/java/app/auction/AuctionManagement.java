package app.auction;

import app.rmiManagement.RemoteAuctionManagement;
import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.bson.types.ObjectId;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Timer;

public class AuctionManagement extends java.rmi.server.UnicastRemoteObject implements RemoteAuctionManagement {

	private MongoDB db;

	public final static String QUEUE_NAME = "app/auction";

	private Channel channel;
	int port;
	String address;
	Registry registry;

	public AuctionManagement() throws RemoteException{
		try {
			db = new MongoDB("");
			Timer timer = new Timer();
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection conn = factory.newConnection();
			channel = conn.createChannel();
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);

			try {
				// get the address of this host.
				address = (InetAddress.getLocalHost()).toString();
			} catch (Exception e) {
				throw new RemoteException("can't get inet address.");
			}
			port = 55555;  // our port
			System.out.println("using address=" + address + ",port=" + port);
			// create the registry and bind the name and object.
			registry = LocateRegistry.createRegistry(port);
			registry.rebind("itemManagement", this);


		} catch (Exception e){
			System.out.println("error in auction.AuctionManagement");
		}


	}
	
	public boolean listForAuction(String auctionId, String itemId, String itemName, Double startingPrice,
					 Double buyNowPrice, String startTime, String expireTime, String sellerId) throws RemoteException{
		try {
			this.db.insertToAuction(auctionId, itemId, itemName, startingPrice, buyNowPrice, startTime, expireTime, sellerId);
			System.out.println("Successfully added item " + itemId + " to auction");
			return true;
		} catch (Exception e){
			System.out.println("error in listForAuction"); 
			return false;
		}
	}
	
	public boolean placeBid(String auctionId, String userId, double bidPrice, String prev_bidder_email) throws RemoteException{
		try {
			AuctionDesc auctionDesc = db.getAuctionDesc(new ObjectId(auctionId));
			this.db.placeBid(auctionId, userId, bidPrice);
			JsonObject jsonObj = new JsonObject();
			jsonObj.addProperty("type", "new bid");
			jsonObj.addProperty("auction_id", auctionId);
			String prev_bidder_id = auctionDesc.getBidderId();
			if(prev_bidder_id == null || prev_bidder_email == null){
				return true;
			}
			jsonObj.addProperty("user_id", prev_bidder_id);
			jsonObj.addProperty("email", prev_bidder_email);
			channel.basicPublish("", AuctionManagement.QUEUE_NAME, null, jsonObj.toString().getBytes(StandardCharsets.UTF_8));
			System.out.println("Successfully placed bid on " + auctionId);
			return true;

		} catch (Exception e){
			System.out.println("error in placeBid"); 
			return false;
		}
	}
	
	public List<AuctionDesc> getAuctions() throws RemoteException{
		try {
			List<AuctionDesc> listOfAuctions = this.db.getAuctions();
			return listOfAuctions;
		} catch (Exception e){
			System.out.println("error in getAuctions"); 
			return null;
		}
	}
	
	public boolean endAuction(String auctionId) {
		try {
			this.db.endAuction(auctionId);
			System.out.println("Successfully ended auction on " + auctionId);
			return true;
		} catch (Exception e){
			System.out.println("error in endAuction"); 
			return false;
		}
	}
	
	public boolean updateAuction(String auctionId, String userId, String param, String newVal) {
		try {
			this.db.updateAuction(auctionId, userId, param, newVal);
			System.out.println("Succesfully updated auction item " + auctionId + " with " + param + " = " + newVal);
			return true;
		} catch (Exception e){
			System.out.println(e);
			System.out.println("error in updateAuction"); 
			return false;
		}
	}
	
	public boolean addToWatchlist(String itemId, String userId) {
		try {
			this.db.addToWatchlist(itemId, userId);
			System.out.println("Succesfully added userId " + userId + " to watchlist_" + itemId);
			return true;
		}catch (Exception e){
			System.out.println(e);
			System.out.println("error in addToWatchlist"); 
			return false;
		}
	}
	
}
