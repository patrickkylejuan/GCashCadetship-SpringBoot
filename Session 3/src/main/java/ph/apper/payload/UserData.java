package ph.apper.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserData {

    private String id;

    private String email;

    @JsonProperty(value = "first_name")
    private String firstName;

    @JsonProperty(value = "last_name")
    private String lastName;

    @JsonProperty(value = "birth_date")
    private String birthDate;

    @JsonProperty(value = "date_registered")
    private String dateRegistered;

    @JsonProperty(value = "date_verified")
    private String dateVerified;

    @JsonProperty(value = "last_login")
    private String lastLogin;

    @JsonProperty(value = "is_verified")
    private boolean isVerified;

    @JsonProperty(value = "is_active")
    private boolean isActive;
}
