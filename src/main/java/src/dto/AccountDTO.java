package src.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Component
public class AccountDTO {
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

    @NotBlank(message = "Username should not be empty!")
    private String username;

    @NotBlank(message = "Password should not be empty!")
    @Pattern(regexp = PASSWORD_PATTERN, message = "Password should contain: " +
            " at least 1 digit, 1 lowercase and 1 uppercase letter and 1 special character," +
            " with length at least 8 and at most 20 characters!")
    private String password;

    @NotBlank
    private String accountType;
}
