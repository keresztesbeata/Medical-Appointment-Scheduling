package src.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Component
public class UserProfileDTO {
    @NotBlank(message = "First name should not be empty!")
    @Length(min = 3)
    private String firstName;

    @NotBlank(message = "Last name should not be empty!")
    @Length(min = 3)
    private String lastName;
}
