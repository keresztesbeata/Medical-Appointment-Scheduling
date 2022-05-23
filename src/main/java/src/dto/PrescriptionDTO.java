package src.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@Component
public class PrescriptionDTO {
    private Integer appointmentId;

    @NotBlank(message = "Medications fields should not be empty!")
    private String medication;

    @NotBlank(message = "Indications are missing!")
    private String indications;

    private LocalDateTime appointmentDate;

    private String patientFirstName;
    private String patientLastName;

    private String doctorFirstName;
    private String doctorLastName;
}
