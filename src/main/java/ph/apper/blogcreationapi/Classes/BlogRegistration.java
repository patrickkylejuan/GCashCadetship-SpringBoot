package ph.apper.blogcreationapi.Classes;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class BlogRegistration {

    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "content is required")
    private String content;

    @NotBlank(message = "date published is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}") //ISO FORMAT YYYY-MM--DD
    private String datePublished;

    @NotBlank(message = "author is required")
    private String author;
}
