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

    private Integer id;

    private String patientFirstName;

    private String patientLastName;

    @NotBlank(message = "The first name of the doctor cannot be missing!")
    private String doctorFirstName;

    @NotBlank(message = "The last name of the doctor cannot be missing!")
    private String doctorLastName;

    private String status;

    @Future(message = "The appointment cannot be set to a date in the past!")
    private LocalDateTime appointmentDate;

    @NotBlank(message = "The medical service type should not be missing!")
    private String medicalService;

    private PrescriptionDTO prescription;
}
