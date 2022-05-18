package src.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import src.dto.DoctorProfileDTO;
import src.dto.PatientProfileDTO;
import src.dto.ReceptionistProfileDTO;
import src.dto.UserProfileDTO;
import src.exceptions.DuplicateDataException;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidDataException;
import src.model.users.Account;
import src.model.users.AccountType;
import src.service.api.UserProfileService;
import src.service.impl.DoctorServiceImpl;
import src.service.impl.PatientServiceImpl;
import src.service.impl.ReceptionistServiceImpl;
import src.service.impl.UserProfileServiceFactory;

@RestController
@Log4j2
public class UserProfileRestController {
    @Autowired
    private PatientServiceImpl patientService;
    @Autowired
    private ReceptionistServiceImpl receptionistService;
    @Autowired
    private DoctorServiceImpl doctorService;

    @GetMapping("/current_user/my_profile")
    public ResponseEntity getUserProfile() {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            UserProfileService userProfileService = UserProfileServiceFactory.getUserProfileService(currentUserAccount.getAccountType());
            return ResponseEntity.of(userProfileService.findById(currentUserAccount.getId()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @PostMapping("/patient/update_profile")
    public ResponseEntity setProfile(@RequestBody PatientProfileDTO userProfileDTO) {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            PatientProfileDTO savedUserProfile = patientService.saveProfile(currentUserAccount.getId(), userProfileDTO);
            return ResponseEntity.ok().body(savedUserProfile);
        } catch (InvalidDataException | DuplicateDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @PostMapping("/doctor/update_profile")
    public ResponseEntity setProfile(@RequestBody DoctorProfileDTO userProfileDTO) {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            DoctorProfileDTO savedUserProfile = doctorService.saveProfile(currentUserAccount.getId(), userProfileDTO);
            return ResponseEntity.ok().body(savedUserProfile);
        } catch (InvalidDataException | DuplicateDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @PostMapping("/receptionist/update_profile")
    public ResponseEntity setProfile(@RequestBody ReceptionistProfileDTO userProfileDTO) {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            ReceptionistProfileDTO savedUserProfile = receptionistService.saveProfile(currentUserAccount.getId(), userProfileDTO);
            return ResponseEntity.ok().body(savedUserProfile);
        } catch (InvalidDataException | DuplicateDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping("/doctor/patient_profiles")
    public ResponseEntity getUserProfile(@RequestParam UserProfileDTO userProfileDTO) {
        return ResponseEntity.ok().body(UserProfileServiceFactory.getUserProfileService(AccountType.PATIENT).findByName(userProfileDTO.getFirstName(), userProfileDTO.getLastName()));
    }
}
