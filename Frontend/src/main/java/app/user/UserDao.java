package app.user;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDao {

//    private final List<User> users = List.of(
//            //        Username    Salt for hash                    Hashed password (the password is "password" for all users)
//            new User("perwendel", "$2a$10$h.dl5J86rGH7I8bD9bZeZe", "$2a$10$h.dl5J86rGH7I8bD9bZeZeci0pDt0.VwFTGujlnEaZXPf/q7vM5wO"),
//    );
//
    private List<User> users = new ArrayList<>();


    public User getUserByUsername(String username) {
        return users.stream().filter(b -> b.getUsername().equals(username)).findFirst().orElse(null);
    }

    public Iterable<String> getAllUserNames() {
        return users.stream().map(User::getUsername).collect(Collectors.toList());
    }

    public void addUser(){

        users.add(new User("perwendel", "$2a$10$h.dl5J86rGH7I8bD9bZeZe", "$2a$10$h.dl5J86rGH7I8bD9bZeZeci0pDt0.VwFTGujlnEaZXPf/q7vM5wO"));
    }

}
