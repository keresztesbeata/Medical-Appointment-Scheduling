package src.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import src.dto.AppointmentDTO;
import src.dto.DoctorProfileDTO;
import src.dto.PrescriptionDTO;
import src.exceptions.DuplicateDataException;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidDataException;
import src.exceptions.InvalidStateException;
import src.model.users.Account;
import src.service.impl.AppointmentServiceImpl;
import src.service.impl.DoctorProfileServiceImpl;
import src.service.impl.PatientProfileServiceImpl;
import src.service.impl.PrescriptionServiceImpl;

import java.time.LocalDate;
import java.util.Optional;

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

    private static final String APPOINTMENT_NOT_FOUND_BY_ID_ERR_MSG = "No appointment was found with given id!";

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

    @GetMapping(UrlAddressCatalogue.COMMON_SEARCH_PATIENT_BY_NAME)
    public ResponseEntity searchPatientByName(@RequestParam String firstName, @RequestParam String lastName) {
        return ResponseEntity.ok().body(patientService.findByName(firstName, lastName));
    }

    @GetMapping(UrlAddressCatalogue.DOCTOR_GET_APPOINTMENT_BY_ID)
    public ResponseEntity getAppointmentById(@RequestParam Integer appointmentId) {
        Optional<AppointmentDTO> appointment = appointmentService.findById(appointmentId);
        if(appointment.isPresent()) {
            return ResponseEntity.ok().body(appointment.get());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new EntityNotFoundException(APPOINTMENT_NOT_FOUND_BY_ID_ERR_MSG));
        }
    }

    @GetMapping(UrlAddressCatalogue.DOCTOR_GET_PAST_APPOINTMENTS)
    public ResponseEntity getPastAppointments() {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            return ResponseEntity.ok().body(appointmentService.findPastAppointmentsOfDoctor(currentUserAccount.getId()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping(UrlAddressCatalogue.DOCTOR_GET_PRESENT_APPOINTMENTS)
    public ResponseEntity getPresentAppointments() {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            return ResponseEntity.ok().body(appointmentService.findPresentAppointmentsOfDoctor(currentUserAccount.getId()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping(UrlAddressCatalogue.DOCTOR_GET_FUTURE_APPOINTMENTS)
    public ResponseEntity getFutureAppointments() {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            return ResponseEntity.ok().body(appointmentService.findFutureAppointmentsOfDoctor(currentUserAccount.getId()));
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
            return ResponseEntity.ok().body(prescriptionService.findByPatientName(firstName, lastName));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping(UrlAddressCatalogue.COMMON_GET_ALL_SPECIALTIES)
    public ResponseEntity getAllSpecialties() {
        return ResponseEntity.ok().body(doctorService.getAllSpecialties());
    }
}
