package online_examination_system.main;

import online_examination_system.users.Faculty;

public class Main {
    public static void main(String[] args){
        
        Faculty f1=new Faculty("aj_7@7", "Ajul T.M", "ajultm@gmail.com", 777, "F101-firstUser", "Mathematics");

        Faculty f2=new Faculty("er#111", "ErrorUser1", "Err@gmail.com", 4041, "F404-FirstError", "ErrorHandling-01");

        Faculty f3=new  Faculty("er#1102", "ErrorUser2", "@error@gmail.com", 40402, "F404-SecondError", "ErrorHandling-02");

    }
}
