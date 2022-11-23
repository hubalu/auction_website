import java.sql.SQLException;

public class UserManagement {


    private Database db;

    public UserManagement(String databaseName) {
        this.db = new Database(databaseName);
        this.db.createDatabase();
        this.db.createTableIfNotExists();
    }


    public boolean deleteUser(Integer id, Integer operatorId, UserType operatorRole) {
        try {
            if (operatorId.equals(id) || operatorRole == UserType.Admin) {
                this.db.deleteUser(id);
            }else {
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public int createUser(String username, UserType userType, String password, String email,String phoneNumber) {
        try {
            if (this.db.checkIfUserExist(username) || this.db.checkIfEmailExist(email)){
                return -1;
            } else {
                this.db.insertIntoTable(username, userType, password, email, phoneNumber);
                return this.db.findId(username);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return -1;
        }
    }

    public boolean updateUser(Integer id, String username, String password, String email,String phoneNumber) {
        try {
            this.db.updateUser(id, username, password, email, phoneNumber);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean suspendUser(Integer id, UserType operatorRole) {
        if (operatorRole == UserType.Admin) {
            try {
                this.db.suspendUser(id);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }
}