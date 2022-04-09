package src.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "medical_services")
public class MedicalService {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(unique = true, nullable = false, length = 200)
    public String name;

    @Column
    private Integer duration;

    @ManyToOne
    @JoinColumn(name="specialty_id", nullable=false)
    private Specialty specialty;

    @OneToMany(mappedBy = "medicalService")
    private Set<Appointment> appointments;
}
