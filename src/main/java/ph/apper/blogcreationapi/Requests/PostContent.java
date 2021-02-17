package ph.apper.blogcreationapi.Requests;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PostContent {
    @NotBlank(message = "Input Blog ID is required")
    private String idInput;
    @NotBlank(message = "Input Blog Content is required")
    private String contentInput;

}
