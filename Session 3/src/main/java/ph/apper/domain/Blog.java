package ph.apper.domain;

import lombok.Data;
import org.apache.tomcat.jni.Local;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
public class Blog {
    @Id
    private String blog_id;

    private String title;
    private String content;

    private String user_id; //this must match userid present in database


    private LocalDateTime date_publish;
    private LocalDateTime last_updated;

    private boolean isVisible = true; // visible by default

    public Blog(String blog_id) {
        this.blog_id = blog_id;
    }


    public Blog() {
    }
}
