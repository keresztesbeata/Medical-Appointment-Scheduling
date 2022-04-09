package src.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import src.model.users.DoctorProfile;
import src.model.users.PatientProfile;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Component
public class AppointmentDTO {

    private PatientProfile patient;

    private DoctorProfile doctor;

    private String status;

    @NotNull(message = "The appointment date cannot be missing!")
    @Future(message = "The appointment cannot be set to a date in the past!")
    private LocalDateTime appointmentDate;

    @NotBlank(message = "The medical service type should not be missing!")
    private String medicalService;

    private String prescription;
}
