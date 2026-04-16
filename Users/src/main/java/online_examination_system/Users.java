package online_examination_system;

public abstract class Users {
    
    //variables
    protected String userId;
    protected String name;
    protected String email;
    protected int password;

    //Constructor
    public Users(String userid, String username, String email, int password) {
        this.userId=userid;
        this.name=username;
        this.email=email;
        this.password=password;
    }

    //Getter functions
    public String getUserId() { return this.userId; }
    public String getName()   { return this.name; }
    public String getEmail()  { return this.email; }

    //methods 

    //email validator
    boolean emailvalidator(String enteredEmail){
        if(enteredEmail==null){
            System.out.println("Enter email");
            return false;
        }
        int indexOfAt=enteredEmail.indexOf('@');
        if(indexOfAt<=0 || indexOfAt==enteredEmail.length()-1 || indexOfAt!= enteredEmail.lastIndexOf('@')){
            return false;
        }
        if(!enteredEmail.endsWith(".com")){
            return false;
        }
        if(!enteredEmail.toLowerCase().equals(enteredEmail)){
            return false;
        }
        return true;
    }


    //LOGIN
    public boolean login(String email, int enteredPassword){
        if(emailvalidator(email)){
            if(this.email.equals(email) && this.password==enteredPassword){
                System.out.println("User Logged in successfully");
                return true;
            }
        }
        System.out.println("Invalid credentials");
        return false;
    }


    //LOGOUT
    public void logout(){
        System.out.println("LOGGED OUT FROM "+this.name);
    }
    
}
