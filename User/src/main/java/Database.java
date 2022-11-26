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
        String sql = "CREATE DATABASE IF NOT EXISTS Users;";
        try (Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS Users.User (\n"
                + "	id INT AUTO_INCREMENT PRIMARY KEY,\n"
                + "	username VARCHAR(255) NOT NULL,\n"
                + "	userType VARCHAR(255) NOT NULL,\n"
                + "	password VARCHAR(255) NOT NULL,\n"
                + "	email VARCHAR(255) NOT NULL,\n"
                + "	phoneNumber VARCHAR(255) NOT NULL,\n"
                + "	suspend Boolean NOT NULL\n"
                + ");";
        try (Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertIntoTable(String username, UserType userType, String password, String email,String phoneNumber) throws SQLException {
        String sql = "INSERT INTO Users.User (username,userType,password,email,phoneNumber,suspend) VALUES(?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, userType.toString());
        pstmt.setString(3, password);
        pstmt.setString(4, email);
        pstmt.setString(5, phoneNumber);
        pstmt.setBoolean(6, false);

        pstmt.executeUpdate();

    }

    public void suspendUser(Integer id) throws SQLException {
        String sql = String.format("UPDATE Users.User SET suspend = true WHERE id = %d", id);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();
    }

    public void updateUser(Integer id, String username, String password, String email,String phoneNumber) throws SQLException {
        String sql = String.format("UPDATE Users.User SET username = '%s', password = '%s', email = '%s', phoneNumber = '%s' WHERE id = %d", username, password, email, phoneNumber ,id);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();
    }

    public void deleteUser(Integer id) throws SQLException {
        String sql = String.format("DELETE FROM Users.User WHERE id = %d", id);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();
    }

    public boolean checkIfUserExist(String userName) {
        String sql = String.format("SELECT COUNT(1) FROM Users.User WHERE username = '%s'", userName);
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int count = rs.getInt("COUNT(1)");
                if (count > 0) {
                    return true;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean checkIfEmailExist(String email) {
        String sql = String.format("SELECT COUNT(1) FROM Users.User WHERE email = '%s'", email);
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int count = rs.getInt("COUNT(1)");
                if (count > 0) {
                    return true;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return false;
    }

    public int findId(String userName) throws SQLException {
        String sql = String.format("SELECT id FROM Users.User WHERE username = '%s'", userName);
        Statement stmt = null;

        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            return rs.getInt("id");
        }
        return -1;
    }

    public User findUserById(Integer Id) throws SQLException {
        String sql = String.format("SELECT * FROM Users.User WHERE id = '%s'", Id);
        Statement stmt = null;

        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        User result = new User();
        while (rs.next()) {
            return User.createUser(rs);
        }
        return result;
    }

    public List<User> findAllUsers() throws SQLException {
        String sql = "SELECT * FROM Users.User";
        Statement stmt = null;

        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<User> results = new ArrayList<>();
        while (rs.next()) {
            results.add(User.createUser(rs));
        }
        return results;
    }

}
