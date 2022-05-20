package src.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import src.dto.AppointmentDTO;
import src.dto.PatientProfileDTO;
import src.exceptions.*;
import src.model.users.Account;
import src.service.impl.AppointmentServiceImpl;
import src.service.impl.DoctorProfileServiceImpl;
import src.service.impl.PatientProfileServiceImpl;
import src.service.impl.PrescriptionServiceImpl;

@RestController
@Log4j2
public class PatientRestController {
    @Autowired
    private PatientProfileServiceImpl patientService;
    @Autowired
    private PrescriptionServiceImpl prescriptionService;
    @Autowired
    private AppointmentServiceImpl appointmentService;
    @Autowired
    private DoctorProfileServiceImpl doctorProfileService;

    @GetMapping(UrlAddressCatalogue.PATIENT_VIEW_PROFILE)
    public ResponseEntity viewPatientProfile() {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            return ResponseEntity.of(patientService.findById(currentUserAccount.getId()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @PostMapping(UrlAddressCatalogue.PATIENT_SETUP_PROFILE)
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

    @GetMapping(UrlAddressCatalogue.PATIENT_GET_ALL_PRESCRIPTIONS)
    public ResponseEntity getAllPrescriptionsOfPatient(@RequestParam String firstName, @RequestParam String lastName) {
        try{
            return ResponseEntity.ok().body(prescriptionService.findByPatient(firstName, lastName));
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping(UrlAddressCatalogue.PATIENT_GET_ALL_MEDICAL_SERVICES)
    public ResponseEntity getAllMedicalServices() {
        return ResponseEntity.ok().body(appointmentService.findAllMedicalServices());
    }

    @GetMapping(UrlAddressCatalogue.PATIENT_EXPORT_PRESCRIPTION)
    public ResponseEntity exportPrescription(@RequestParam Integer appointmentId) {
        try{
            prescriptionService.exportPrescription(appointmentId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @PostMapping(UrlAddressCatalogue.PATIENT_CREATE_APPOINTMENT)
    public ResponseEntity createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        try{
            Account currentUserAccount = Utils.getCurrentUserAccount();
            appointmentService.create(currentUserAccount.getId(), appointmentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        } catch (InvalidDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    @PostMapping(UrlAddressCatalogue.PATIENT_UPDATE_APPOINTMENT_STATE)
    public ResponseEntity updateAppointmentStatus(@RequestParam Integer appointmentId, @RequestParam String newState) {
        try{
            Account currentUserAccount = Utils.getCurrentUserAccount();
            appointmentService.updateStatus(appointmentId, currentUserAccount, newState);
            return ResponseEntity.ok().build();
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        } catch (InvalidAccessException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e);
        } catch (InvalidStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    @GetMapping(UrlAddressCatalogue.PATIENT_GET_APPOINTMENTS)
    public ResponseEntity getAppointmentsByStatus(@RequestParam String status) {
        try{
            Account currentUserAccount = Utils.getCurrentUserAccount();
            return ResponseEntity.ok().body(appointmentService.filterByPatientAndStatus(currentUserAccount.getId(), status));
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping(UrlAddressCatalogue.PATIENT_GET_PAST_APPOINTMENTS)
    public ResponseEntity getPastAppointments() {
        try{
            Account currentUserAccount = Utils.getCurrentUserAccount();
            return ResponseEntity.ok().body(appointmentService.findPastAppointmentsOfPatient(currentUserAccount.getId()));
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping(UrlAddressCatalogue.PATIENT_GET_UPCOMING_APPOINTMENTS)
    public ResponseEntity getUpcomingAppointments() {
        try{
            Account currentUserAccount = Utils.getCurrentUserAccount();
            return ResponseEntity.ok().body(appointmentService.findUpcomingAppointmentsOfPatient(currentUserAccount.getId()));
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping(UrlAddressCatalogue.PATIENT_SEARCH_DOCTOR_BY_MEDICAL_SERVICE)
    public ResponseEntity searchDoctor(@RequestParam String medicalService) {
        try {
            return ResponseEntity.ok().body(appointmentService.findDoctorsByMedicalService(medicalService));
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping(UrlAddressCatalogue.PATIENT_SEARCH_DOCTOR_BY_NAME)
    public ResponseEntity getDoctorProfile(@RequestParam String firstName, @RequestParam String lastName) {
        return ResponseEntity.ok().body(doctorProfileService.findByName(firstName, lastName));
    }

}
