package ph.apper.blogcreationapi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ph.apper.blogcreationapi.Exceptions.InvalidBlogRegistrationException;
import ph.apper.blogcreationapi.Exceptions.InvalidContentUpdateException;
import ph.apper.blogcreationapi.Exceptions.InvalidIDEntryException;
import ph.apper.blogcreationapi.Exceptions.InvalidPostDeleteException;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class BlogCreationExceptionHandler {

    //exceptionfor1

    @ExceptionHandler({InvalidBlogRegistrationException.class,InvalidIDEntryException.class, InvalidContentUpdateException.class, InvalidPostDeleteException.class})
    public ResponseEntity<Map<String,String>> handleLogicException(Exception e)
    {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Invalid request: " + e.getMessage());

        return ResponseEntity.badRequest().body(response);

    }






}
