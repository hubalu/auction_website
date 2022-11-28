import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private Connection conn;

    public Database(String database_path) {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/", "root", "123qweASD-");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createDatabase() {
        String sql = "CREATE DATABASE IF NOT EXISTS Cart;";
        try (Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS Cart.Cart (\n"
                + "	id INT AUTO_INCREMENT PRIMARY KEY,\n"
                + "	user_id INT NOT NULL,\n"
                + "	item_id INT NOT NULL,\n"
                + "	item_name VARCHAR(255) NOT NULL,\n"
                + "	quantity INT NOT NULL,\n"
                + "	buy_now_price DOUBLE NOT NULL\n"
                + ");";
        try (Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertIntoTable(int user_id, int item_id, String item_name, int quantity, double buy_now_price) throws SQLException {
        String sql = "INSERT INTO Cart.Cart (user_id,item_id,item_name, quantity, buy_now_price) VALUES(?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, user_id);
        pstmt.setInt(2, item_id);
        pstmt.setString(3, item_name);
        pstmt.setDouble(4, quantity);
        pstmt.setDouble(5, buy_now_price);

        pstmt.executeUpdate();

    }

    public int checkIfItemExist(int user_id, int item_id) {
        String sql = String.format("SELECT quantity FROM Cart.Cart WHERE user_id = %s AND item_id = %s", user_id, item_id);
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int count = rs.getInt("quantity");
                if (count > 0) {
                    return count;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
        return 0;
    }

    public void updateCartByQuantity(int user_id, int item_id, int oldCount, int quantity) throws SQLException {
        String sql = String.format("UPDATE Cart.Cart SET quantity = %s WHERE user_id = %s and item_id = %s", oldCount+quantity, user_id, item_id);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();
    }

    public List<cartItem> getCartByUser(int user_id) {
        String sql = String.format("SELECT * FROM Cart.Cart WHERE user_id = %s", user_id);
        Statement stmt = null;
        List<cartItem> result = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                result.add(cartItem.createCart(rs));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return result;
        }

        return result;

    }
}
