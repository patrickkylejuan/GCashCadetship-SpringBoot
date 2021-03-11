package ph.apper.exception;

public class InvalidBlogRegistrationRequestException extends Exception{
    public InvalidBlogRegistrationRequestException(String message) {
        super(message);
    }

    public InvalidBlogRegistrationRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
