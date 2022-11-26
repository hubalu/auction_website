package app.bidding;

import app.item.Item;
import app.login.LoginController;
import app.rmiManagement.RMIHelper;
import app.rmiManagement.RemoteItemManagement;
import app.util.Path;
import app.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;
import app.item.ItemController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import static app.util.RequestUtil.*;


public class BidController {

//    private Remote registryLookUp(){
//        Remote itemManagement = null;
//        Registry registry;
//        try{
//            String serverAddress=(InetAddress.getLocalHost()).toString();
//            String serverPort="12345";
//            // get the registry
//            registry=LocateRegistry.getRegistry(
//                    serverAddress,
//                    (new Integer(serverPort)).intValue()
//            );
//            // look up the remote object in the RMI Registry
//            itemManagement= (registry.lookup("rmiServer"));
//            // call the remote method
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//        return itemManagement;
//    }

    public static RMIHelper rmiHelper = new RMIHelper();

    public static Route uploadAuctionPlaceholder = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();

//        ArrayList<Item> itemList = ItemController.generateItemList();
//        model.put("itemList", itemList);
        return ViewUtil.render(request, model, Path.Template.UPLOAD_AUCTION);
    };

    public static Route handleUploadAuctionPostPlaceholder = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        //RemoteItemManagement rmItemManagement = rmiHelper.getRemItemManagement();

        // database processing
        //String item_name = request.queryParams("item_name");
        //String item_desc = request.queryParams("description");
        //String category = request.queryParams("category");
        // RMI call Item microservice
        //rmItemManagement.upload_item("1", item_name, item_desc, category);
        /*


        String startTime = new Date().toString();
        long et = new Date().getTime() + (1000 * 60 * 60);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String endTime = dateFormat.format(et);

        auctionList.add(new Auction(40, 100, 110, 150, startTime, endTime)); */
        String n = request.queryParams("item");
        String a = request.queryParams("start_price");
        String b = request.queryParams("buy_now_price");
        String c = request.queryParams("start_time");
        String d = request.queryParams("end_time");

        //return n + " / " + a + " / " + b + " / " + c + " / " + d;
        Map<String, Object> model = new HashMap<>();
        ArrayList<Auction> auctionList = generateItemList();
        model.put("auctionList", auctionList);
        return ViewUtil.render(request, model, Path.Template.ALL_AUCTIONS);
    };

    public static Route getAllAuctionPlaceholder = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();
        ArrayList<Auction> auctionList = generateItemList();
        model.put("auctionList", auctionList);
        return ViewUtil.render(request, model, Path.Template.ALL_AUCTIONS);
    };

    public static Route getOneAuctionPlaceholder = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            String startTime = new Date().toString();
            long et = new Date().getTime() + (1000 * 60 * 60);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String endTime = dateFormat.format(et);
            Auction auction = new Auction(40, "Sunscreen", 30, 60, 70, 100, startTime, endTime );
            model.put("auction", auction);
//            Item item = new Item("4399", "Sunscreen", "A bottle of new Sunscreen", "Suncare");
//            model.put("item", item);
            return ViewUtil.render(request, model, Path.Template.ONE_AUCTION);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route submitBidPlaceholder = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            //Some RMI Function Call to Submit Bid
            HashMap<String, Object> model = new HashMap<>();
            ArrayList<Auction> auctions = generateItemList();
            model.put("auction", auctions);
            return ViewUtil.render(request, model, Path.Template.ALL_AUCTIONS);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    private static ArrayList<Auction> generateItemList(){

        List<Integer> startPrice = Arrays.asList(20, 30, 40);
        List<Integer> currentPrice = Arrays.asList(50, 60, 70);
        List<Integer> buyNowPrice = Arrays.asList(80, 90, 100);
        String startTime = new Date().toString();
        long et = new Date().getTime() + (1000 * 60 * 60);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        int id = 0;
        ArrayList<Auction> itemList = new ArrayList<Auction>();
        String category = "Skincare";
        String itemName = "Sunscreen";
        List<String> description = Arrays.asList("A bottle of Sunscreen", "A mechanical keyboard", "A good mouse", "Hello", "World");
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                for (int k = 0; k < 3; k++){
                    itemList.add(new Auction(i * 9 + j * 3 + k, itemName, i * 9 + j * 3 + k, startPrice.get(i), currentPrice.get(j),buyNowPrice.get(k),
                            startTime, dateFormat.format(et)));
                    et += (1000*60 * 60);
                }
            }
        }

        return itemList;
    }
}
