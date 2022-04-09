package src.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Getter
@Setter
@Component
public class DoctorProfileDTO extends UserProfileDTO{
    @NotBlank(message = "The specialty cannot be missing!")
    private String specialty;

    @NotNull(message = "The start time cannot be missing!")
    private LocalTime startTime;

    @NotNull(message = "The finish time cannot be missing!")
    private LocalTime finishTime;
}
