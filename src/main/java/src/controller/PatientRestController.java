package src.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import src.dto.AppointmentDTO;
import src.dto.PatientProfileDTO;
import src.exceptions.*;
import src.model.AppointmentStatus;
import src.model.users.Account;
import src.service.impl.AppointmentServiceImpl;
import src.service.impl.DoctorProfileServiceImpl;
import src.service.impl.PatientProfileServiceImpl;
import src.service.impl.PrescriptionServiceImpl;

import java.util.Arrays;
import java.util.stream.Collectors;

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

    /**
     * GET "patient/view_profile"
     *
     * @return 200 OK - if the current patient is logged in and has an existing profile
     * 404 NOT FOUND - if the patient is not logged in or it doesn't have a profile set up
     */
    @GetMapping(UrlAddressCatalogue.PATIENT_VIEW_PROFILE)
    public ResponseEntity viewPatientProfile() {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            return ResponseEntity.of(patientService.findById(currentUserAccount.getId()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    /**
     * POST "patient/setup_profile"
     *
     * @param userProfileDTO contains the data related to the patients profile
     *                       ex. body: {
     *                       "firstName": "example_firstName",
     *                       "lastName": "example_lastName",
     *                       "birthdate": "yyyy-mm-dd",
     *                       "email": "example@email.com",
     *                       "phone": "0123456789",
     *                       "allergies": "example_allergy
     *                       }
     * @return 200 OK - if the current user's profile information was successfully saved
     * 404 NOT FOUND - if there is no user logged in
     * 400 BAD REQUEST - if the provided data is invalid
     */
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

    /**
     * GET "/patient/prescriptions/all"
     *
     * @return 200 OK - if the currently logged in user is a patient
     * 404 NOT FOUND - if no currently logged in user or not a patient
     */
    @GetMapping(UrlAddressCatalogue.PATIENT_GET_ALL_PRESCRIPTIONS)
    public ResponseEntity getAllPrescriptionsOfPatient() {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            return ResponseEntity.ok().body(prescriptionService.findByPatientId(currentUserAccount.getId()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    /**
     * GET "/patient/prescriptions/all?appointmentId="
     *
     * @return 200 OK - if the appointment has an associated prescription
     * 404 NOT FOUND - if no prescription was yet created for the given appointment or no appointment exists with the given id
     */
    @GetMapping(UrlAddressCatalogue.PATIENT_GET_PRESCRIPTION_BY_ID)
    public ResponseEntity getPrescriptionsOfCurrentPatientById(@RequestParam Integer appointmentId) {
        try {
            return ResponseEntity.ok().body(prescriptionService.findByAppointmentId(appointmentId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping(UrlAddressCatalogue.COMMON_GET_ALL_MEDICAL_SERVICES)
    public ResponseEntity getAllMedicalServices() {
        return ResponseEntity.ok().body(appointmentService.findAllMedicalServices());
    }

    /**
     * GET "/patient/export_prescription?appointmentId="
     *
     * @return 200 OK - if the appointment has an associated prescription and it could be successfully exported
     * 404 NOT FOUND - if no prescription was yet created for the given appointment or no appointment exists with the given id
     */
    @PostMapping(UrlAddressCatalogue.PATIENT_EXPORT_PRESCRIPTION)
    public ResponseEntity exportPrescription(@RequestParam Integer appointmentId) {
        try {
            prescriptionService.exportPrescription(appointmentId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    /**
     * POST "/patient/appointment/new"
     *
     * @param appointmentDTO ex. body: {
     *                       "doctorFirstName": "example_doctorFirstName",
     *                       "doctorLastName": "example_doctorLastName",
     *                       "medicalService": "example_medicalService"
     *                       }
     * @return 201 CREATED - if the appointment could be successfully created
     * 404 NOT FOUND - if no doctor was found with the given name or no medical service was found with the given name
     * 400 BAD REQUEST - if some of the required data is missing
     */
    @PostMapping(UrlAddressCatalogue.PATIENT_CREATE_APPOINTMENT)
    public ResponseEntity createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            appointmentService.create(currentUserAccount.getId(), appointmentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        } catch (InvalidDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    /**
     * POST "/patient/appointment/update_state?appointmentId=&newState="
     *
     * @param appointmentId the id of the appointment
     * @param newState      the new state
     * @return 200 OK - if the appointemnt's state could be successfully updated
     * 405 METHOD NOT ALLOWED - if the user has no right to modfiy the state of the appointment (ex doctor)
     * 400 BAD REQUEST - if the transition to the new state from the current state is not possible
     */
    @PostMapping(UrlAddressCatalogue.PATIENT_UPDATE_APPOINTMENT_STATE)
    public ResponseEntity updateAppointmentStatus(@RequestParam Integer appointmentId, @RequestParam String newState) {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            appointmentService.updateStatus(appointmentId, currentUserAccount, newState);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        } catch (InvalidAccessException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e);
        } catch (InvalidStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    @GetMapping(UrlAddressCatalogue.PATIENT_GET_APPOINTMENTS)
    public ResponseEntity getAppointmentsByStatus(@RequestParam String status) {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            return ResponseEntity.ok().body(appointmentService.filterByPatientAndStatus(currentUserAccount.getId(), status));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping(UrlAddressCatalogue.PATIENT_GET_PAST_APPOINTMENTS)
    public ResponseEntity getPastAppointments() {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            return ResponseEntity.ok().body(appointmentService.findPastAppointmentsOfPatient(currentUserAccount.getId()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping(UrlAddressCatalogue.PATIENT_GET_UPCOMING_APPOINTMENTS)
    public ResponseEntity getUpcomingAppointments() {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            return ResponseEntity.ok().body(appointmentService.findUpcomingAppointmentsOfPatient(currentUserAccount.getId()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping(UrlAddressCatalogue.COMMON_SEARCH_DOCTOR_BY_MEDICAL_SERVICE)
    public ResponseEntity searchDoctor(@RequestParam String medicalService) {
        try {
            return ResponseEntity.ok().body(appointmentService.findDoctorsByMedicalService(medicalService));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping(UrlAddressCatalogue.COMMON_SEARCH_DOCTOR_BY_NAME)
    public ResponseEntity getDoctorProfile(@RequestParam String firstName, @RequestParam String lastName) {
        return ResponseEntity.ok().body(doctorProfileService.findByName(firstName, lastName));
    }

    @GetMapping(UrlAddressCatalogue.COMMON_ALL_APPOINTMENT_STATUSES)
    public ResponseEntity getAllAppointmentStatuses() {
        return ResponseEntity.ok().body(Arrays.stream(AppointmentStatus.values()).map(Enum::name).collect(Collectors.toList()));
    }

}
