package src.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Component
public class AppointmentDTO {

    @NotBlank(message = "The first name of the patient cannot be missing!")
    private String patientFirstName;

    @NotBlank(message = "The last name of the patient cannot be missing!")
    private String patientLastName;

    @NotBlank(message = "The first name of the doctor cannot be missing!")
    private String doctorFirstName;

    @NotBlank(message = "The last name of the doctor cannot be missing!")
    private String doctorLastName;

    private String status;

    @NotNull(message = "The appointment date cannot be missing!")
    @Future(message = "The appointment cannot be set to a date in the past!")
    private LocalDateTime appointmentDate;

    @NotBlank(message = "The medical service type should not be missing!")
    private String medicalService;

    private PrescriptionDTO prescription;
}
