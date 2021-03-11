package ph.apper.exception;

public class BlogNotFoundException extends  Exception{
    public BlogNotFoundException(String message) {
        super(message);
    }

    public BlogNotFoundException(String message, Throwable cause) {
        super(message, cause);}
}
