import java.sql.SQLException;
import java.util.List;

public class CartManagement {

    private Database db;

    public CartManagement(String databaseName) {
        this.db = new Database(databaseName);
        this.db.createDatabase();
        this.db.createTableIfNotExists();
    }

    public boolean addCart(int user_id, int auction_id, String item_name, double buy_now_price){
        try {
            boolean exist = this.db.checkIfItemExist(user_id, auction_id);
            if (!exist) {   //prevent repeated adding to cart
                this.db.insertIntoTable(user_id, auction_id, item_name, buy_now_price);
            }
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public List<cartItem> getCart(int user_id) {
        try {
            return this.db.getCartByUser(user_id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public boolean clearCart(int user_id){
        try {
            List<cartItem> items = this.db.getCartByUser(user_id);
            for (cartItem item : items) {
                this.db.clearItemCart(item.getAuction_id());
            }
            //this.db.clearUserCart(user_id);
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
}
