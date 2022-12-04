package app;

import app.payment.PaymentManagement;

public class Application {
    public static void main(String[] args) {
        try {
            PaymentManagement paymentManagement = new PaymentManagement();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}