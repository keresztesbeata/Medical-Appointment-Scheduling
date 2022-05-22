package src.service.api;

import org.springframework.stereotype.Service;
import src.dto.AppointmentDTO;
import src.dto.DoctorProfileDTO;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidAccessException;
import src.exceptions.InvalidDataException;
import src.exceptions.InvalidStateException;
import src.model.MedicalService;
import src.model.users.Account;
import src.service.impl.schedule.SchedulingType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Provides functionalities for updating and managing appointments.
 */
@Service
public interface AppointmentService {
    /**
     * Create a new appointment after the patient has requested it.
     * @param patientId the id of the patient
     * @param appointmentDTO the details of the appointment to be created such as the patient's name, the doctor's name, the type of requested service, etc.
     * @throws InvalidDataException if some data is missing or has an invalid value
     * @throws EntityNotFoundException if either the doctor, patient or the medical service cannot be found in the database (non-existent)
     */
    void create(Integer patientId, AppointmentDTO appointmentDTO) throws InvalidDataException, EntityNotFoundException;

    AppointmentDTO schedule(Integer appointmentId, LocalDateTime dateTime) throws InvalidDataException, EntityNotFoundException;

    void changeSchedulingStrategy(String schedulingType);

    void updateStatus(Integer appointmentId, Account account, String newStatus) throws InvalidStateException, EntityNotFoundException, InvalidAccessException;

    List<String> findAllMedicalServices();

    List<AppointmentDTO> findAllAppointmentsByStatus(String status);

    List<AppointmentDTO> findAllAppointmentsOfPatient(String firstName, String lastName) throws EntityNotFoundException;

    List<AppointmentDTO> findPastAppointmentsOfPatient(Integer patientId) throws EntityNotFoundException;

    List<AppointmentDTO> findUpcomingAppointmentsOfPatient(Integer patientId) throws EntityNotFoundException;

    List<AppointmentDTO> filterByDoctorAndStatus(Integer doctorId, String appointmentStatus) throws EntityNotFoundException;

    List<AppointmentDTO> findAllAppointmentsOfDoctor(String firstName, String lastName) throws EntityNotFoundException;

    List<AppointmentDTO> findAppointmentsOfDoctorByDate(Integer doctorId, LocalDate date) throws EntityNotFoundException;

    List<LocalDateTime> findAvailableDates(String firstName, String lastName, String medicalService) throws EntityNotFoundException ;

    List<DoctorProfileDTO> findDoctorsByMedicalService(String medicalServiceName) throws EntityNotFoundException;

    List<AppointmentDTO> filterByPatientAndStatus(Integer patientId, String appointmentStatus) throws EntityNotFoundException;
}
