package src.model.users;

import src.model.Appointment;

import java.time.LocalDate;
import java.util.Set;

public class Patient {
    private Account account;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate birthdate;
    private String allergies;
    private Set<Appointment> appointments;
}
