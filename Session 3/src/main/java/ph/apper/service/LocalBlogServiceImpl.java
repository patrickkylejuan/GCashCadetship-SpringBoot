package ph.apper.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ph.apper.domain.Blog;
import ph.apper.domain.User;
import ph.apper.domain.VerificationCode;
import ph.apper.exception.BlogNotFoundException;
import ph.apper.exception.InvalidBlogRegistrationRequestException;
import ph.apper.payload.*;
import ph.apper.util.IdService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

@Service
@Profile("local")
public class LocalBlogServiceImpl implements BlogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalUserServiceImpl.class);

    private final List<User> users = new ArrayList<>();
    private final List<VerificationCode> verificationCodes = new ArrayList<>();
    private final List<Blog> blogs = new ArrayList<>();


    @Override
    public BlogRegistrationResponse create(BlogRegistrationRequest request) throws InvalidBlogRegistrationRequestException {
        if (!(isVerifiedAndActiveUser(request.getUser_id()))) {
            throw new InvalidBlogRegistrationRequestException("user is either unverified or inactive. pls try again!");
        }

        //generate id
        String blogId = IdService.getNextUserId();
        LOGGER.info("Generated BLOG ID: {}", blogId);
        Blog newBlog = new Blog(blogId); //blog id inserted in constructor
        newBlog.setTitle(request.getTitle());
        newBlog.setContent(request.getContent());
        newBlog.setUser_id(request.getUser_id());
        // update date_published to local date and time
        newBlog.setDate_publish(LocalDateTime.now());
        newBlog.setLast_updated(LocalDateTime.now());
        newBlog.setVisible(true);
        //SAVE newBlog to list
        blogs.add(newBlog);

        //return the response
        return new BlogRegistrationResponse(newBlog.getBlog_id());
    }

    @Override
    public BlogData getBlog(String id) throws BlogNotFoundException {
        Blog b = getBlogById(id);

        return toBlogData(b);
    }

    @Override
    public void updateBlog(String id, UpdateBlogRequest request) throws InvalidBlogRegistrationRequestException, BlogNotFoundException {
        checkID(id).orElseThrow(() -> new BlogNotFoundException("Blog not found on :: "+ id));

        for (Blog blog : blogs) {
            if (blog.getBlog_id().equals(id)) {
                blog.setContent(request.getContent());
                blog.setTitle(request.getTitle());
            }
        }


    }

    @Override
    public void deleteBlog(String id) throws BlogNotFoundException {
        Blog blog = getBlogById(id);
        blog.setVisible(false);
    }

    @Override
    public List<BlogData> getAllBlogs(boolean all) {
        List<BlogData> blogData = new ArrayList<>();
        List<BlogData> preBlogData = new ArrayList<>();
        Stream<Blog> blogStream = blogs.stream();

        if(all == true){
            blogStream.forEach(blog -> blogData.add(toBlogData(blog)));
        }
        else {

            blogStream = blogs.stream().filter(b -> b.isVisible());
            blogStream.forEach(blog -> blogData.add(toBlogData(blog)));
        }
        return blogData;
    }

    private boolean isVerifiedAndActiveUser(String user_id) {
        try {
            return getUserById(user_id).isVerified() && getUserById(user_id).isActive();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private User getUserById(String id) {
        return users.stream()
                .filter(user -> id.equals(user.getId()))
                .findFirst()
                .orElseThrow();
    }

    private Blog getBlogById(String id) {
        return blogs.stream()
                .filter(blog -> blog.equals(blog.getBlog_id()))
                .findFirst()
                .orElseThrow();
    }

    private BlogData toBlogData(Blog b) {

        BlogData blogData = new BlogData();
        blogData.setBlog_id(b.getBlog_id());
        blogData.setTitle(b.getTitle());
        blogData.setContent(b.getContent());
        blogData.setDate_publish(b.getDate_publish().format(DateTimeFormatter.ISO_DATE_TIME));
        blogData.setLast_updated(b.getLast_updated().format(DateTimeFormatter.ISO_DATE_TIME));
        blogData.setUser_id(b.getUser_id());
        blogData.setVisible(b.isVisible());


        return blogData;
    }

    //method to Check if there are null IDs
    public Optional<Blog> checkID(String id){
        return blogs.stream().filter(blog -> id.equals(blog.getBlog_id())).findFirst();
    }






}
