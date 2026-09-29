package online_examination_system.users;

public abstract class Users {
    
    //variables
    protected String userId;
    protected String name;
    protected String email;
    protected String password;
    protected String role;

    //Constructor
    public Users(String userid, String username, String email, int password) {
        this(userid, username, email, String.valueOf(password));
    }

    public Users(String userid, String username, String email, String password) {
        this.userId=userid;
        this.name=username;
        this.email=email;
        this.password=password;
        this.role = getClass().getSimpleName().toLowerCase();
    }

    //Getter functions
    public String getUserId() { return this.userId; }
    public String getName()   { return this.name; }
    public String getEmail()  { return this.email; }
    public String getPassword() { return this.password; }
    public String getRole() { return this.role; }

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
        return login(email, String.valueOf(enteredPassword));
    }

    public boolean login(String email, String enteredPassword){
        if(emailvalidator(email)){
            if(this.email.equalsIgnoreCase(email) && this.password.equals(enteredPassword)){
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
