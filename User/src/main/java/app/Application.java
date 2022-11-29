package app;

import app.user.UserManagement;

public class Application {
    public static void main(String args[]) {
        try {
            UserManagement service = new UserManagement("userData");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
