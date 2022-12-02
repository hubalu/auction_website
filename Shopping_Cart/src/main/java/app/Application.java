package app;

import app.cart.CartManagement;

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
