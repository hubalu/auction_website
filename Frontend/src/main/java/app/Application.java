package app;

import app.bidding.BidController;
import app.item.ItemController;
import app.login.LoginController;
import app.user.UserController;
import app.user.UserDao;
import app.util.Filters;
import app.util.Path;
import app.util.ViewUtil;

import static spark.Spark.*;
import static spark.debug.DebugScreen.*;

public class Application {

    public static UserDao userDao;

    public static void main(String[] args) {

        userDao = new UserDao();

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
        get(Path.Web.ITEM,           ItemController.uploadItem);
        post(Path.Web.ITEM,          ItemController.handleUploadItemPost);
        get(Path.Web.ITEMS,          ItemController.getAllItemsPlaceholder);
        get(Path.Web.ONE_ITEM,       ItemController.getOneItemPlaceholder);

        get(Path.Web.CREATE_USER,       UserController.getCreateUserPage);
        post(Path.Web.CREATE_USER,      UserController.createUser);

        get(Path.Web.UPLOAD_AUCTION,    BidController.uploadAuctionPlaceholder);
        post(Path.Web.UPLOAD_AUCTION,   BidController.handleUploadAuctionPostPlaceholder);
        get(Path.Web.ALL_AUCTIONS,      BidController.getAllAuctionPlaceholder);
        get(Path.Web.ONE_AUCTION,       BidController.getOneAuctionPlaceholder);

        get("*",                     ViewUtil.notFound);

        //Set up after-filters (called after each get/post)
        after("*",                   Filters.addGzipHeader);
    }
}