package app.item;

import app.login.LoginController;
import app.rmiManagement.RMIHelper;
import app.rmiManagement.RemoteItemManagement;
import app.util.Path;
import app.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;


public class ItemContoller {

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
        return item_name + "/" + item_desc + "/" + category;
    };
}
