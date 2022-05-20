package src.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import src.dto.DoctorProfileDTO;
import src.dto.PrescriptionDTO;
import src.exceptions.DuplicateDataException;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidDataException;
import src.exceptions.InvalidStateException;
import src.model.users.Account;
import src.service.api.AppointmentService;
import src.service.api.PrescriptionService;
import src.service.impl.AppointmentServiceImpl;
import src.service.impl.DoctorProfileServiceImpl;
import src.service.impl.PatientProfileServiceImpl;
import src.service.impl.PrescriptionServiceImpl;

import java.time.LocalDate;

@RestController
public class DoctorRestController {
    @Autowired
    private DoctorProfileServiceImpl doctorService;
    @Autowired
    private PatientProfileServiceImpl patientService;
    @Autowired
    private AppointmentServiceImpl appointmentService;
    @Autowired
    private PrescriptionServiceImpl prescriptionService;

    @GetMapping(UrlAddressCatalogue.DOCTOR_VIEW_PROFILE)
    public ResponseEntity viewDoctorProfile() {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            return ResponseEntity.of(doctorService.findById(currentUserAccount.getId()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @PostMapping(UrlAddressCatalogue.DOCTOR_SETUP_PROFILE)
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

    @GetMapping(UrlAddressCatalogue.DOCTOR_SEARCH_PATIENT_BY_NAME)
    public ResponseEntity searchPatientByName(@RequestParam String firstName, @RequestParam String lastName) {
        return ResponseEntity.ok().body(patientService.findByName(firstName, lastName));
    }

    @GetMapping(UrlAddressCatalogue.DOCTOR_GET_ALL_APPOINTMENTS)
    public ResponseEntity getAllAppointments(@RequestParam String status) {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            return ResponseEntity.ok().body(appointmentService.filterByDoctorAndStatus(currentUserAccount.getId(), status));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping(UrlAddressCatalogue.DOCTOR_GET_APPOINTMENTS_OF_TODAY)
    public ResponseEntity getAppointmentsOfToday() {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            return ResponseEntity.ok().body(appointmentService.findAppointmentsOfDoctorByDate(currentUserAccount.getId(), LocalDate.now()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @PostMapping(UrlAddressCatalogue.DOCTOR_CREATE_PRESCRIPTION)
    public ResponseEntity updateProfile(@RequestParam Integer appointmentId, @RequestBody PrescriptionDTO prescriptionDTO) {
        try {
            prescriptionService.addPrescription(appointmentId, prescriptionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (InvalidStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping(UrlAddressCatalogue.DOCTOR_GET_PATIENTS_PRESCRIPTIONS)
    public ResponseEntity getPatientsPastPrescriptions(@RequestParam String firstName, @RequestParam String lastName) {
        try {
            return ResponseEntity.ok().body(prescriptionService.findByPatient(firstName, lastName));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping(UrlAddressCatalogue.DOCTOR_GET_ALL_SPECIALTIES)
    public ResponseEntity getAllSpecialties() {
        return ResponseEntity.ok().body(doctorService.getAllSpecialties());
    }
}
