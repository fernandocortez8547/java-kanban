package exceptions;

public class KvClientStartException extends RuntimeException{
    public KvClientStartException() {}

    public KvClientStartException(String s){
        super(s);
    }
}
