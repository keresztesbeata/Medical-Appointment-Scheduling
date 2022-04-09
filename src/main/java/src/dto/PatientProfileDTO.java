package src.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;
import src.model.users.UserProfile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@Component
public class PatientProfileDTO extends UserProfile {
    @NotBlank(message = "The email cannot be missing!")
    @Email(message = "Email should have a valid format: name@domain !")
    private String email;

    @NotBlank(message = "The phone number cannot be missing!")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Phone number should consist of a sequence of 10 digits !")
    private String phone;

    @NotBlank(message = "The birthdate cannot be missing!")
    @Past(message = "The birthdate should be before the current date!")
    private LocalDate birthdate;

    @Length(max = 300, message = "The list of allergies should not be longer than 300 characters!")
    private String allergies;
}
