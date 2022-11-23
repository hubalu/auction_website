import org.junit.jupiter.api.Test;

public class UserManagementTest {

    @Test
    public void testUserManagement(){
        UserManagement um = new UserManagement("data.db");
        int id1 = um.createUser("jemma", UserType.User, "12345", "hell@gmail.com", "12345");
        int id2 = um.createUser("geogery", UserType.Admin, "12345777", "hellooooo@gmail.com", "123456666");
        int id3 = um.createUser("lala", UserType.Admin, "12345777", "h@gmail.com", "123456666");
        int id4 = um.createUser("xx", UserType.User, "bxnc", "xx@gmail.com", "whaaaat");
        System.out.println(id1);
        System.out.println(id4);

        um.updateUser(3, "lalaland", "123456", "hhh@gmail.com", "00000");

        um.suspendUser(1, UserType.Admin);
        um.deleteUser(2, 2, UserType.User);
    }
}