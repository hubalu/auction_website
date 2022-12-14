package app;

import app.auction.BidController;
import app.cart.CartController;
import app.category.CategoryController;
import app.item.ItemController;
import app.login.LoginController;
import app.payment.PaymentController;
import app.user.CategoryDao;
import app.user.UserController;
import app.user.UserDao;
import app.util.Filters;
import app.util.Path;
import app.util.ViewUtil;

import static spark.Spark.*;
import static spark.debug.DebugScreen.*;

public class Application {

    public static UserDao userDao;
    public static CategoryDao categoryDao;

    public static void main(String[] args) {

        userDao = new UserDao();
        categoryDao = new CategoryDao();

        // Configure Spark
        port(4567);
        staticFiles.location("/public");
        staticFiles.expireTime(600L);
        enableDebugScreen();

        // Set up before-filters (called before each get/post)
        before("*",                  Filters.addTrailingSlashes);
        before("*",                  Filters.handleLocaleChange);

        get(Path.Web.LOGIN,          LoginController.serveLoginPage);
        post(Path.Web.LOGIN,         LoginController.handleLoginPost);
        post(Path.Web.LOGOUT,        LoginController.handleLogoutPost);

        get(Path.Web.ITEM,              ItemController.uploadItem);
        post(Path.Web.ITEM,             ItemController.handleUploadItemPost);
        get(Path.Web.ITEMS,             ItemController.getAllItemsPlaceholder);
        get(Path.Web.ONE_ITEM,          ItemController.getOneItemPlaceholder);
        post(Path.Web.FLAG_ITEM,        ItemController.flagItemPost);
        get(Path.Web.FLAG_ITEM,         ItemController.getFlagedItems);
        post(Path.Web.DELETE_ITEM,      ItemController.postDeleteItems);

        get(Path.Web.ONE_USER_INFO,     UserController.getOneUserInfo);
        get(Path.Web.USER_INFO,         UserController.getUserInfo);
        post(Path.Web.UPDATE_PASSWORD,  UserController.updatePassword);
        post(Path.Web.UPDATE_USER_INFO, UserController.updateUserInfo);

        get(Path.Web.CREATE_USER,       UserController.getCreateUserPage);
        post(Path.Web.CREATE_USER,      UserController.createUser);
        post(Path.Web.SUSPEND_USER,      UserController.deactivateUser);
        post(Path.Web.UNSUSPEND_USER,    UserController.activateUser);
        post(Path.Web.DELETE_USER,      UserController.deleteUser);

        get(Path.Web.UPLOAD_AUCTION,    BidController.uploadAuctionPlaceholder);
        post(Path.Web.UPLOAD_AUCTION,   BidController.handleUploadAuctionPostPlaceholder);
        get(Path.Web.ALL_AUCTIONS,      BidController.getAllAuctionPlaceholder);
        get(Path.Web.ONE_AUCTION,       BidController.getOneAuctionPlaceholder);
        post(Path.Web.UPLOAD_BID,       BidController.submitBidPlaceholder);
        post(Path.Web.ADD_TO_WATCHLIST, BidController.addToWatchlist);
        get(Path.Web.PERSONAL_AUCTIONS,      BidController.getAllPersonalAuction);

        get(Path.Web.MAKE_PAYMENT,       PaymentController.makePayment);
        get(Path.Web.INSERT_BANK_BALANCE, PaymentController.insertBankBalance);
        get(Path.Web.BALANCE,               PaymentController.getBankBalance);
        post(Path.Web.MAKE_PAYMENT,       PaymentController.makePayment);
        post(Path.Web.INSERT_BANK_BALANCE, PaymentController.insertBankBalance);
        post(Path.Web.BALANCE,              PaymentController.insertBankBalance);
        post(Path.Web.CHECKOUT,              CartController.checkOut);

        get(Path.Web.CATEGORY,          CategoryController.loadCategory);
        post(Path.Web.CATEGORY,         CategoryController.addCategory);
        post(Path.Web.DELETE_CATEGORY,         CategoryController.deleteCategory);
        post(Path.Web.UPDATE_CATEGORY,          CategoryController.updateCategory);

        get(Path.Web.CART, CartController.getCart);
        post(Path.Web.ADD_CART, CartController.addCart);
        post(Path.Web.CLEAR_CART, CartController.clearCart);
        post(Path.Web.REMOVE_CART, CartController.removeItem);

        get("*",                     ViewUtil.notFound);

        //Set up after-filters (called after each get/post)
        after("*",                   Filters.addGzipHeader);
    }
}