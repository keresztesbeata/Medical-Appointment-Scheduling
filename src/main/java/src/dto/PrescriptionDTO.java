package src.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Getter
@Setter
@Component
public class PrescriptionDTO {

    private String medication;

    private String indications;

    private LocalDate appointmentDate;

    private String patientName;
}
