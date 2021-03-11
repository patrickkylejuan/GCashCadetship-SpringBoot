package ph.apper.blogcreationapi.Exceptions;

public class InvalidContentUpdateException extends Exception {

    public InvalidContentUpdateException(String message) {super(message);}
    public InvalidContentUpdateException(String message, Throwable cause) { super (message,cause);}
}
