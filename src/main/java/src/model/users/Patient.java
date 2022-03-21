package src.model.users;

import src.model.Appointment;

import java.time.LocalDate;
import java.util.List;

public class Patient {
    private Integer id;
    private Account account;
    private String email;
    private String phone;
    private LocalDate birthdate;
    private String allergies;
    private List<Appointment> appointments;
}
