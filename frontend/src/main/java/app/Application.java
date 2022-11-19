package app;

import app.item.ItemContoller;
import app.login.*;
import app.user.*;
import app.util.*;

import java.security.Permission;

import static spark.Spark.*;
import static spark.debug.DebugScreen.*;

public class Application {


    public static UserDao userDao;

    public static void main(String[] args) {

        userDao = new UserDao();
        userDao.addUser();

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
        get(Path.Web.ITEM,           ItemContoller.uploadItem);
        post(Path.Web.ITEM,          ItemContoller.handleUploadItemPost);
        get("*",                     ViewUtil.notFound);
        get("/hello/",           (request, response) -> {return "hello world";});

        //Set up after-filters (called after each get/post)
        after("*",                   Filters.addGzipHeader);
    }
}