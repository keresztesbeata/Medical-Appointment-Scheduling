package src.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import src.model.users.ReceptionistProfile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Component
public class ReceptionistProfileDTO extends ReceptionistProfile {
    @NotBlank(message = "The email cannot be missing!")
    @Email(message = "Email should have a valid format: name@domain !")
    private String email;

    @NotBlank(message = "The phone number cannot be missing!")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Phone number should consist of a sequence of 10 digits !")
    private String phone;
}
