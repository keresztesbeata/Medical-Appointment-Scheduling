package src.model.users;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "receptionists")
public class ReceptionistProfile extends UserProfile{

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(unique = true, nullable = false, length = 100)
    private String phone;
}
