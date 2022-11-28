package app.login;

import app.user.User;
import app.user.UserController;
import app.util.Path;
import app.util.ViewUtil;
import spark.*;

import java.util.HashMap;
import java.util.Map;

import static app.Application.userDao;
import static app.util.RequestUtil.*;

public class LoginController {

    public static Route serveLoginPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("loggedOut", removeSessionAttrLoggedOut(request));
        model.put("loginRedirect", removeSessionAttrLoginRedirect(request));
        return ViewUtil.render(request, model, Path.Template.LOGIN);
    };

    public static Route handleLoginPost = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        if (!UserController.authenticate(getQueryUsername(request), getQueryPassword(request))) {
            model.put("authenticationFailed", true);
            return ViewUtil.render(request, model, Path.Template.LOGIN);
        }
        model.put("authenticationSucceeded", true);
        request.session().attribute("currentUser", getQueryUsername(request));
        User user = userDao.getUserByUsername(getQueryUsername(request));
        request.session().attribute("userID", user.getUserId());
        request.session().attribute("userRole", user.getUserType());
        if (getQueryLoginRedirect(request) != null) {
            response.redirect(getQueryLoginRedirect(request));
        }
        response.redirect(Path.Web.ITEMS);
        return null;
        //return ViewUtil.render(request, model, Path.Template.LOGIN);
    };

    public static Route handleLogoutPost = (Request request, Response response) -> {
        request.session().removeAttribute("currentUser");
        request.session().removeAttribute("userID");
        request.session().removeAttribute("userRole");
        request.session().attribute("loggedOut", true);
        response.redirect(Path.Web.LOGIN);
        return null;
    };

    // The origin of the request (request.pathInfo()) is saved in the session so
    // the user can be redirected back after login
    public static void ensureUserIsLoggedIn(Request request, Response response) {
        if (request.session().attribute("currentUser") == null) {
            request.session().attribute("loginRedirect", request.pathInfo());
            response.redirect(Path.Web.LOGIN);
        }
        Integer userid = (Integer)request.session().attribute("userID");
        System.out.printf("User Id in getUserInfo: %d\n", userid);
    };
}
