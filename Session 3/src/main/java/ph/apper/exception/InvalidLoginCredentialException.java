package ph.apper.exception;

public class InvalidLoginCredentialException extends Exception {
    public InvalidLoginCredentialException(String message) {
        super(message);
    }

    public InvalidLoginCredentialException(String message, Throwable cause) {
        super(message, cause);
    }
}
