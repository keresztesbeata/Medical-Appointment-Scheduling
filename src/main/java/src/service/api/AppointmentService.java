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
import src.service.impl.schedule.SchedulingStrategy;
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
     * @param appointmentDTO the details of the appointment to be created such as the patient's name, the doctor's name, the type of requested service, etc.
     * @throws InvalidDataException if some of the data is missing or has an invalid value
     * @throws EntityNotFoundException if either the doctor, patient or the medical service cannot be found in the database (non-existent)
     */
    void create(AppointmentDTO appointmentDTO) throws InvalidDataException, EntityNotFoundException;

    /**
     *
     * @param appointmentId
     * @param dateTime
     * @return
     * @throws InvalidDataException
     * @throws EntityNotFoundException
     */
    AppointmentDTO schedule(Integer appointmentId, LocalDateTime dateTime) throws InvalidDataException, EntityNotFoundException;

    void changeSchedulingStrategy(SchedulingType schedulingType);

    /**
     *
     * @param appointmentId
     * @param account
     * @param newStatus
     * @throws InvalidStateException
     * @throws EntityNotFoundException
     * @throws InvalidAccessException
     */
    void updateStatus(Integer appointmentId, Account account, String newStatus) throws InvalidStateException, EntityNotFoundException, InvalidAccessException;

    /**
     *
     * @param patientId
     * @return
     * @throws EntityNotFoundException
     */
    List<AppointmentDTO> findByPatient(Integer patientId) throws EntityNotFoundException;

    /**
     *
     * @param patientId
     * @param untilDate
     * @return
     * @throws EntityNotFoundException
     */
    List<AppointmentDTO> findByPatientAndDateUntil(Integer patientId, LocalDate untilDate) throws EntityNotFoundException;

    /**
     *
     * @param patientId
     * @param toDate
     * @return
     * @throws EntityNotFoundException
     */
    List<AppointmentDTO> findByPatientAndDateUpTo(Integer patientId, LocalDate toDate) throws EntityNotFoundException;

    /**
     *
     * @param doctorId
     * @return
     * @throws EntityNotFoundException
     */
    List<AppointmentDTO> findByDoctor(Integer doctorId) throws EntityNotFoundException;

    /**
     *
     * @param doctorId
     * @param localDate
     * @return
     * @throws EntityNotFoundException
     */
    List<AppointmentDTO> findByDoctorAndDate(Integer doctorId, LocalDate localDate) throws EntityNotFoundException;

    List<LocalDateTime> findAvailableDates(Integer doctorId, MedicalService medicalService) throws EntityNotFoundException ;

    List<DoctorProfileDTO> findDoctorsByMedicalService(String medicalServiceName) throws EntityNotFoundException;
}
