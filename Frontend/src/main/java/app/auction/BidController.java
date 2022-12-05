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

    public static Route addToWatchlist = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemoteAuctionManagement rmAuctionManagement = rmiHelper.getRemAuctionManagement();

        String auctionId = request.queryParams("auctionID");
        String userID = request.session().attribute("userID").toString();
        rmAuctionManagement.addToWatchlist(auctionId, userID);
        response.redirect(Path.Web.UPLOAD_AUCTION+auctionId);
        return null;
    };

    public static Route getAllAuctionPlaceholder = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemoteAuctionManagement rmAuctionManagement = rmiHelper.getRemAuctionManagement();
        Map<String, Object> model = new HashMap<>();
        boolean earliestExpirationFirst = true;
        if (request.queryParams("sort_order") != null) {
            earliestExpirationFirst = request.queryParams("sort_order").equals("earliest");
            if (!request.queryParams("sort_order").equals("earliest")){
                model.put("latest", true);
            }
        }
        List<AuctionDesc> auctionList = rmAuctionManagement.getAuctions(earliestExpirationFirst);
        cachedAuctions = auctionList;
        model.put("auctionList", auctionList);
        return ViewUtil.render(request, model, Path.Template.ALL_AUCTIONS);
    };

    public static Route getOneAuctionPlaceholder = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemoteAuctionManagement rmAuctionManagement = rmiHelper.getRemAuctionManagement();
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            cachedAuctions = rmAuctionManagement.getAuctions(true);
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
        System.out.println("fetching RM Auction Management in place bid");
        RemoteAuctionManagement rmAuctionManagement = rmiHelper.getRemAuctionManagement();
        if (clientAcceptsHtml(request)) {
            //Some RMI Function Call to Submit Bid
            String auctionId = request.queryParams("auctionID");
            System.out.println("auctionId");
            String userID = request.session().attribute("userID").toString();
            Double bidPrice = Double.parseDouble(request.queryParams("bidPrice"));
            rmAuctionManagement.placeBid(auctionId, userID, bidPrice);
            response.redirect(Path.Web.ALL_AUCTIONS);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route getAllPersonalAuction = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemoteAuctionManagement rmAuctionManagement = rmiHelper.getRemAuctionManagement();
        Map<String, Object> model = new HashMap<>();

        String userid = request.session().attribute("userID").toString();

        List<AuctionDesc> auctionList = rmAuctionManagement.getAuctionsByUser(userid);
        model.put("auctionList", auctionList);
        return ViewUtil.render(request, model, Path.Template.PERSONAL_AUCTIONS);
    };
}
