package app.cart;

import app.login.LoginController;
import app.rmiManagement.RMIHelper;
import app.rmiManagement.RemoteCartManagement;
import app.util.Path;
import app.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartController {

    public static RMIHelper rmiHelper = new RMIHelper();
    private static List<cartItem> cachedCartItems;

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
        RemoteCartManagement rmCartManagement = rmiHelper.getRemCartManagement();
        Map<String, Object> model = new HashMap<>();
        cachedCartItems = rmCartManagement.getCart(request.session().attribute("userID"));
        model.put("cartItems", cachedCartItems);
        System.out.println("everything in the cart");
        System.out.println(cachedCartItems);
        return ViewUtil.render(request, model, Path.Template.GET_CART);
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
}
