package src.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import src.dto.PatientProfileDTO;
import src.exceptions.DuplicateDataException;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidDataException;
import src.model.users.Account;
import src.service.impl.PatientProfileServiceImpl;

@RestController
@Log4j2
public class PatientRestController {
    @Autowired
    private PatientProfileServiceImpl patientService;

    @GetMapping("/patient/my_profile")
    public ResponseEntity getPatientProfile() {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            return ResponseEntity.of(patientService.findById(currentUserAccount.getId()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @PostMapping("/patient/update_profile")
    public ResponseEntity updateProfile(@RequestBody PatientProfileDTO userProfileDTO) {
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


}
