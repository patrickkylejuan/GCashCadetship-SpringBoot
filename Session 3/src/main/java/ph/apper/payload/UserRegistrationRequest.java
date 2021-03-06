package ph.apper.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserRegistrationRequest {
    @JsonProperty(value = "first_name")
    @NotBlank(message = "first_name is required")
    private String firstName;

    @JsonProperty(value = "last_name")
    @NotBlank(message = "last_name is required")
    private String lastName;

    @Email(message = "email must be valid")
    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    @JsonProperty(value = "birth_date")
    @NotBlank(message = "birth_date is required")
    private String birthDate;
}
