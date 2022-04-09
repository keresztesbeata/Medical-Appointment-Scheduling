package src.model;

import lombok.Getter;
import lombok.Setter;
import src.model.users.DoctorProfile;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "specialties")
public class Specialty {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(unique = true, nullable = false, length = 200)
    public String name;

    @OneToMany(mappedBy = "specialty")
    private Set<MedicalService> medicalServices;

    @OneToMany(mappedBy = "specialty")
    private Set<DoctorProfile> doctorProfiles;
}
