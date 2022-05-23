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


    /**
     * GET "doctor/view_profile"
     *
     * @return 200 OK - if the current doctor is logged in and has an existing profile
     * 404 NOT FOUND - if the doctor is not logged in or it doesn't have a profile set up
     */
    @GetMapping(UrlAddressCatalogue.DOCTOR_VIEW_PROFILE)
    public ResponseEntity viewDoctorProfile() {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            return ResponseEntity.of(doctorService.findById(currentUserAccount.getId()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    /**
     * POST "doctor/setup_profile"
     *
     * @param userProfileDTO contains the data related to the doctors profile
     *                       ex. body: {
     *                       "firstName": "example_firstName",
     *                       "lastName": "example_lastName",
     *                       "specialty": "example_specialty",
     *                       "startTime": "hh:mm:ss",
     *                       "finishTime": "hh:mm:ss",
     *                       }
     * @return 200 OK - if the current user's profile information was successfully saved
     * 404 NOT FOUND - if there is no user logged in
     * 400 BAD REQUEST - if the provided data is invalid
     */
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

    /**
     * GET "/common/search_patient_by_name?firstName=&lastName="
     *
     * @param firstName the first name of the patient
     * @param lastName  the last name of the patient
     * @return 200 OK - Along with the list of patients who have a partially/fully matching name
     */
    @GetMapping(UrlAddressCatalogue.COMMON_SEARCH_PATIENT_BY_NAME)
    public ResponseEntity searchPatientByName(@RequestParam String firstName, @RequestParam String lastName) {
        return ResponseEntity.ok().body(patientService.findByName(firstName, lastName));
    }

    /**
     * GET "/doctor/appointment/id?appointmentId="
     *
     * @param appointmentId the id of the appointment
     * @return 200 OK - if there is an appointment with the given id
     * 404 NOT FOUND if no appointment exists with the given id
     */
    @GetMapping(UrlAddressCatalogue.DOCTOR_GET_APPOINTMENT_BY_ID)
    public ResponseEntity getAppointmentById(@RequestParam Integer appointmentId) {
        Optional<AppointmentDTO> appointment = appointmentService.findById(appointmentId);
        if (appointment.isPresent()) {
            return ResponseEntity.ok().body(appointment.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new EntityNotFoundException(APPOINTMENT_NOT_FOUND_BY_ID_ERR_MSG));
        }
    }

    /**
     * GET "/doctor/appointments/past"
     *
     * @return 200 OK - if the currently logged in user is a doctor
     * 404 NOT FOUND - if the currently logged in user doesn't  have an associated doctor profile
     */
    @GetMapping(UrlAddressCatalogue.DOCTOR_GET_PAST_APPOINTMENTS)
    public ResponseEntity getPastAppointments() {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            return ResponseEntity.ok().body(appointmentService.findPastAppointmentsOfDoctor(currentUserAccount.getId()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    /**
     * GET "/doctor/appointments/past"
     *
     * @return 200 OK - if the currently logged in user is a doctor
     * 404 NOT FOUND - if the currently logged in user doesn't  have an associated doctor profile
     */
    @GetMapping(UrlAddressCatalogue.DOCTOR_GET_PRESENT_APPOINTMENTS)
    public ResponseEntity getPresentAppointments() {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            return ResponseEntity.ok().body(appointmentService.findPresentAppointmentsOfDoctor(currentUserAccount.getId()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    /**
     * GET "/doctor/appointments/past"
     *
     * @return 200 OK - if the currently logged in user is a doctor
     * 404 NOT FOUND - if the currently logged in user doesn't  have an associated doctor profile
     */
    @GetMapping(UrlAddressCatalogue.DOCTOR_GET_FUTURE_APPOINTMENTS)
    public ResponseEntity getFutureAppointments() {
        try {
            Account currentUserAccount = Utils.getCurrentUserAccount();
            return ResponseEntity.ok().body(appointmentService.findFutureAppointmentsOfDoctor(currentUserAccount.getId()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    /**
     * POST "/doctor/prescription/new?appointmentId="
     *
     * @param appointmentId   the id of the appointment to which the prescription is made
     * @param prescriptionDTO teh details of the prescription
     *                        ex. body {
     *                        "medication": "example_medication",
     *                        "indications": "example_indications",
     *                        }
     * @return 201 CREATED - if the currently logged in user is a doctor
     * 404 NOT FOUND - if the currently logged in user doesn't  have an associated doctor profile
     */
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

    /**
     * GET "/doctor/patient_prescriptions?firstName=&lastName="
     *
     * @param firstName the patient's first name
     * @param lastName  the patient's last name
     * @return 200 OK - if the patient was found by the given name
     * 404 NOT FOUND - if no patient was found by the given name
     */
    @GetMapping(UrlAddressCatalogue.DOCTOR_GET_PATIENTS_PRESCRIPTIONS)
    public ResponseEntity getPatientsPastPrescriptions(@RequestParam String firstName, @RequestParam String lastName) {
        try {
            return ResponseEntity.ok().body(prescriptionService.findByPatientName(firstName, lastName));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }


    /**
     * GET "/common/specialties/all"
     *
     * @return 200 OK - the list of specialties is retrieved
     */
    @GetMapping(UrlAddressCatalogue.COMMON_GET_ALL_SPECIALTIES)
    public ResponseEntity getAllSpecialties() {
        return ResponseEntity.ok().body(doctorService.getAllSpecialties());
    }
}
