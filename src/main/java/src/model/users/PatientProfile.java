package src.model.users;

import lombok.Getter;
import lombok.Setter;
import src.model.Appointment;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "patient_profiles")
public class PatientProfile {
    @Id
    @Column(name = "patient_id")
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;

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
