package src.model.users;

import lombok.Getter;
import lombok.Setter;
import src.model.Appointment;
import src.model.Specialty;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "doctor_profiles")
public class DoctorProfile {
    @Id
    @Column(name = "doctor_id")
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;

    @ManyToOne
    @JoinColumn(name="specialty_id", nullable=false)
    private Specialty specialty;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime finishTime;

    @OneToMany(mappedBy="doctor")
    private Set<Appointment> appointments;
}
