package app;

import app.rmiManagement.ItemManagement;

public class Application {
    public static void main(String args[]) {
        try {
            ItemManagement service = new ItemManagement();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}