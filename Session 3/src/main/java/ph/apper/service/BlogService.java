package ph.apper.service;

import ph.apper.exception.BlogNotFoundException;
import ph.apper.exception.InvalidBlogRegistrationRequestException;
import ph.apper.payload.*;

import java.util.List;

public interface BlogService {

    //create blog post
    BlogRegistrationResponse create(BlogRegistrationRequest request) throws InvalidBlogRegistrationRequestException;
    BlogData getBlog(String id) throws BlogNotFoundException;
    void updateBlog(String id, UpdateBlogRequest request) throws InvalidBlogRegistrationRequestException, BlogNotFoundException;
    void deleteBlog(String id) throws BlogNotFoundException;
    List<BlogData> getAllBlogs(boolean all);


}
