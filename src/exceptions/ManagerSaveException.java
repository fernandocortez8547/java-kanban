package exceptions;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException() {}

    public ManagerSaveException (String s){
        super(s);
    }
}
