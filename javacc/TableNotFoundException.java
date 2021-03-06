public class TableNotFoundException extends Exception{
    String name;
    public TableNotFoundException(String name){
        this.name = name;
    }
    public String getMessage(){
        return "ERROR: table "+name+" does not exist.";
    }
}