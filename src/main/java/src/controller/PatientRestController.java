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
import src.service.impl.PrescriptionServiceImpl;

@RestController
@Log4j2
public class PatientRestController {
    @Autowired
    private PatientProfileServiceImpl patientService;
    @Autowired
    private PrescriptionServiceImpl prescriptionService;

    @GetMapping(UrlAddressCatalogue.PATIENT_VIEW_PROFILE)
    public ResponseEntity viewPatientProfile() {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            return ResponseEntity.of(patientService.findById(currentUserAccount.getId()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @PostMapping(UrlAddressCatalogue.PATIENT_UPDATE_PROFILE)
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

    @GetMapping(UrlAddressCatalogue.PATIENT_VIEW_ALL_PRESCRIPTIONS)
    public ResponseEntity viewAllPrescriptionsOfPatient(@RequestParam String firstName, @RequestParam String lastName) {
        try{
            return ResponseEntity.ok().body(prescriptionService.findByPatient(firstName, lastName));
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping(UrlAddressCatalogue.PATIENT_EXPORT_PRESCRIPTION)
    public ResponseEntity exportPrescription(@RequestParam Integer prescriptionId) {
        try{
            prescriptionService.exportPrescription(prescriptionId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

}
