package app.cart;

import app.login.LoginController;
import app.rmiManagement.RMIHelper;
import app.rmiManagement.RemoteAuctionManagement;
import app.rmiManagement.RemoteCartManagement;
import app.rmiManagement.RemotePaymentManagement;
import app.util.Path;
import app.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartController {

    public static RMIHelper rmiHelper = new RMIHelper();
    private static List<cartItem> cachedCartItems;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static Route addCart = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();
        RemoteCartManagement rmCartManagement = rmiHelper.getRemCartManagement();

        int userid = request.session().attribute("userID");
        String auctionId = request.queryParams("auctionId");
        String itemName = request.queryParams("itemName");
        Double buyNowPrice = Double. parseDouble(request.queryParams("buyNowPrice"));

        rmCartManagement.addCart(userid, auctionId, itemName, buyNowPrice);

        response.redirect(Path.Web.CART);
        return null;
    };

    public static Route getCart = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        try {
            RemoteCartManagement rmCartManagement = rmiHelper.getRemCartManagement();
            Map<String, Object> model = new HashMap<>();
            cachedCartItems = rmCartManagement.getCart(request.session().attribute("userID"));
            double totalCost = 0.0;
            for(cartItem item: cachedCartItems){
                totalCost += item.getBuy_now_price();
            }
            model.put("cartItems", cachedCartItems);
            model.put("totalCost", df.format(totalCost));
            System.out.println("everything in the cart");
            System.out.println(cachedCartItems);
            return ViewUtil.render(request, model, Path.Template.GET_CART);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return null;
        }
    };

    public static Route removeItem = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        try {
            RemoteCartManagement rmCartManagement = rmiHelper.getRemCartManagement();
            rmCartManagement.removeItem(request.queryParams("auctionID"));
            response.redirect(Path.Web.CART);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return null;
    };

    public static Route clearCart = (Request request, Response response) -> {
        try {
            LoginController.ensureUserIsLoggedIn(request, response);
            RemoteCartManagement rmCartManagement = rmiHelper.getRemCartManagement();
            rmCartManagement.clearCart(request.session().attribute("userID"));
            response.redirect(Path.Web.CART);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

        return null;
    };

    public static Route checkOut = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();
        RemotePaymentManagement rmPaymentManagement = rmiHelper.getRemPaymentManagement();
        RemoteAuctionManagement rmAuctionManagement = rmiHelper.getRemAuctionManagement();

        String userid = request.session().attribute("userID").toString();
        double totalCost = Double.parseDouble(request.queryParams("totalCost"));

        if (rmPaymentManagement.viewBalance(userid) < totalCost){
            model.put("cartItems", cachedCartItems);
            model.put("BalanceTooLow", true);
            model.put("totalCost", totalCost);
            return ViewUtil.render(request, model, Path.Template.GET_CART);
        } else {
            for (cartItem item : cachedCartItems) {
                rmAuctionManagement.endAuction(item.getAuction_id());
            }
            rmPaymentManagement.makePayment(userid, totalCost);
            RemoteCartManagement rmCartManagement = rmiHelper.getRemCartManagement();
            rmCartManagement.clearCart(request.session().attribute("userID"));
            response.redirect(Path.Web.CART);
            return null;
        }
    };
}
