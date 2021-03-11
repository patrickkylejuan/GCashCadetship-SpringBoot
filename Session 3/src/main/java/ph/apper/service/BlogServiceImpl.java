package ph.apper.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ph.apper.domain.Blog;
import ph.apper.domain.User;
import ph.apper.exception.BlogNotFoundException;
import ph.apper.exception.InvalidBlogRegistrationRequestException;
import ph.apper.payload.*;
import ph.apper.repository.BlogRepository;
import ph.apper.repository.UserRepository;
import ph.apper.repository.VerificationCodeRepository;
import ph.apper.util.IdService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Profile({"dev","prod"})
@Service
public class BlogServiceImpl implements BlogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final BlogRepository blogRepository;

    public BlogServiceImpl(UserRepository userRepository, VerificationCodeRepository verificationCodeRepository, BlogRepository blogRepository) {
        this.userRepository = userRepository;
        this.verificationCodeRepository = verificationCodeRepository;
        this.blogRepository = blogRepository;
    }


    @Override
    public BlogRegistrationResponse create(BlogRegistrationRequest request) throws InvalidBlogRegistrationRequestException {
        //verify that user id is verified and active user request.getUser_id()
        //filter this stream bro
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
        //SAVE newBlog to repo
        blogRepository.save(newBlog);

        //return the response
        return new BlogRegistrationResponse(newBlog.getBlog_id());
    }

    @Override
    public BlogData getBlog(String blog_id) throws BlogNotFoundException {
        Blog blog = getBlogByID(blog_id);
        return BlogServiceUtil.toBlogData(blog);
    }

    @Override
    public void updateBlog(String blog_id, UpdateBlogRequest request) throws BlogNotFoundException, InvalidBlogRegistrationRequestException {
        Blog blog = getBlogByID(blog_id);
        if (Objects.nonNull(request.getTitle())) {
            blog.setTitle(request.getTitle());
        }

        if (Objects.nonNull(request.getContent())) {
            blog.setContent(request.getContent());
        }

        blog.setLast_updated(LocalDateTime.now());

        blogRepository.save(blog);

    }

    @Override
    public void deleteBlog(String blog_id) throws BlogNotFoundException {
        Blog blog = getBlogByID(blog_id);
        blog.setVisible(false);
        blogRepository.save(blog);

    }

    @Override
    @Transactional
    public List<BlogData> getAllBlogs(boolean all) {

        List<Blog> blogList = new ArrayList<>();
        List<BlogData> blogDataList = new ArrayList<>();

        // if all = true, include all blogs, if all = false, include the not deleted ones (is_visible =true)
        if(all == true){
            blogRepository.findAll().forEach(blogList::add);
            Stream<Blog> blogStream = blogList.stream();
            blogStream.forEach(blog -> blogDataList.add(BlogServiceUtil.toBlogData(blog)));
        }
        else {
            Stream<Blog> blogStream = blogRepository.findAllByIsVisible(true);
            blogStream.forEach(blog -> blogDataList.add(BlogServiceUtil.toBlogData(blog)));

        }
        return blogDataList;

    }


//    @Override
//    @Transactional
//    public List<BlogData> getAllBlogs(boolean all) {
//          List<BlogData> blogDataList = new ArrayList<>();
//          Stream<Blog> blogStream = blogRepository.findAllByIs_Visible(all);
//          blogStream.forEach(b -> blogDataList.add(BlogServiceUtil.toBlogData(b)));
//          return blogDataList;
//
//
//    }




    private boolean isVerifiedAndActiveUser(String id) {
        Optional<User> idQ = userRepository.findById(id);
        return idQ.isPresent() && idQ.get().isVerified() && idQ.get().isActive();
    }

    private Blog getBlogByID(String blog_id) throws BlogNotFoundException {
        return blogRepository.findById(blog_id).orElseThrow(() -> new BlogNotFoundException("Blog" + blog_id + "not found"));
    }
}
