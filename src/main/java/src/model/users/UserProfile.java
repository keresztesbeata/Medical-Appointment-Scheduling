package src.model.users;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Entity
@Table(name = "user_profiles")
public abstract class UserProfile {

    @Id
    @Column(name = "user_profile_id")
    private Integer id;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "First name should not be empty!")
    private String firstName;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "Last name should not be empty!")
    private String lastName;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
