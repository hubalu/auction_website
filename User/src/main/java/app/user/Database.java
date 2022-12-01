package app.user;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class Database {
    private Connection conn;
    public Database(String database_path) {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://mysql-db/", "root", "secretpass");
            System.out.println("Connection Established");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createDatabase() {
        String sql = "CREATE DATABASE IF NOT EXISTS Users;";
        try (Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            System.out.println("Users Database Created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS Users.User (\n"
                + "	id INT AUTO_INCREMENT PRIMARY KEY,\n"
                + "	username VARCHAR(255) NOT NULL,\n"
                + "	userType VARCHAR(255) NOT NULL,\n"
                + "	email VARCHAR(255) NOT NULL,\n"
                + "	phoneNumber VARCHAR(255) NOT NULL,\n"
                + "	suspend Boolean NOT NULL\n"
                + ");";
        try (Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            System.out.println("Users.User Table Created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertIntoTable(String username, UserType userType, String email,String phoneNumber) throws SQLException {
        //TODO 5. Use the sqlite connection to insert a new record into the
        //the database.
        //The timestamp can be insertion time and doesn't have to be the actual
        //fetch time
        String sql = "INSERT INTO Users.User (username,userType,email,phoneNumber,suspend) VALUES(?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, userType.toString());
        pstmt.setString(3, email);
        pstmt.setString(4, phoneNumber);
        pstmt.setBoolean(5, false);

        pstmt.executeUpdate();
        System.out.println("User Inserted");
    }

    public void suspendUser(Integer id) throws SQLException {
        String sql = String.format("UPDATE Users.User SET suspend = true WHERE id = %d", id);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();
    }

    public void updateUser(Integer id, String username, String email,String phoneNumber) throws SQLException {
        String sql = String.format("UPDATE Users.User SET username = '%s', email = '%s', phoneNumber = '%s' WHERE id = %d", username, email, phoneNumber ,id);
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

    public List<UserInfo> getAllUser() throws SQLException {
        String sql = String.format("SELECT id, username, email, phoneNumber FROM Users.User");
        Statement stmt = null;
        List<UserInfo> res = new LinkedList<>();
        System.out.println("Calling Get All Users");
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                UserInfo user = new UserInfo(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("phoneNumber"));
                res.add(user);
            }
            return res;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public UserInfo getOneUser(int id) throws  SQLException{
        String sql = String.format("SELECT id, username, email, phoneNumber FROM Users.User WHERE id = %d", id);
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                int userId = rs.getInt("id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phoneNumber");
                UserInfo user = new UserInfo(userId, username, email, phoneNumber);
                return user;
            }
            return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
