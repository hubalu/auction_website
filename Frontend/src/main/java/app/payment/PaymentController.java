package app.payment;

import app.item.Item;
import app.login.LoginController;
import app.rmiManagement.RMIHelper;
import app.rmiManagement.RemoteAuctionManagement;
import app.rmiManagement.RemoteItemManagement;
import app.rmiManagement.RemotePaymentManagement;
import app.util.Path;
import app.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import static app.util.RequestUtil.*;

// TODO EVERYTHING BELOW
public class PaymentController {
    public static RMIHelper rmiHelper = new RMIHelper();

    public static Route makePayment = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemotePaymentManagement rmPaymentManagement = rmiHelper.getRemPaymentManagement();

        double amount = Double.parseDouble(request.queryParams("amount"));
        String userID = request.session().attribute("userID").toString();
        rmPaymentManagement.makePayment(userID, amount);
        response.redirect(Path.Web.VIEW_BALANCE);
        return null;
    };

    public static Route insertBankBalance = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemotePaymentManagement rmPaymentManagement = rmiHelper.getRemPaymentManagement();

        double amount = Double.parseDouble(request.queryParams("amount"));
        String userID = request.session().attribute("userID").toString();
        rmPaymentManagement.insertToBankBalance(userID, amount);
        response.redirect(Path.Web.VIEW_BALANCE);
        return null;
    };
    
    public static Route getMakePaymentPlaceholder = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, Path.Template.MAKE_PAYMENT);
    };

    public static Route getInsertBankBalancePlaceholder = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, Path.Template.INSERT_BANK_BALANCE);
    };

    public static Route getViewBalancePlaceholder = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemotePaymentManagement rmPaymentManagement = rmiHelper.getRemPaymentManagement();

        Map<String, Object> model = new HashMap<>();

        String userID = request.session().attribute("userID").toString();

        // TODO uncomment line 67
        double balance = 123;
        // double balance = rmPaymentManagement.viewBalance(userID);

        model.put("balance", balance);
        return ViewUtil.render(request, model, Path.Template.VIEW_BALANCE);
    };
}
