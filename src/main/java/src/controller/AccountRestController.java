package src.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import src.config.JwtTokenProvider;
import src.dto.AccountDTO;
import src.dto.DoctorProfileDTO;
import src.dto.PatientProfileDTO;
import src.exceptions.DuplicateDataException;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidDataException;
import src.mapper.AccountMapper;
import src.model.users.Account;
import src.model.users.AccountType;
import src.service.impl.AccountServiceImpl;
import src.service.impl.DoctorProfileServiceImpl;
import src.service.impl.PatientProfileServiceImpl;

@RestController
@Log4j2
public class AccountRestController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private DoctorProfileServiceImpl doctorProfileService;

    @Autowired
    private PatientProfileServiceImpl patientProfileService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    private static final String PROFILE_NOT_SET_ERROR_MESSAGE = "Your profile has not yet been set up! You should start by setting up your profile first!";

    /**
     * GET "/current_user"
     *
     * @return 200 OK - Along with the user's account details if the user is logged in
     * 404 NOT FOUND - If there is currently no logged in user
     */
    @GetMapping(value = UrlAddressCatalogue.CURRENT_USER)
    @ResponseBody
    public ResponseEntity getLoggedInUser() {
        try {
            AccountMapper accountMapper = new AccountMapper();
            Account account = Utils.getCurrentUserAccount();
            AccountDTO accountDTO = accountMapper.mapToDto(account);
            switch (account.getAccountType()) {
                case PATIENT -> accountDTO.setHasProfile(patientProfileService.findById(account.getId()).isPresent());
                case DOCTOR -> accountDTO.setHasProfile(doctorProfileService.findById(account.getId()).isPresent());
                case RECEPTIONIST -> accountDTO.setHasProfile(false);
            }
            return ResponseEntity.ok().body(accountDTO);
        } catch (EntityNotFoundException e) {
            log.warn("getLoggedInUser: {} ", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    /**
     * GET "/current_user_profile"
     *
     * @return 200 OK - Along with the user's profile details if the user is logged in
     * 404 NOT FOUND - If there is currently no logged in user
     * 204 NO CONTENT - If the user doesn't have an associated profile, for ex the Receptionist
     */
    @GetMapping(value = UrlAddressCatalogue.CURRENT_USER_PROFILE)
    @ResponseBody
    public ResponseEntity getLoggedInUserProfile() {
        try {
            Account account = Utils.getCurrentUserAccount();
            if (account.getAccountType().equals(AccountType.DOCTOR)) {
                DoctorProfileDTO doctorProfile = doctorProfileService.findById(account.getId())
                        .orElseThrow(() -> new EntityNotFoundException(PROFILE_NOT_SET_ERROR_MESSAGE));

                return ResponseEntity.ok().body(doctorProfile);
            } else if (account.getAccountType().equals(AccountType.PATIENT)) {
                PatientProfileDTO patientProfile = patientProfileService.findById(account.getId())
                        .orElseThrow(() -> new EntityNotFoundException(PROFILE_NOT_SET_ERROR_MESSAGE));

                return ResponseEntity.ok().body(patientProfile);
            }

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (EntityNotFoundException e) {
            log.warn("getLoggedInUserProfile: {} ", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    /**
     * POST "/perform_login"
     *
     * @param accountDTO contains the credentials of the user
     *                   ex. body: {
     *                   "username": "example_username",
     *                   "password": "example_password"
     *                   }
     * @return 200 OK - Along with a valid Jwt Token if the user has logged in successfully
     * 404 NOT FOUND - If the user provided invalid credentials
     */
    @PostMapping(UrlAddressCatalogue.PERFORM_LOGIN)
    public ResponseEntity authenticateUser(@RequestBody AccountDTO accountDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            accountDTO.getUsername(),
                            accountDTO.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            return ResponseEntity.ok().body(new JwtAuthenticationResponse(jwt));
        } catch (AuthenticationException e) {
            log.error("authenticateUser: {} ", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    /**
     * POST "/perform_register"
     *
     * @param accountDTO contains the user's credentials and the type of account to be created
     *                   ex. body: {
     *                   "username": "example_username",
     *                   "password": "example_password",
     *                   "accountType": "example_account_type"
     *                   }
     * @return 201 CREATED - if the user could be successfully registered
     * 400 BAD REQUEST - if the user provided invalid or missing data
     */
    @PostMapping(value = UrlAddressCatalogue.PERFORM_REGISTER)
    public ResponseEntity register(@RequestBody AccountDTO accountDTO) {
        try {
            accountService.register(accountDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (InvalidDataException | DuplicateDataException e) {
            log.error("register: {} ", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }
}
