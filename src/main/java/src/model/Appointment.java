package src.model;

import src.model.users.Doctor;
import src.model.users.Patient;

import java.time.LocalDateTime;

public class Appointment {
    private Integer id;
    private Patient patient;
    private Doctor doctor;
    private AppointmentStatus status;
    private LocalDateTime date;
    private MedicalService medicalService;
}
