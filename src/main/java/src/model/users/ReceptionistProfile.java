package src.model.users;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "receptionist_profiles")
public class ReceptionistProfile {
    @Id
    @Column(name = "receptionist_id")
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(unique = true, nullable = false, length = 100)
    private String phone;
}
