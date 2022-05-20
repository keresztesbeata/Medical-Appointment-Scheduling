package src.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@Component
@ToString
public class PatientProfileDTO {
    private Integer id;

    @NotBlank(message = "First name should not be empty!")
    @Length(min = 3)
    private String firstName;

    @NotBlank(message = "Last name should not be empty!")
    @Length(min = 3)
    private String lastName;

    @NotBlank(message = "The email cannot be missing!")
    @Email(message = "Email should have a valid format: name@domain !")
    private String email;

    @NotBlank(message = "The phone number cannot be missing!")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Phone number should consist of a sequence of 10 digits !")
    private String phone;

    @NotNull(message = "The birthdate cannot be missing!")
    @Past(message = "The birthdate should be before the current date!")
    private LocalDate birthdate;

    @Length(max = 300, message = "The list of allergies should not be longer than 300 characters!")
    private String allergies;
}
