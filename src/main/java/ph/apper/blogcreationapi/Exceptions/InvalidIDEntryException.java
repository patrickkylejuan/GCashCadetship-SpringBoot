package ph.apper.blogcreationapi.Exceptions;

public class InvalidIDEntryException extends Exception {

    public InvalidIDEntryException(String message) {super(message);}
    public InvalidIDEntryException(String message, Throwable cause) {super(message,cause);}
}
