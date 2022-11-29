package app.auction;

import app.rmiManagement.RMIHelper;
import app.rmiManagement.RemoteAuctionManagement;
import app.rmiManagement.RemoteUserManagement;
import app.timertasks.Expiration;
import app.timertasks.OneDayAlert;
import app.timertasks.OneHourAlert;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

public class AuctionManagement extends java.rmi.server.UnicastRemoteObject implements RemoteAuctionManagement {

	private MongoDB db;

	public final static String QUEUE_NAME = "auction";

	private Channel channel;
	int port;
	String address;
	Registry registry;

	Timer timer;

	RemoteUserManagement rmiUser;

	public AuctionManagement() throws RemoteException{
		try {
			db = new MongoDB("auctionSite");
			timer = new Timer();
			rmiUser = new RMIHelper().getRemUserManagement();
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
			port = 23456;  // our port
			System.out.println("using address=" + address + ",port=" + port);
			// create the registry and bind the name and object.
			registry = LocateRegistry.createRegistry(port);
			registry.rebind("auctionManagemen", this);

		} catch (Exception e){
			e.printStackTrace();
		}


	}
	
	public boolean listForAuction(String itemId, String itemName, Double startingPrice,
					 Double buyNowPrice, String startTime, String expireTime, String sellerId) throws RemoteException{
		try {
			ObjectId objectId = this.db.insertToAuction(itemId, itemName, startingPrice, buyNowPrice, startTime, expireTime, sellerId);

			DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
			Date end_time = dateFormat.parse(expireTime);
			timer.schedule(new Expiration(objectId.toString(), db), end_time);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(end_time);
			int newHour = calendar.get(Calendar.HOUR) - 1;
			calendar.set(Calendar.HOUR, newHour);
			Date oneHourBefore = calendar.getTime();
			if(!oneHourBefore.before(Calendar.getInstance().getTime())){
				timer.schedule(new OneHourAlert(objectId.toString(), channel, db, rmiUser), oneHourBefore);
			}
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
			calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 1);
			Date oneDayBefore = calendar.getTime();
			if(!oneDayBefore.before(Calendar.getInstance().getTime())){
				timer.schedule(new OneDayAlert(objectId.toString(), channel, db, rmiUser), oneDayBefore);
			}
			System.out.println("Successfully added item " + itemId + " to auction");
			return true;
		} catch (Exception e){
			e.printStackTrace();
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
			e.printStackTrace();
			return false;
		}
	}
	
	public List<AuctionDesc> getAuctions() throws RemoteException{
		try {
			List<AuctionDesc> listOfAuctions = this.db.getAuctions();
			return listOfAuctions;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean endAuction(String auctionId) {
		try {
			this.db.endAuction(auctionId);
			System.out.println("Successfully ended auction on " + auctionId);
			return true;
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateAuction(String auctionId, String userId, String param, String newVal) {
		try {
			this.db.updateAuction(auctionId, userId, param, newVal);
			System.out.println("Succesfully updated auction item " + auctionId + " with " + param + " = " + newVal);
			return true;
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean addToWatchlist(String auctionId, String userId) {
		try {
			this.db.addToWatchlist(auctionId, userId);
			System.out.println("Succesfully added userId " + userId + " to watchlist_" + auctionId);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
}
