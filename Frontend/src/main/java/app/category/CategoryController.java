package app.category;

import app.login.LoginController;

import app.rmiManagement.RMIHelper;
import app.rmiManagement.RemoteUserManagement;
import app.user.User;
import app.user.UserInfo;
import app.user.UserType;
import app.util.Path;
import app.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import app.rmiManagement.RemoteItemManagement;
import java.util.*;

import static app.Application.categoryDao;

public class CategoryController {

    public static RMIHelper rmiHelper = new RMIHelper();
    public static Route loadCategory = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();
        if(request.session().attribute("userRole") == UserType.Admin){
            ArrayList<String> categories = categoryDao.getCategories();
            model.put("categories", categories);
            return ViewUtil.render(request, model, Path.Template.CATEGORY);
        } else {
            return ViewUtil.render(request, model, Path.Template.UNAUTHORIZED);
        }
    };

    public static Route addCategory = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();
        // database processing
        if(request.session().attribute("userRole") == UserType.Admin) {
            String categoryName = request.queryParams("newCategory");
            boolean success = categoryDao.addCategory(categoryName);
            if (!success){
                model.put("AddCategoryFailed", true);
            } else{
                model.put("AddCategorySuccess", true);
            }
            response.redirect(Path.Web.CATEGORY);
            return null;
        } else {
            return ViewUtil.render(request, model, Path.Template.UNAUTHORIZED);
        }
    };

    public static Route updateCategory = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemoteItemManagement rmItemManagement = rmiHelper.getRemItemManagement();
        // database processing
        Map<String, Object> model = new HashMap<>();
        if(request.session().attribute("userRole") == UserType.Admin) {
            String oldCategory = request.queryParams("oldCategory");
            String newCategory = request.queryParams("newCategory");
            boolean success = categoryDao.updateCategory(oldCategory, newCategory);
            if (!success){
                model.put("UpdateCategoryFailed", true);
            } else{
                model.put("UpdateCategorySuccess", true);
                rmItemManagement.update_category(oldCategory, newCategory);
            }
            response.redirect(Path.Web.CATEGORY);
            return null;
        } else {

            return ViewUtil.render(request, model, Path.Template.UNAUTHORIZED);
        }
    };

    public static Route deleteCategory = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemoteItemManagement rmItemManagement = rmiHelper.getRemItemManagement();
        // database processing
        Map<String, Object> model = new HashMap<>();
        if(request.session().attribute("userRole") == UserType.Admin) {
            String categoryName = request.queryParams("categoryName");
            boolean success = categoryDao.deleteCategory(categoryName);
            if (!success){
                model.put("DeleteCategoryFailed", true);
            } else{
                model.put("DeleteCategorySuccess", true);
                rmItemManagement.delete_category(categoryName);
            }
            response.redirect(Path.Web.CATEGORY);
            return null;
        } else {
            return ViewUtil.render(request, model, Path.Template.UNAUTHORIZED);
        }
    };
}