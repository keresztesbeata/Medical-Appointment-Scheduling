package src.model.users;

import lombok.Getter;
import lombok.Setter;
import src.model.Appointment;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "patient_profiles")
public class PatientProfile {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "First name should not be empty!")
    private String firstName;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "Last name should not be empty!")
    private String lastName;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Account account;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(unique = true, nullable = false, length = 100)
    private String phone;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(length = 300)
    private String allergies;

    @OneToMany(mappedBy="patient")
    private Set<Appointment> appointments;
}
