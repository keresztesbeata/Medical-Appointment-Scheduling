package src.model.users;

import src.model.Appointment;
import src.model.Specialty;

import java.time.LocalTime;
import java.util.List;

public class Doctor {
    private Account account;
    private String fullName;
    private Specialty specialty;
    private LocalTime startTime;
    private LocalTime finishTime;
    private List<Appointment> appointments;
}
