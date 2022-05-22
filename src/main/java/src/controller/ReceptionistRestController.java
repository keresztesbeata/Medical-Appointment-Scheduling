package src.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidAccessException;
import src.exceptions.InvalidDataException;
import src.exceptions.InvalidStateException;
import src.model.AppointmentStatus;
import src.model.users.Account;
import src.service.api.AppointmentService;
import src.service.impl.AppointmentServiceImpl;
import src.service.impl.DoctorProfileServiceImpl;
import src.service.impl.PatientProfileServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@Log4j2
public class ReceptionistRestController {

    @Autowired
    private PatientProfileServiceImpl patientService;
    @Autowired
    private AppointmentServiceImpl appointmentService;
    @Autowired
    private DoctorProfileServiceImpl doctorService;

    @GetMapping(UrlAddressCatalogue.RECEPTIONIST_ALL_APPOINTMENT_STATUSES)
    public ResponseEntity getAllAppointmentStatuses() {
        return ResponseEntity.ok().body(Arrays.stream(AppointmentStatus.values()).map(Enum::name).collect(Collectors.toList()));
    }

    @GetMapping(UrlAddressCatalogue.RECEPTIONIST_GET_ALL_APPOINTMENTS_BY_STATUS)
    public ResponseEntity getAllAppointmentsByStatus(@RequestParam String status) {
        return ResponseEntity.ok().body(appointmentService.findAllAppointmentsByStatus(status));
    }

    @GetMapping(UrlAddressCatalogue.RECEPTIONIST_GET_ALL_APPOINTMENTS_OF_PATIENT)
    public ResponseEntity getAllAppointmentsOfPatient(@RequestParam String firstName, @RequestParam String lastName) {
        try {
            return ResponseEntity.ok().body(appointmentService.findAllAppointmentsOfPatient(firstName, lastName));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping(UrlAddressCatalogue.RECEPTIONIST_GET_ALL_APPOINTMENTS_OF_DOCTOR)
    public ResponseEntity getAllAppointmentsOfDoctor(@RequestParam String firstName, @RequestParam String lastName) {
        try {
            return ResponseEntity.ok().body(appointmentService.findAllAppointmentsOfDoctor(firstName, lastName));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping(UrlAddressCatalogue.RECEPTIONIST_GET_ALL_DOCTORS)
    public ResponseEntity getAllDoctors() {
        return ResponseEntity.ok().body(doctorService.findAll());
    }

    @GetMapping(UrlAddressCatalogue.RECEPTIONIST_GET_ALL_PATIENTS)
    public ResponseEntity getAllPatients() {
        return ResponseEntity.ok().body(patientService.findAll());
    }

    @GetMapping(UrlAddressCatalogue.RECEPTIONIST_GET_AVAILABLE_APPOINTMENTS_FOR_DOCTOR)
    public ResponseEntity getAvailableAppointmentsForDoctor(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String medicalService) {
        try {
            return ResponseEntity.ok().body(appointmentService.findAvailableDates(firstName, lastName, medicalService));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping(UrlAddressCatalogue.RECEPTIONIST_GET_APPOINTMENTS_OF_DOCTOR_BY_GIVEN_DATE)
    public ResponseEntity getAllAppointmentsForDoctorByDate(@RequestParam Integer doctorId, @RequestParam LocalDate localDate) {
        try {
            return ResponseEntity.ok().body(appointmentService.findAppointmentsOfDoctorByDate(doctorId, localDate));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @PostMapping(UrlAddressCatalogue.RECEPTIONIST_UPDATE_APPOINTMENT_STATE)
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

    @PostMapping(UrlAddressCatalogue.RECEPTIONIST_SCHEDULE_APPOINTMENT)
    public ResponseEntity scheduleAppointment(@RequestParam Integer appointmentId, @RequestParam LocalDateTime appointmentDate) {
        try {
            appointmentService.schedule(appointmentId, appointmentDate);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        } catch (InvalidDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    @PostMapping(UrlAddressCatalogue.RECEPTIONIST_CHANGE_SCHEDULING_STRATEGY)
    public ResponseEntity changeSchedulingStrategy(@RequestParam String strategy) {
        appointmentService.changeSchedulingStrategy(strategy);
        return ResponseEntity.ok().build();
    }
}
