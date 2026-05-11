package online_examination_system.users;

import java.util.ArrayList;
import java.util.List;

import online_examination_system.exams.Exam;

// Admin extends Users - an admin is a special type of user
public class Admin extends Users {

    private String adminId;
    private List<Exam> scheduledExams;
    private List<Users> managedUsers;  
    public Admin(String userId, String name, String email, int password, String adminId) {
        this(userId, name, email, String.valueOf(password), adminId);
    }

    public Admin(String userId, String name, String email, String password, String adminId) {
        super(userId, name, email, password);
        this.adminId = adminId;
        this.scheduledExams = new ArrayList<>();
        this.managedUsers = new ArrayList<>();
    }

    // add exam to scheduled list
    public void scheduleExam(Exam exam) {
        scheduledExams.add(exam);
        System.out.println("  [ADMIN]  Exam scheduled: " + exam.getTitle() + " | Duration: " + exam.getDuration() + " mins");
    }

    // add a user to managed list
    public void manageUsers(Users user) {  // changed User to Users
        managedUsers.add(user);
        System.out.println("  [ADMIN]  User registered: " + user.getName() + " (" + user.getClass().getSimpleName() + ")");
    }

    // print all scheduled exams
    public void listExams() {
        System.out.println("  [ADMIN]  Scheduled Exams:");
        for (Exam e : scheduledExams)
            System.out.println("           - " + e.getTitle());
    }

    public String getAdminId() { return adminId; }
    public List<Exam> getScheduledExams() { return scheduledExams; }
    public List<Users> getManagedUsers() { return managedUsers; }
}
