package app.user;

import app.item.Item;
import app.login.LoginController;
import app.util.Path;
import app.util.ViewUtil;
import org.mindrot.jbcrypt.*;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static app.Application.userDao;

public class UserController {

    // Authenticate the user by hashing the inputted password using the stored salt,
    // then comparing the generated hashed password to the stored hashed password
    public static boolean authenticate(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return false;
        }
        User user = userDao.getUserByUsername(username);
        if (user == null){
            return false;
        }
        String hashedPassword = BCrypt.hashpw(password, user.getSalt());
        System.out.println(user);
        return hashedPassword.equals(user.getHashedPassword());
    }

    public static Route getCreateUserPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, Path.Template.CREATE_USER);
    };


    public static Route createUser = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);

        // database processing
        String name = request.queryParams("name");
        String username = request.queryParams("username");
        String email = request.queryParams("email");
        String password = request.queryParams("password");
        String phone_number = request.queryParams("phone_number");

        Map<String, Object> model = new HashMap<>();
        boolean success = userDao.addUser(username, password);
        // RMI call User microservice
        if (success){
            return ViewUtil.render(request, model, Path.Template.LOGIN);
        } else {
            model.put("userCreateFailed", true);
            return ViewUtil.render(request, model, Path.Template.CREATE_USER);
        }

        //rmItemManagement.upload_item("1", item_name, item_desc, category);


    };
}
