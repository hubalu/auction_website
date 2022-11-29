package app.auction;

import app.item.Item;
import app.login.LoginController;
import app.rmiManagement.RMIHelper;
import app.rmiManagement.RemoteAuctionManagement;
import app.rmiManagement.RemoteItemManagement;
import app.util.Path;
import app.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

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
    private static List<AuctionDesc> cachedAuctions;

    public static Route uploadAuctionPlaceholder = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();

        String item_id = request.queryParams("item_id");
        model.put("selected", item_id);

        RemoteItemManagement rmItemManagement = rmiHelper.getRemItemManagement();
        List<Item> itemList = rmItemManagement.search_item(null, null, "UploadTime", true);
        model.put("userID", request.session().attribute("userID"));
        model.put("itemList", itemList);
        return ViewUtil.render(request, model, Path.Template.UPLOAD_AUCTION);
    };

    public static Route handleUploadAuctionPostPlaceholder = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemoteAuctionManagement rmAuctionManagement = rmiHelper.getRemAuctionManagement();

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
        String itemId = request.queryParams("item").split("@")[0];
        String itemName = request.queryParams("item").split("@")[1];
        Double startingPrice = Double.parseDouble(request.queryParams("start_price"));
        Double buyNowPrice;
        if (!request.queryParams("buy_now_price").isEmpty()){
            buyNowPrice = Double.parseDouble(request.queryParams("buy_now_price"));
        } else {
            buyNowPrice = null;
        }
        String startTime = request.queryParams("start_time");
        String expireTime = request.queryParams("end_time");
        String sellerId = request.session().attribute("userID").toString();

        rmAuctionManagement.listForAuction(itemId, itemName, startingPrice, buyNowPrice,
                startTime, expireTime, sellerId);
        response.redirect(Path.Web.ALL_AUCTIONS);
        return null;
    };

    public static Route getAllAuctionPlaceholder = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemoteAuctionManagement rmAuctionManagement = rmiHelper.getRemAuctionManagement();
        Map<String, Object> model = new HashMap<>();
        List<AuctionDesc> auctionList = rmAuctionManagement.getAuctions();
        cachedAuctions = auctionList;
        model.put("auctionList", auctionList);
        return ViewUtil.render(request, model, Path.Template.ALL_AUCTIONS);
    };

    public static Route getOneAuctionPlaceholder = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemoteAuctionManagement rmAuctionManagement = rmiHelper.getRemAuctionManagement();
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            if (cachedAuctions == null){
                cachedAuctions = rmAuctionManagement.getAuctions();;
            }
            String auctionId = request.params(":AuctionID");
            for (AuctionDesc auction : cachedAuctions){
                if(auction.getAuctionId().equals(auctionId)){
                    model.put("auction", auction);
                }
            }
            model.put("userID", request.session().attribute("userID"));
            return ViewUtil.render(request, model, Path.Template.ONE_AUCTION);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route submitBidPlaceholder = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemoteAuctionManagement rmAuctionManagement = rmiHelper.getRemAuctionManagement();
        if (clientAcceptsHtml(request)) {
            //Some RMI Function Call to Submit Bid
            String auctionId = request.params(":AuctionID");
            String userID = request.session().attribute("userID");
            Double bidPrice = Double.parseDouble(request.queryParams("bidPrice"));
            rmAuctionManagement.placeBid(auctionId, userID, bidPrice);
            HashMap<String, Object> model = new HashMap<>();
            ArrayList<AuctionDesc> auctions = generateItemList();
            model.put("auction", auctions);
            response.redirect(Path.Web.ALL_AUCTIONS);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    private static ArrayList<AuctionDesc> generateItemList(){

        List<Double> startPrice = Arrays.asList(20.0, 30.0, 40.0);
        List<Double> currentPrice = Arrays.asList(50.0, 60.0, 70.0);
        List<Double> buyNowPrice = Arrays.asList(80.0, 90.0, 100.0);
        String startTime = new Date().toString();
        long et = new Date().getTime() + (1000 * 60 * 60);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        int id = 0;
        ArrayList<AuctionDesc> itemList = new ArrayList<AuctionDesc>();
        String category = "Skincare";
        String itemName = "Sunscreen";
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                for (int k = 0; k < 3; k++){
                    itemList.add(new AuctionDesc(String.valueOf(i * 9 + j * 3 + k), String.valueOf(i * 9 + j * 3 + k), itemName, startPrice.get(i),
                            buyNowPrice.get(k), currentPrice.get(i), "1",
                            startTime, dateFormat.format(et), "6"));
                    et += (1000*60 * 60);
                }
            }
        }
        return itemList;
    }
}
