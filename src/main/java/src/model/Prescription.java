package src.model;

import lombok.Getter;
import lombok.Setter;
import src.model.users.DoctorProfile;
import src.model.users.PatientProfile;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "prescriptions")
public class Prescription {
    @Id
    @Column(name = "appointment_id")
    private Integer id;

    @Column(length = 500)
    private String medication;

    @Column(length = 500)
    private String indications;

    @OneToOne
    @MapsId
    @JoinColumn(name="appointment_id")
    private Appointment appointment;
}
