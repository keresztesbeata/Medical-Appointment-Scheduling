package src.service.api;

import org.springframework.stereotype.Service;
import src.dto.AppointmentDTO;
import src.dto.DoctorProfileDTO;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidAccessException;
import src.exceptions.InvalidDataException;
import src.exceptions.InvalidStateException;
import src.model.users.Account;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Provides functionalities for updating and managing appointments.
 */
@Service
public interface AppointmentService {
    /**
     * Create a new appointment after the patient has requested it.
     *
     * @param patientId      the id of the patient
     * @param appointmentDTO the details of the appointment to be created such as the patient's name, the doctor's name, the type of requested service, etc.
     * @throws InvalidDataException    if some data is missing or has an invalid value
     * @throws EntityNotFoundException if either the doctor, patient or the medical service cannot be found in the database (non-existent)
     */
    void create(Integer patientId, AppointmentDTO appointmentDTO) throws InvalidDataException, EntityNotFoundException;

    /**
     * Schedule an appointment by setting the appointment date. This is the responsibility of the receptionist.
     *
     * @param appointmentId the id of the appointment
     * @param dateTime      the date and time when the appointment will be scheduled
     * @return the appointment containing the new date and with updated status 'Scheduled'
     * @throws InvalidDataException    if the appointmentDate is missing or invalid, it is before the current date, as appointments cannot be scheduled in the past
     * @throws EntityNotFoundException if no appointment exists with the given id
     */
    AppointmentDTO schedule(Integer appointmentId, LocalDateTime dateTime) throws InvalidDataException, EntityNotFoundException;

    /**
     * Allows switching between the type of strategy used for scheduling appointments and selecting the most appropriate dates.
     *
     * @param schedulingType the type of the selected strategy
     */
    void changeSchedulingStrategy(String schedulingType);

    /**
     * Update the status of the appointment.
     *
     * @param appointmentId the id of the appointment
     * @param account       the account of the user who wants to perform the update (only the Patient and Receptionist have the right to perform this operation)
     * @param newStatus     the status to be changed to
     * @throws InvalidStateException   if the status of the appointment cannot be changed to the new status given its current state, because of the predefined state transition flow for managing appointments
     * @throws EntityNotFoundException if no appointment exists with the given id
     * @throws InvalidAccessException  if the user who attempts to modify the status of the appointment doesn't have the right to do it. For ex Patients and Receptionist can only request certain state transitions.
     */
    void updateStatus(Integer appointmentId, Account account, String newStatus) throws InvalidStateException, EntityNotFoundException, InvalidAccessException;

    /**
     * Get an appointment based on its id.
     *
     * @param appointmentId the id of the appointment
     * @return the appointment wrapped in an optional if it was found, otherwise an empty optional.
     */
    Optional<AppointmentDTO> findById(Integer appointmentId);

    /**
     * Get all the medical services.
     *
     * @return list of medical service names.
     */
    List<String> findAllMedicalServices();

    /**
     * Get all the appointments filtered by status.
     *
     * @param status is the status based on which the appointments are selected
     * @return the list of appointments with the given status
     */
    List<AppointmentDTO> findAllAppointmentsByStatus(String status);

    /**
     * Get all the appointments of a patient based on its unique name.
     *
     * @param firstName the first name of the patient
     * @param lastName  the last name of the patient
     * @return the list of appointments made by the given patient
     * @throws EntityNotFoundException if no patient was found with the given name
     */
    List<AppointmentDTO> findAllAppointmentsOfPatient(String firstName, String lastName) throws EntityNotFoundException;

    /**
     * Get all the past appointments of a patient based on its unique id.
     *
     * @param patientId the id of the patient
     * @return the list of appointments made by the given patient
     * @throws EntityNotFoundException if no patient was found with the given id
     */
    List<AppointmentDTO> findPastAppointmentsOfPatient(Integer patientId) throws EntityNotFoundException;

    /**
     * Get all the future appointments of a patient based on its unique id.
     *
     * @param patientId the id of the patient
     * @return the list of appointments made by the given patient
     * @throws EntityNotFoundException if no patient was found with the given id
     */
    List<AppointmentDTO> findUpcomingAppointmentsOfPatient(Integer patientId) throws EntityNotFoundException;

    List<AppointmentDTO> filterByDoctorAndStatus(Integer doctorId, String appointmentStatus) throws EntityNotFoundException;

    /**
     * Get all appointments made at a given doctor based on its unique name.
     *
     * @param firstName the first name of the doctor
     * @param lastName  the last name of the doctor
     * @return the list of appointments made for a given doctor
     * @throws EntityNotFoundException if no doctor was found with the given name
     */
    List<AppointmentDTO> findAllAppointmentsOfDoctor(String firstName, String lastName) throws EntityNotFoundException;

    /**
     * Get all past appointments made at a given doctor based on its id.
     *
     * @param doctorId the first id of the doctor
     * @return the list of appointments made for a given doctor
     * @throws EntityNotFoundException if no doctor was found with the given id
     */
    List<AppointmentDTO> findPastAppointmentsOfDoctor(Integer doctorId) throws EntityNotFoundException;

    /**
     * Get all present appointments made at a given doctor based on its id.
     *
     * @param doctorId the first id of the doctor
     * @return the list of appointments made for a given doctor
     * @throws EntityNotFoundException if no doctor was found with the given id
     */
    List<AppointmentDTO> findPresentAppointmentsOfDoctor(Integer doctorId) throws EntityNotFoundException;

    /**
     * Get all future appointments made at a given doctor based on its id.
     *
     * @param doctorId the first id of the doctor
     * @return the list of appointments made for a given doctor
     * @throws EntityNotFoundException if no doctor was found with the given id
     */
    List<AppointmentDTO> findFutureAppointmentsOfDoctor(Integer doctorId) throws EntityNotFoundException;

    /**
     * Get a list of available dates for scheduling an appointment.
     *
     * @param doctorFirstName the first name of the doctor for which the appointment is made
     * @param doctorLastName  the last name of the doctor for which the appointment is made
     * @param medicalService  the name of the requested medical service
     * @return the list of available time slots
     * @throws EntityNotFoundException if no doctor was found with the given name
     */
    List<LocalDateTime> findAvailableDates(String doctorFirstName, String doctorLastName, String medicalService) throws EntityNotFoundException;

    /**
     * Get the list of doctors who are capable to provide the given medical service based on their specialty/field.
     *
     * @param medicalServiceName the name of the service
     * @return the list of doctors which qualify
     * @throws EntityNotFoundException if no medical service was found by the given name
     */
    List<DoctorProfileDTO> findDoctorsByMedicalService(String medicalServiceName) throws EntityNotFoundException;

    /**
     * Get a list of appointments made by a given patient and filtered by status.
     *
     * @param patientId         the id of the patient
     * @param appointmentStatus the status after which the appointments are filtered
     * @return the list of filtered appointments
     * @throws EntityNotFoundException if no patient was found with the given id
     */
    List<AppointmentDTO> filterByPatientAndStatus(Integer patientId, String appointmentStatus) throws EntityNotFoundException;
}
