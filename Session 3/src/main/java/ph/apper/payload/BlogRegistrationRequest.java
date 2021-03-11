package ph.apper.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BlogRegistrationRequest {
    @JsonProperty(value = "title")
    @NotBlank(message = "title is required")
    private String title;

    @JsonProperty(value = "content")
    @NotBlank(message = "content is required")
    private String content;

    @JsonProperty(value = "user_id")
    @NotBlank(message = "user_id is required")
    private String user_id;
}
