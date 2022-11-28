import java.sql.SQLException;

public class CartManagement {

    private Database db;

    public CartManagement(String databaseName) {
        this.db = new Database(databaseName);
        this.db.createDatabase();
        this.db.createTableIfNotExists();
    }

    public boolean addCart(int user_id, int item_id, String item_name, int quantity, double buy_now_price){
        try {
            int count = this.db.checkIfItemExist(user_id, item_id);
            if (count > 0) {
                this.db.updateCartByQuantity(user_id, item_id, count, quantity);
            } else {
                this.db.insertIntoTable(user_id, item_id, item_name, quantity, buy_now_price);
            }
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean getCart(int user_id, String token) {
        return true;
    }
}
