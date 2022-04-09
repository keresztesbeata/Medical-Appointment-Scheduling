package src.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Component
public class UserProfileDTO {
    @NotBlank(message = "First name should not be empty!")
    private String firstName;

    @NotBlank(message = "Last name should not be empty!")
    private String lastName;
}
