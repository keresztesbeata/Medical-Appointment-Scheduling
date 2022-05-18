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
@Table(name = "doctors")
public class DoctorProfile extends UserProfile{

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
