package src.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import src.dto.AccountDTO;
import src.dto.PatientProfileDTO;
import src.dto.UserProfileDTO;

@Getter
@Setter
@ToString
public class RegisterRequest {
    private AccountDTO accountDTO;
    private UserProfileDTO userProfileDTO;
}
