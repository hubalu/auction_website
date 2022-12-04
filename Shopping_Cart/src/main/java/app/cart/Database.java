package app.cart;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private Connection conn;

    public Database() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://mysql-db/", "root", "secretpass");
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
                + "	auction_id VARCHAR(3000) NOT NULL,\n"
                + "	item_name VARCHAR(255) NOT NULL,\n"
                + "	buy_now_price DOUBLE NOT NULL\n"
                + ");";
        try (Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertIntoTable(int user_id, String auction_id, String item_name, double buy_now_price) throws SQLException {
        System.out.println("let's see auction id");
        System.out.println(auction_id);
        String sql = "INSERT INTO Cart.Cart (user_id, auction_id, item_name, buy_now_price) VALUES(?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, user_id);
        pstmt.setString(2, auction_id);
        pstmt.setString(3, item_name);
        pstmt.setDouble(4, buy_now_price);

        pstmt.executeUpdate();

    }

    public boolean checkIfItemExist(int user_id, String auction_id) {
        String sql = String.format("SELECT count(*) FROM Cart.Cart WHERE user_id = %s AND auction_id = '%s'", user_id, auction_id);
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int count = rs.getInt("count(*)");
                return count > 0;

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return false;
    }

    public List<cartItem> getCartByUser(int user_id) throws SQLException {
        String sql = String.format("SELECT * FROM Cart.Cart WHERE user_id = %s", user_id);
        Statement stmt = null;
        List<cartItem> result = new ArrayList<>();
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            result.add(cartItem.createCart(rs));
        }
        return result;

    }


    public void clearItemCart(String auction_id) throws SQLException {
        String sql = String.format("DELETE FROM Cart.Cart WHERE auction_id = '%s'", auction_id);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.execute();
    }
}
