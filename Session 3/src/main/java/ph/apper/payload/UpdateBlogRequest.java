package ph.apper.payload;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateBlogRequest {
    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "content")
    private String content;
}
