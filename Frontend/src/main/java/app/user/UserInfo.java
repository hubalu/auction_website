package app.user;

import lombok.*;

import java.io.Serializable;

@Value // All fields are private and final. Getters (but not setters) are generated (https://projectlombok.org/features/Value.html)
public class UserInfo implements Serializable {
    int UserId;
    String Username;
    String Email;
    String PhoneNumber;

    public UserInfo(int userId, String username, String email, String phoneNumber) {
        this.UserId = userId;
        this.Username = username;
        this.Email = email;
        this.PhoneNumber = phoneNumber;
    }
}
