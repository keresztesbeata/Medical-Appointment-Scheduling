package src.model.users;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Entity
@Table(name = "user_profiles")
public class UserProfile {
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

    @OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL)
    private User user;
}
