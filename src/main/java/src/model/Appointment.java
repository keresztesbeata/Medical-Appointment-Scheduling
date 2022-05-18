package src.model;

import lombok.Getter;
import lombok.Setter;
import src.model.users.DoctorProfile;
import src.model.users.PatientProfile;
import src.model.users.User;
import src.model.users.UserProfile;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name="patient_id", nullable=false)
    private PatientProfile patient;

    @ManyToOne
    @JoinColumn(name="doctor_id", nullable=false)
    private DoctorProfile doctor;

    @Column(nullable = false)
    private AppointmentStatus status;

    @Column(nullable = false)
    private LocalDateTime appointmentDate;

    @ManyToOne
    @JoinColumn(name="medical_service_id", nullable=false)
    private MedicalService medicalService;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Prescription prescription;

}
