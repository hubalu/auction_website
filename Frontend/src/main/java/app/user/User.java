package app.user;

import lombok.*;

@Value // All fields are private and final. Getters (but not setters) are generated (https://projectlombok.org/features/Value.html)
public class User {
    String username;
    String salt;
    String hashedPassword;
    int userId;
    UserType userType;
    boolean deactivate;
    public User(String username, String salt, String hashedPassword, int userId, UserType userType, boolean deactivate) {
        this.username = username;
        this.salt = salt;
        this.hashedPassword = hashedPassword;
        this.userId = userId;
        this.userType = userType;
        this.deactivate = deactivate;
    }

    public boolean getDeactivate(){
        return this.deactivate;
    }
}
