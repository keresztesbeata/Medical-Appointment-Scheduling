package src.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Component
public class PrescriptionDTO {

    private Integer id;

    private String medication;

    private String indications;

    @NotNull(message = "Appointment date should not be missing!")
    private LocalDateTime appointmentDate;

    @NotBlank(message = "Patient name should not be empty!")
    private String patientFirstName;

    @NotBlank(message = "Patient name should not be empty!")
    private String patientLastName;
}
