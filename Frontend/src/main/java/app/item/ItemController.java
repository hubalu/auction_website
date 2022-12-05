package app.item;

import app.login.LoginController;
import app.rmiManagement.RMIHelper;
import app.rmiManagement.RemoteItemManagement;
import app.user.CategoryDao;
import app.user.UserType;
import app.util.Path;
import app.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.lang.reflect.Array;
import java.util.*;
import static app.util.RequestUtil.*;
import static spark.Spark.redirect;
import static app.Application.categoryDao;


public class ItemController {

    private static List<Item> cachedItems;
    public static RMIHelper rmiHelper = new RMIHelper();

    public static Route uploadItem = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();
        ArrayList<String> categories = categoryDao.getCategories();
        model.put("categories", categories);
        return ViewUtil.render(request, model, Path.Template.ITEM);
    };

    public static Route handleUploadItemPost = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemoteItemManagement rmItemManagement = rmiHelper.getRemItemManagement();

        // database processing
        String item_name = request.queryParams("item_name");
        String item_desc = request.queryParams("description");
        String category = request.queryParams("category");
        // RMI call Item microservice
        rmItemManagement.upload_item(request.session().attribute("userID").toString(), item_name, item_desc, category);
        Map<String, Object> model = new HashMap<>();
        ArrayList<Item> itemList = new ArrayList<>();
        response.redirect(Path.Web.ITEMS);
        return null;
    };

    public static Route getAllItemsPlaceholder = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemoteItemManagement rmItemManagement = rmiHelper.getRemItemManagement();
        ArrayList<String> categories = categoryDao.getCategories();


        String search_value = request.queryParams("search_value");
        String search_type = request.queryParams("search_type");
        System.out.println(search_type);
        String order = request.queryParams("order");
        boolean desc = true;
        if (order != null && order.equals("ASC")){
            desc = false;
        }
        List<Item> currentItems;
        if (search_type == null){
            cachedItems = rmItemManagement.search_item(null, null, "UploadTime", desc);
            currentItems = cachedItems;
        } else if (search_type.equals("category")) {
            currentItems = rmItemManagement.search_item(null, search_value, "UploadTime", desc);
        } else {
            currentItems = rmItemManagement.search_item(search_value, null, "UploadTime", desc);
        }
        Map<String, Object> model = new HashMap<>();
        model.put("itemList", currentItems);
        model.put("categories", categories);
        return ViewUtil.render(request, model, Path.Template.ITEMS);
    };

    public static Route flagItemPost = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemoteItemManagement rmItemManagement = rmiHelper.getRemItemManagement();
        String itemID = request.queryParams("item_id");
        boolean success = rmItemManagement.flag_item(itemID);
        System.out.println(success);
        response.redirect(Path.Web.ITEMS);
        return null;
    };

    public static Route getFlagedItems = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemoteItemManagement rmItemManagement = rmiHelper.getRemItemManagement();

        List<Item> flaggedItems = rmItemManagement.get_flag_items();
        Map<String, Object> model = new HashMap<>();
        model.put("flaggedItems", flaggedItems);

        return ViewUtil.render(request, model, Path.Template.FLAGGED_ITEMS);
    };

    public static Route postDeleteItems = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);

        RemoteItemManagement rmItemManagement = rmiHelper.getRemItemManagement();
        String itemID = request.queryParams("item_id");

        boolean success = rmItemManagement.remove_item(itemID);
        if (!success) {
            Map<String, Object> model = new HashMap<>();
            model.put("activeAuction", true);

            if (cachedItems == null){
                cachedItems = rmItemManagement.search_item(null, null, "UploadTime", true);
            }
            for (Item item : cachedItems){
                if(item.getItemID().equals(itemID)){
                    model.put("item", item);
                }
            }
            model.put("userID", request.session().attribute("userID"));
            return ViewUtil.render(request, model, Path.Template.ONE_ITEM);
        } else if(request.session().attribute("userRole").toString().equals("Admin")) {
            response.redirect(Path.Web.FLAG_ITEM);
            return null;
        } else {
            response.redirect(Path.Web.ITEMS);
            return null;
        }
    };

    public static Route getOneItemPlaceholder = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemoteItemManagement rmItemManagement = rmiHelper.getRemItemManagement();
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            String itemId = request.params(":ItemID");
            if (cachedItems == null){
                cachedItems = rmItemManagement.search_item(null, null, "UploadTime", true);
            }
            for (Item item : cachedItems){
                if(item.getItemID().equals(itemId)){
                    model.put("item", item);
                }
            }
            model.put("userID", request.session().attribute("userID"));
            return ViewUtil.render(request, model, Path.Template.ONE_ITEM);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

//    public static ArrayList<Item> generateItemList(){
//        ArrayList<Item> itemList = new ArrayList<Item>();
//        List<String> name = Arrays.asList("Hello", "World", "Sunscreen", "Mouse", "Keyboard");
//        int id = 0;
//        String category = "Skincare";
//        List<String> description = Arrays.asList("A bottle of Sunscreen", "A mechanical keyboard", "A good mouse", "Hello", "World");
//        for (int i = 0; i < 5; i++){
//            for (int j = 0; j < 5; j++){
//                itemList.add(new Item(Integer.toString(i * 5 + j), name.get(i), description.get(j),category));
//            }
//        }
//
//        return itemList;
//    }
}
