package ph.apper.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BlogRegistrationResponse {
    public BlogRegistrationResponse(String blog_id) {
        this.blog_id = blog_id;
    }

    @JsonProperty("blog_id")
    private String blog_id;
}
