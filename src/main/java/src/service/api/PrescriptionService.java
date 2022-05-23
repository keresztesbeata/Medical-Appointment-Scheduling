package src.service.api;

import src.dto.PrescriptionDTO;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidStateException;

import java.util.List;
import java.util.Optional;

public interface PrescriptionService {
    /**
     * Add a prescription for a patient following a consultation.
     *
     * @param appointmentId   the id of the appointment after which the prescription is given
     * @param prescriptionDTO the details of the prescription
     * @throws InvalidStateException if the appointment is not yet completed, in which case no prescriptions can be given
     */
    void addPrescription(Integer appointmentId, PrescriptionDTO prescriptionDTO) throws InvalidStateException, EntityNotFoundException;

    /**
     * Get a prescription based on the appointment to which it was added. There is a 1-to-1 relationship between appointments and prescriptions, and they share the same id.
     *
     * @param appointmentId the id of the appointment which is the same as the id of the prescription
     * @return the prescription if it was found, wrapped in an optional or an empty optional
     * @throws EntityNotFoundException if no appointment exists with the given id
     */
    Optional<PrescriptionDTO> findByAppointmentId(Integer appointmentId) throws EntityNotFoundException;

    /**
     * Get the prescriptions assigned for a given patient based on its name.
     *
     * @param firstName the first name of the patient
     * @param lastName  the last name of the patient
     * @return the list of prescriptions of the given patient
     * @throws EntityNotFoundException if no patient was found by the given name
     */
    List<PrescriptionDTO> findByPatientName(String firstName, String lastName) throws EntityNotFoundException;

    /**
     * Get the prescriptions assigned for a given patient based on its id.
     *
     * @param id the id of the patient
     * @return the list of prescriptions of the given patient
     * @throws EntityNotFoundException if no patient was found by the given id
     */
    List<PrescriptionDTO> findByPatientId(Integer id) throws EntityNotFoundException;

    /**
     * Export a prescription, by saving it as a pdf.
     *
     * @param appointmentId the id of the appointment for which the prescription was made
     * @throws EntityNotFoundException if no appointment exists with the given id
     */
    void exportPrescription(Integer appointmentId) throws EntityNotFoundException;
}
