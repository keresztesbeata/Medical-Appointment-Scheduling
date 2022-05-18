package src.model.users;

import lombok.Getter;
import lombok.Setter;
import src.model.Appointment;
import src.model.Specialty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "doctor_profiles")
public class DoctorProfile {
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
