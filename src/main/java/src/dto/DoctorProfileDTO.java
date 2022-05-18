package src.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Getter
@Setter
@Component
public class DoctorProfileDTO {
    @NotBlank(message = "First name should not be empty!")
    @Length(min = 3)
    private String firstName;

    @NotBlank(message = "Last name should not be empty!")
    @Length(min = 3)
    private String lastName;

    @NotBlank(message = "The specialty cannot be missing!")
    private String specialty;

    @NotNull(message = "The start time cannot be missing!")
    private LocalTime startTime;

    @NotNull(message = "The finish time cannot be missing!")
    private LocalTime finishTime;
}
