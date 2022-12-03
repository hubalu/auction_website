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

    public static Route getCart = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemoteCartManagement rmCartManagement = rmiHelper.getRemCartManagement();
        Map<String, Object> model = new HashMap<>();
        cachedCartItems = rmCartManagement.getCart(request.session().attribute("userID"));
        model.put("cartItems", cachedCartItems);
        return ViewUtil.render(request, model, Path.Template.GET_CART);
    };


    public static Route clearCart = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemoteCartManagement rmCartManagement = rmiHelper.getRemCartManagement();
        rmCartManagement.clearCart(request.session().attribute("userID"));
        response.redirect(Path.Web.CART);
        return null;
    };
}
