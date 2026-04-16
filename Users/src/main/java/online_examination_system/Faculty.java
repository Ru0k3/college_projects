package online_examination_system;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;

public class Faculty extends Users{
    //variables
    private String facultyId;
    private String subject;
    private List<Question> questionBank;

    //Constructor
    public Faculty(String userId, String name, String email, int password, String facultyId, String subject ){
        super(userId, name, email, password);
        this.facultyId=facultyId;
        this.subject=subject;
        this.questionBank=new ArrayList<Question>();
    }


    
    //GETTERS
    public String getFacultyID(){
        return facultyId;
    }
    public String getSubject(){
        return subject;
    }

    //createQuestion method
    public void createQuestion(Question q){
        try{
            if(q==null){
                throw new IllegalArgumentException("Question cannot be null");
            }
            questionBank.add(q);
            System.out.println("Question Id: "+q.questionId+" Question added to the List by "+this.facultyId);

        }
        catch(Exception e){
            System.out.println("Unexpected Error in creating question"+e.getMessage());
        }
    }

    //manageExam method
    
}
