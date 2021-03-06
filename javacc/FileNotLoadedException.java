public class FileNotLoadedException extends Exception{
    String filename;
    public FileNotLoadedException(String filename){
        this.filename = filename;
    }
    public String getMessage(){
        return "ERROR: file "+filename+" was not loaded.";
    }
}