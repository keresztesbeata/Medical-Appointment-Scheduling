package src.service.api;

import src.dto.PrescriptionDTO;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidStateException;

import java.util.List;
import java.util.Optional;

public interface PrescriptionService {
    /**
     * Add a prescription for a patient following a consultation.
     * @param appointmentId the id of the appointment after which the prescription is given
     * @param prescriptionDTO the details of the prescription
     * @throws InvalidStateException if the appointment is not yet completed, in which case no prescriptions can be given
     */
    void addPrescription(Integer appointmentId, PrescriptionDTO prescriptionDTO) throws InvalidStateException, EntityNotFoundException;

    Optional<PrescriptionDTO> findByAppointmentId(Integer appointmentId) throws EntityNotFoundException;

    List<PrescriptionDTO> findByPatientName(String firstName, String lastName) throws EntityNotFoundException;

    List<PrescriptionDTO> findByPatientId(Integer id) throws EntityNotFoundException;

    void exportPrescription(Integer appointmentId) throws EntityNotFoundException;
}
