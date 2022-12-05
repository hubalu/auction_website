package app.user;

import app.item.Item;
import app.login.LoginController;
import app.rmiManagement.RMIHelper;
import app.rmiManagement.RemoteItemManagement;
import app.rmiManagement.RemotePaymentManagement;
import app.rmiManagement.RemoteUserManagement;
import app.util.Path;
import app.util.ViewUtil;
import org.mindrot.jbcrypt.*;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static app.Application.userDao;

public class UserController {
    public static RMIHelper rmiHelper = new RMIHelper();

    // Authenticate the user by hashing the inputted password using the stored salt,
    // then comparing the generated hashed password to the stored hashed password
    public static boolean authenticate(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return false;
        }
        User user = userDao.getUserByUsername(username);
        if (user == null || user.getDeactivate()){
            return false;
        }
        String hashedPassword = BCrypt.hashpw(password, user.getSalt());
        return hashedPassword.equals(user.getHashedPassword());
    }

    public static Route updatePassword = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemoteItemManagement rmItemManagement = rmiHelper.getRemItemManagement();

        Map<String, Object> model = new HashMap<>();
        // database processing
        String username = request.queryParams("username");
        String password = request.queryParams("password");
        if ( userDao.updateUser(username, password)){
            model.put("passwordUpdateSuccess", true);
        } else {
            model.put("passwordUpdateFail", true);
        }
        response.redirect(Path.Web.USER_INFO);
        return null;
    };

    public static Route updateUserInfo = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemoteItemManagement rmItemManagement = rmiHelper.getRemItemManagement();

        Map<String, Object> model = new HashMap<>();
        // database processing
        int userid = Integer.parseInt(request.queryParams("userid"));
        String username = request.queryParams("username");
        String email = request.queryParams("email");
        String phoneNumber = request.queryParams("phoneNumber");

        RemoteUserManagement rmiUserManagement = rmiHelper.getRemUserManagement();
        boolean success = rmiUserManagement.updateUser(userid, username, email, phoneNumber);

        if (success){
            model.put("userInfoUpdateSuccess", true);
        } else {
            model.put("userInfoUpdateFail", true);
        }

        response.redirect(Path.Web.USER_INFO);
        return null;
    };

    public static Route deactivateUser = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();

        if(request.session().attribute("userRole").toString().equals("Admin")){
            // database processing
            RemoteUserManagement rmiUserManagement = rmiHelper.getRemUserManagement();
            int userid = Integer.parseInt(request.queryParams("userid"));
            String username = request.queryParams("username");
            boolean success = rmiUserManagement.suspendUser(userid, UserType.Admin);
            if (!success){
                response.redirect(Path.Web.USER_INFO);
                return null;
            }
            success = userDao.deactivateUser(username, true);
            if (!success){
                userDao.deactivateUser(username, false);
            }
            response.redirect(Path.Web.USER_INFO);
            return null;
        } else {
            return ViewUtil.render(request, model, Path.Template.UNAUTHORIZED);
        }
    };

    public static Route activateUser = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();

        if(request.session().attribute("userRole").toString().equals("Admin")){
            // database processing
            RemoteUserManagement rmiUserManagement = rmiHelper.getRemUserManagement();
            int userid = Integer.parseInt(request.queryParams("userid"));
            String username = request.queryParams("username");
            boolean success = rmiUserManagement.suspendUser(userid, UserType.Admin);
            if (!success){
                response.redirect(Path.Web.USER_INFO);
                return null;
            }

            success = userDao.deactivateUser(username, false);
            if (!success){
                userDao.deactivateUser(username, true);
            }
            response.redirect(Path.Web.USER_INFO);
            return null;
        } else {
            return ViewUtil.render(request, model, Path.Template.UNAUTHORIZED);
        }
    };

    public static Route deleteUser = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();

        if(request.session().attribute("userRole").toString().equals("Admin")){
            // database processing
            RemoteUserManagement rmiUserManagement = rmiHelper.getRemUserManagement();
            int userid = Integer.parseInt(request.queryParams("userid"));
            String username = request.queryParams("username");
            int operatorId = request.session().attribute("userID");

            boolean success = rmiUserManagement.deleteUser(userid, operatorId, UserType.Admin);
            if (!success){
                response.redirect(Path.Web.USER_INFO);
                return null;
            }

            userDao.deleteUser(username);
            response.redirect(Path.Web.USER_INFO);
            return null;
        } else {
            return ViewUtil.render(request, model, Path.Template.UNAUTHORIZED);
        }
    };

    public static Route getCreateUserPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, Path.Template.CREATE_USER);
    };

    public static Route getOneUserInfo = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemoteUserManagement rmiUserManagement = rmiHelper.getRemUserManagement();
        Map<String, Object> model = new HashMap<>();

        Integer userid = request.session().attribute("userID");
        UserType userType = request.session().attribute("userRole");
        String requestid = request.params(":userid");
        if (userType == UserType.Admin || userid.toString().equals(requestid)) {
            UserInfo userInfo = rmiUserManagement.getOneUser(Integer.parseInt(requestid));
            User user = userDao.getUserByUsername(userInfo.getUsername());
            model.put("userInfo", userInfo);
            model.put("user", user);
            model.put("admin", true);
            model.put("userList", user);
            return ViewUtil.render(request, model, Path.Template.ONE_USER);
        } else {
            return ViewUtil.render(request, model, Path.Template.UNAUTHORIZED);
        }
    };

    public static Route getUserInfo = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        RemoteUserManagement rmiUserManagement = rmiHelper.getRemUserManagement();
        Map<String, Object> model = new HashMap<>();
        System.out.println("User Type: " + request.session().attribute("userRole"));
        if(request.session().attribute("userRole") == UserType.User){
            UserInfo userInfo = rmiUserManagement.getOneUser(request.session().attribute("userID"));
            User user = userDao.getUserByUsername(request.session().attribute("currentUser"));
            model.put("userInfo", userInfo);
            model.put("user", user);
            return ViewUtil.render(request, model, Path.Template.ONE_USER);
        } else {
            List<UserInfo> userList = rmiUserManagement.getAllUser();
            model.put("userList", userList);
            return ViewUtil.render(request, model, Path.Template.ALL_USER);
        }
    };

    public static Route createUser = (Request request, Response response) -> {
        RemoteUserManagement rmiUserManagement = rmiHelper.getRemUserManagement();
        RemotePaymentManagement rmiPaymentManagement = rmiHelper.getRemPaymentManagement();

        // database processing
        String name = request.queryParams("name");
        String username = request.queryParams("username");
        String email = request.queryParams("email");
        String password = request.queryParams("password");
        String phone_number = request.queryParams("phone_number");

        Map<String, Object> model = new HashMap<>();

        int userId = rmiUserManagement.createUser(username, UserType.User, email, phone_number);
        if (userId == -1){
            model.put("userCreateFailed", true);
            return ViewUtil.render(request, model, Path.Template.CREATE_USER);
        }
        System.out.println(userId);

        rmiPaymentManagement.insertToBankBalance(String.format("%d", userId), 0.0);

        boolean success = userDao.addUser(username, password, UserType.User, userId);
        System.out.println(success);
        // RMI call User microservice
        if (success){
            return ViewUtil.render(request, model, Path.Template.LOGIN);
        } else {
            model.put("userCreateFailed", true);
            return ViewUtil.render(request, model, Path.Template.CREATE_USER);
        }

    };
}
