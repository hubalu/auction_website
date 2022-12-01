package app.auction;

import app.rmiManagement.RMIHelper;
import app.rmiManagement.RemoteAuctionManagement;
import app.rmiManagement.RemoteUserManagement;
import app.timertasks.Expiration;
import app.timertasks.OneDayAlert;
import app.timertasks.OneHourAlert;
import app.timertasks.StartTask;
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
import java.util.*;

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
			Connection conn;
			factory.setHost("rabbit-mq");
			conn = factory.newConnection();
			channel = conn.createChannel();
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);

			try {
				// get the address of this host.
				address = (InetAddress.getLocalHost()).toString();
//				address = "auction";
			} catch (Exception e) {
				throw new RemoteException("can't get inet address.");
			}
			port = 12345;  // our port
			System.out.println("using address=" + address + ",port=" + port);
			// create the registry and bind the name and object.
			registry = LocateRegistry.createRegistry(port);
			registry.rebind("auctionManagement", this);

		} catch (Exception e){
			e.printStackTrace();
		}


	}
	
	public boolean listForAuction(String itemId, String itemName, Double startingPrice,
					 Double buyNowPrice, String startTime, String expireTime, String sellerId) throws RemoteException{
		try {
			ObjectId objectId = this.db.insertToAuction(itemId, itemName, startingPrice, buyNowPrice, startTime, expireTime, sellerId);

			String time_to_end = expireTime.replace("T", " ");
			String time_to_start = startTime.replace("T", " ");
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date start_time = dateFormat.parse(time_to_start);
			System.out.println(start_time.toString());
			Date end_time = dateFormat.parse(time_to_end);
			timer.schedule(new StartTask(objectId.toString(), db), start_time);
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
	
	public boolean placeBid(String auctionId, String userId, double bidPrice) throws RemoteException{
		try {
			AuctionDesc auctionDesc = db.getAuctionDesc(new ObjectId(auctionId));
			this.db.placeBid(auctionId, userId, bidPrice);
			JsonObject jsonObj = new JsonObject();
			jsonObj.addProperty("type", "new bid");
			jsonObj.addProperty("auction_id", auctionId);
			String prev_bidder_id = auctionDesc.getBidderId();
			if(prev_bidder_id == null){
				return true;
			}
			jsonObj.addProperty("user_id", prev_bidder_id);
			List<String> user = new ArrayList<>();
			user.add(prev_bidder_id);
			List<String> email_list = rmiUser.getEmailList(user);
			jsonObj.addProperty("email", email_list.get(0));
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
	
	public boolean addToWatchlist(String auctionId, String userId) throws RemoteException{
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
