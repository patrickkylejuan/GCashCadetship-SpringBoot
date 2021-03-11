package ph.apper.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @JsonProperty(value = "first_name")
    private String firstName;

    @JsonProperty(value = "last_name")
    private String lastName;

    private String password;

    @JsonProperty(value = "birth_date")
    private String birthDate;

    @JsonProperty(value = "is_active")
    private Boolean isActive;
}
