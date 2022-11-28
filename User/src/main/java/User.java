import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.sql.ResultSet;
import java.sql.SQLException;

@Value
public class User {
    int id;
    String username;
    String password;
    String email;
    String phoneNumber;
    String userType;
    Boolean suspend;

    public User() {
        this.id = 0;
        this.username = null;
        this.password = null;
        this.email = null;
        this.phoneNumber = null;
        this.userType = null;
        this.suspend = false;
    }

    public User(int id, String username, String password, String email, String phoneNumber, String userType, Boolean suspend) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        this.suspend = suspend;
    }

    public static User createUser(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getString("phoneNumber"), rs.getString("userType"), rs.getBoolean("suspend"));
    }

}
