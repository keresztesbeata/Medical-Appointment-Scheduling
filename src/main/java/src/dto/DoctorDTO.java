package src.dto;

import src.model.Appointment;

import java.time.LocalTime;
import java.util.Set;

public class DoctorDTO {
    private String username;
    private String specialty;
    private LocalTime startTime;
    private LocalTime finishTime;
    private Set<Appointment> appointments;
}
