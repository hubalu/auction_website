import lombok.Value;

import java.sql.ResultSet;
import java.sql.SQLException;

@Value
public class cartItem {
    int id;
    int user_id;
    int item_id;
    String item_name;
    int quantity;
    double buy_now_price;

    public cartItem(int id, int user_id, int item_id, String item_name, int quantity, double buy_now_price) {
        this.id = id;
        this.user_id = user_id;
        this.item_id = item_id;
        this.item_name = item_name;
        this.quantity = quantity;
        this.buy_now_price = buy_now_price;
    }

    public static cartItem createCart(ResultSet rs) throws SQLException {
        return new cartItem(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("item_id"), rs.getString("item_name"), rs.getInt("quantity"), rs.getDouble("buy_now_price"));
    }
}
