package ph.apper.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ph.apper.exception.BlogNotFoundException;
import ph.apper.exception.InvalidBlogRegistrationRequestException;
import ph.apper.exception.InvalidUserRegistrationRequestException;
import ph.apper.exception.UserNotFoundException;
import ph.apper.payload.*;
import ph.apper.service.BlogService;
import java.util.stream.Stream;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("blog")
public class BlogController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    //declare service and constructor
    private final BlogService blogService;


    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping
    public ResponseEntity<BlogRegistrationResponse> create(
            @Valid @RequestBody BlogRegistrationRequest request,
            @RequestHeader("Reference-Number") String referenceNumber) throws InvalidBlogRegistrationRequestException {
        LOGGER.info("Blog registration {} received", referenceNumber);

        BlogRegistrationResponse response = blogService.create(request);

        LOGGER.info("Blog registration {} successful", referenceNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<BlogData> getBlog(@PathVariable("id") String blogId) throws BlogNotFoundException {
        return ResponseEntity.ok(blogService.getBlog(blogId));
    }

    @PatchMapping("{id}")
    public ResponseEntity<GenericResponse> updateBlog(
            @PathVariable String id,
            @RequestHeader("Reference-Number") String referenceNumber,
            @Valid @RequestBody UpdateBlogRequest request) throws BlogNotFoundException, InvalidBlogRegistrationRequestException {
        LOGGER.info("Update blog with reference number {} received", referenceNumber);

        blogService.updateBlog(id, request);

        LOGGER.info("Update blog with reference number {} successful", referenceNumber);

        return ResponseEntity.ok(new GenericResponse("update blog success"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<GenericResponse> deleteBlog(
            @PathVariable("id") String blog_id,
            @RequestHeader("Reference-Number") String referenceNumber) throws BlogNotFoundException {
        LOGGER.info("Deleting user with reference number {} received", referenceNumber);

        blogService.deleteBlog(blog_id);

        LOGGER.info("Delete user with reference number {} successful", referenceNumber);

        return ResponseEntity.ok(new GenericResponse("delete user success"));
    }


    @GetMapping
    public ResponseEntity<List<BlogData>> getAllUsers(@RequestParam(defaultValue= "false") boolean all) {
        // if all = true, include all blogs, if all = false, include only the not deleted ones (is_visible =true)
        return ResponseEntity.ok(blogService.getAllBlogs(all));
    }

//    @GetMapping
//    public ResponseEntity<List<BlogData>> getAllBlogs() {
//        return ResponseEntity.ok(blogService.getAllBlogs(true));
//    }


}
