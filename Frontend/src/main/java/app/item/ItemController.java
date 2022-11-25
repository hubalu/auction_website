package app.item;

import app.login.LoginController;
import app.rmiManagement.RMIHelper;
import app.rmiManagement.RemoteItemManagement;
import app.util.Path;
import app.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.lang.reflect.Array;
import java.util.*;
import static app.util.RequestUtil.*;
import static spark.Spark.redirect;


public class ItemController {

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

    private static List<Item> cachedItems;
    public static RMIHelper rmiHelper = new RMIHelper();
    public static Route uploadItem = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();
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
        rmItemManagement.upload_item("1", item_name, item_desc, category);
        Map<String, Object> model = new HashMap<>();
        ArrayList<Item> itemList = new ArrayList<>();
//        itemList.add(new Item("30", "sdf", "sdf", "Sdf"));
//        model.put("itemList", itemList);
//        return ViewUtil.render(request, model, Path.Template.ITEMS);
        response.redirect(Path.Web.ITEMS);
        return null;
    };

    public static Route getAllItemsPlaceholder = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemoteItemManagement rmItemManagement = rmiHelper.getRemItemManagement();
        cachedItems = rmItemManagement.search_item(null, null, "UploadTime", true);
        Map<String, Object> model = new HashMap<>();
//        ArrayList<Item> itemList = generateItemList();
        model.put("itemList", cachedItems);
        return ViewUtil.render(request, model, Path.Template.ITEMS);
    };

    public static Route getOneItemPlaceholder = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            String itemId = request.params(":ItemID");
            for (Item item : cachedItems){
                if(item.getItemID().equals(itemId)){
                    model.put("item", item);
                }
            }
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
