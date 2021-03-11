package ph.apper.blogcreationapi.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ph.apper.blogcreationapi.Classes.Blog;
import ph.apper.blogcreationapi.Classes.BlogRegistration;
import ph.apper.blogcreationapi.Exceptions.InvalidBlogRegistrationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;


@Service
public class BlogService {

    //Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(BlogService.class);



    //List of blogs WITH id
    private final List<Blog> blogs = new ArrayList<>();

    //Generate getter
    public List<Blog> getBlogs() {
        return blogs;
    }

    //Declaring Services
    private final IdService idService;

    //Injecting ID Service via constructor
    public BlogService(IdService idService){
        this.idService = idService;
    }

    // Making a create method for creation of blogs (generating IDS, adding them to the object, saving them in list)
    public Blog create(BlogRegistration blogRegistration) throws InvalidBlogRegistrationException {

        LocalDate parsedDatePublished = LocalDate.parse(blogRegistration.getDatePublished());

        //generate Blog ID
        String blogId = idService.getNextBlogId();
        LOGGER.info("Generated Blog ID: {}", blogId);

        // Save Blog Registration WITH blog id to a new blog object called newBlog
        Blog newBlog = new Blog(blogId);
        newBlog.setTitle(blogRegistration.getTitle());
        newBlog.setContent(blogRegistration.getContent());
        newBlog.setDatePublished(blogRegistration.getDatePublished());
        newBlog.setAuthor(blogRegistration.getAuthor());

        blogs.add(newBlog);

        return newBlog;
    }

    //Method to retrieve a blog object from blogs based on ID

    public Blog findPost(String id){
        return blogs.stream().filter(blog -> id.equals(blog.getId())).findFirst().get();
    }

    //method to Check if there are null IDs
    public Optional<Blog> checkID(String id){
        return blogs.stream().filter(blog -> id.equals(blog.getId())).findFirst();
    }

    //method to delete ID
    public boolean deletePost(String id) {
        boolean isPostDeleted = false;

        ListIterator<Blog> iter = blogs.listIterator();
        while(iter.hasNext()){
            if(iter.next().getId().equals(id)){
                iter.remove();
                isPostDeleted = true;
            }
        }

//        for (Blog blog : blogs) {
//            if (blog.getId().equals(id)) {
//                //delete the blog object from blogs
//                blogs.remove(blog);
//                isPostDeleted = true;
//            }
//        }
        return isPostDeleted;
    }
}
