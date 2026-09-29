package online_examination_system.users;

import java.util.ArrayList;
import java.util.List;

// Admin extends Users - an admin is a special type of user
public class Admin extends Users {

    private String adminId;
    private List<Users> managedUsers;  
    public Admin(String userId, String name, String email, int password, String adminId) {
        this(userId, name, email, String.valueOf(password), adminId);
    }

    public Admin(String userId, String name, String email, String password, String adminId) {
        super(userId, name, email, password);
        this.adminId = adminId;
        this.managedUsers = new ArrayList<>();
    }

    // add a user to managed list
    public void manageUsers(Users user) {  // changed User to Users
        managedUsers.add(user);
        System.out.println("  [ADMIN]  User registered: " + user.getName() + " (" + user.getClass().getSimpleName() + ")");
    }

    public String getAdminId() { return adminId; }
    public List<Users> getManagedUsers() { return managedUsers; }
}
