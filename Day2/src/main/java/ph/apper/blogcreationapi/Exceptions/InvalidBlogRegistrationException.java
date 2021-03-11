package ph.apper.blogcreationapi.Exceptions;

public class InvalidBlogRegistrationException extends Exception{
    public InvalidBlogRegistrationException(String message) {super(message);}
    public InvalidBlogRegistrationException(String message, Throwable cause) { super (message,cause);}
}
