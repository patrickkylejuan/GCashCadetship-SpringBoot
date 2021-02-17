package ph.apper.blogcreationapi;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ph.apper.blogcreationapi.Classes.Blog;
import ph.apper.blogcreationapi.Classes.BlogRegistration;
import ph.apper.blogcreationapi.Exceptions.InvalidBlogRegistrationException;
import ph.apper.blogcreationapi.Exceptions.InvalidContentUpdateException;
import ph.apper.blogcreationapi.Exceptions.InvalidIDEntryException;
import ph.apper.blogcreationapi.Exceptions.InvalidPostDeleteException;
import ph.apper.blogcreationapi.Requests.IdInput;
import ph.apper.blogcreationapi.Requests.PostContent;
import ph.apper.blogcreationapi.Responses.BlogRegistrationResponse;
import ph.apper.blogcreationapi.Services.BlogService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@RestController
public class BlogController {
    //Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(BlogController.class);
    //Declare objects of service classes that you will use + make constructor
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    // NUMBER 1 -> Blog creation API (POST)

    @PostMapping(path = "create")
    public ResponseEntity<BlogRegistrationResponse>
    create(@Valid @RequestBody BlogRegistration request, @RequestHeader("reference-number") String referenceNumber)
        throws InvalidBlogRegistrationException {
        LOGGER.info("Processing blog creation with reference number {}", referenceNumber);
        // make a new Blog object by using the create function in your blogservice class
        // (automatically adds the blog and the details to a list)
        // (ensures that all fields are present)
        Blog createdBlog = blogService.create(request);
        //initiate your response object
        BlogRegistrationResponse response = new BlogRegistrationResponse();
        //Set response id to be the one created from blogService.create
        response.setId(createdBlog.getId());

        LOGGER.info("Processing user registration with reference number {} done", referenceNumber);
        return ResponseEntity.created(URI.create(createdBlog.getId())).body(response);  // creates URI of new blog post
    }

//    @GetMapping(path = "listblogs") //need exception handling
//    public ResponseEntity<List<Blog>> getBlogs(@RequestHeader("reference-number") String referenceNumber) {
//        LOGGER.info("Processing blogs retrieval with reference number {}", referenceNumber);
//        List<Blog> displayList = new ArrayList<>();
//        Collections.copy(displayList,blogService.getBlogs());
//        LOGGER.info("Blogs retrieval with reference number {} done!", referenceNumber);
//        return ResponseEntity.ok(displayList);
//    }

    // NUMBER 2 -> Retrieval of created Blogs (GET)
    @GetMapping(path = "listblogs") //need exception handling
    public List<Blog> getBlogs(@RequestHeader("reference-number") String referenceNumber) {
        LOGGER.info("Processing blogs retrieval with reference number {}", referenceNumber);
        LOGGER.info("Blogs retrieval with reference number {} done!", referenceNumber);
        return blogService.getBlogs();
    }

    //NUMBER 3 -> Retrieval of blog based on blog id (GET)
    @GetMapping(path = "getblog")
    public  Blog
    getBlogByID(@Valid @RequestBody IdInput request, @RequestHeader("reference-number") String referenceNumber)
            throws InvalidIDEntryException {
        //if ID is not in blogs list
        blogService.checkID(request.getIdInput()).orElseThrow(() -> new InvalidIDEntryException("Blog not found on :: "+ request.getIdInput()));
        LOGGER.info("Processing blog retrieval with reference number {}", referenceNumber);
        LOGGER.info("Processing blog retrieval with reference number {} done", referenceNumber);
        return blogService.findPost(request.getIdInput());
    }

    // NUMBER 4 -> Update the content of a blog (based on ID) (PUT)

    @PutMapping(path = "updateblog")
    public Blog
    updateBlog(@Valid @RequestBody PostContent request, @RequestHeader("reference-number") String referenceNumber)
            throws InvalidContentUpdateException {
        //if ID is not in blogs list
        blogService.checkID(request.getIdInput()).orElseThrow(() -> new InvalidContentUpdateException("Blog not found on :: "+ request.getIdInput()));
        LOGGER.info("Processing blog content update with reference number {}", referenceNumber);
        // Get the object from blogs list and update its content field

        for (Blog blog : blogService.getBlogs()) {
            if (blog.getId().equals(request.getIdInput())) {
                blog.setContent(request.getContentInput());
            }
        }

        LOGGER.info("Processing blog content update with reference number {} done", referenceNumber);
        return blogService.findPost(request.getIdInput()) ;

    }


    // NUMBER 5 -> Delete a blog post
    @DeleteMapping(path = "deleteblog")
    public ResponseEntity<String> deletePost(@Valid @RequestBody IdInput request, @RequestHeader("reference-number") String referenceNumber)
    throws InvalidPostDeleteException {
        blogService.checkID(request.getIdInput()).orElseThrow(() -> new InvalidPostDeleteException("Blog not found on :: "+ request.getIdInput()));
        LOGGER.info("Processing blog deletion with reference number {}", referenceNumber);

        boolean isPostDeleted = blogService.deletePost(request.getIdInput());

        if (isPostDeleted == false) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOGGER.info("Processing blog deletion with reference number {} done", referenceNumber);
//        return new ResponseEntity<>(request.getIdInput(), HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body("Blog post with ID: " + request.getIdInput() + "successfully deleted!");
    }









}

