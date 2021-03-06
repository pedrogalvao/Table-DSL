public class ParameterNotFoundException extends Exception{
    String name;
    public ParameterNotFoundException(String name){
        this.name = name;
    }
    public String getMessage(){
        return "ERROR: parmeter "+name+" was not found. Please pass enough parameters to the program.";
    }
}