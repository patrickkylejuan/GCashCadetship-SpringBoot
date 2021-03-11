package ph.apper.blogcreationapi.Exceptions;

public class InvalidPostDeleteException extends Exception{

    public InvalidPostDeleteException(String message) {super(message);}
    public InvalidPostDeleteException(String message, Throwable cause) {super(message,cause);}
}
