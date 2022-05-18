package src.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import src.dto.DoctorProfileDTO;
import src.exceptions.DuplicateDataException;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidDataException;
import src.model.users.Account;
import src.service.impl.DoctorProfileServiceImpl;
import src.service.impl.PatientProfileServiceImpl;

@RestController
public class DoctorRestController {
    @Autowired
    private DoctorProfileServiceImpl doctorService;
    @Autowired
    private PatientProfileServiceImpl patientService;

    @GetMapping("/doctor/my_profile")
    public ResponseEntity getDoctorProfile() {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            return ResponseEntity.of(doctorService.findById(currentUserAccount.getId()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @PostMapping("/doctor/update_profile")
    public ResponseEntity updateProfile(@RequestBody DoctorProfileDTO userProfileDTO) {
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

    @GetMapping("/doctor/patient_profiles")
    public ResponseEntity getPatientProfiles(@RequestParam String firstName, @RequestParam String lastName) {
        return ResponseEntity.ok().body(patientService.findByName(firstName, lastName));
    }
}
