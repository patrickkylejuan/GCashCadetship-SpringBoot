package ph.apper.payload;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BlogData {
    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "content")
    private String content;

    @JsonProperty(value = "blog_id")
    private String blog_id;

    @JsonProperty(value = "user_id")
    private String user_id;

    @JsonProperty(value = "date_publish")
    private String date_publish;

    @JsonProperty(value = "last_updated")
    private String last_updated;

    @JsonProperty(value = "is_visible")
    private boolean isVisible;
}
