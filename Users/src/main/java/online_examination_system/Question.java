package online_examination_system;

public abstract class Question {
    //variables
    protected String questionId;
    protected String questionText;
    protected int marks;

    //Constructor
    public Question(String questionId, String questionText, int marks){
        this.questionId=questionId;
        this.questionText=questionText;
        this.marks=marks;
    }

    //GETTERS
    public String getQuestion(){
        return questionText;
    }
    public String getQuestionID(){
        return questionId;
    }
    public int getMarks(){
        return marks;
    }

    //Display question method
    public void displayQuestion(){
        System.out.println(getQuestionID()+". "+getQuestion()+"  ["+getMarks()+"] ");
    }

    //check Answer method
}
