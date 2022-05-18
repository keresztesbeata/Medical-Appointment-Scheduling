package src.model.users;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "user_accounts")
public class Account {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true, nullable = false, length = 100)
    @NotBlank(message = "Username should not be empty!")
    private String username;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Password should not be empty!")
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Account type should not be missing!")
    private AccountType accountType;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private User user;
}
