package app;

import app.user.CartManagement;

public class Application {
    public static void main(String args[]) {
        try {
            CartManagement service = new CartManagement();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
