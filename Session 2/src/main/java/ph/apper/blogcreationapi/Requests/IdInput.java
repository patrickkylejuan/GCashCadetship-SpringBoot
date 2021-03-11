package ph.apper.blogcreationapi.Requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class IdInput {
    @NotBlank(message = "Input ID is required")
    private String idInput;
}
