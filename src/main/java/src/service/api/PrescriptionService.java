package src.service.api;

public interface PrescriptionService {
    /**
     * Add a prescription for a patient following a consultation.
     * @param appointmentId the id of the appointment after which the prescription is given
     */
    void addPrescription(Integer appointmentId);
}
