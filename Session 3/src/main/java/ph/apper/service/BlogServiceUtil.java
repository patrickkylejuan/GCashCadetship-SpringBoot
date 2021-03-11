package ph.apper.service;

import ph.apper.domain.Blog;
import ph.apper.payload.BlogData;
import ph.apper.payload.UserData;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public final class BlogServiceUtil {
    public static BlogData toBlogData(Blog b) {
        BlogData blogData = new BlogData();
        blogData.setBlog_id(b.getBlog_id());
        blogData.setContent(b.getContent());
        blogData.setTitle(b.getTitle());
        blogData.setUser_id(b.getUser_id());
        blogData.setDate_publish(b.getDate_publish().format(DateTimeFormatter.ISO_DATE_TIME));
        blogData.setLast_updated(b.getLast_updated().format(DateTimeFormatter.ISO_DATE_TIME));
        blogData.setVisible(b.isVisible());

        return blogData;
    }
}
