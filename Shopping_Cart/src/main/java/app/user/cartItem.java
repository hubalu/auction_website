package app.user;

import lombok.Value;

import java.sql.ResultSet;
import java.sql.SQLException;

@Value
public class cartItem {
    int id;
    int user_id;
    int auction_id;
    String item_name;
    double buy_now_price;

    public cartItem(int id, int user_id, int auction_id, String item_name, double buy_now_price) {
        this.id = id;
        this.user_id = user_id;
        this.auction_id = auction_id;
        this.item_name = item_name;
        this.buy_now_price = buy_now_price;
    }

    public static cartItem createCart(ResultSet rs) throws SQLException {
        return new cartItem(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("auction_id"), rs.getString("item_name"), rs.getDouble("buy_now_price"));
    }
}
