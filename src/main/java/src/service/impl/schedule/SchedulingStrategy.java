package src.service.impl.schedule;

import src.model.Appointment;
import src.model.MedicalService;
import src.model.users.DoctorProfile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents the strategy used for scheduling the next appointment. Based on the Strategy design pattern, the concrete strategies which implement this interface,
 * will provide different ways for scheduling.
 */
public interface SchedulingStrategy {
    /**
     * Get the list of candidates for setting the date of the new appointment.
     * @param medicalService the type of the medical service which is needed to know its duration
     * @param doctorProfile the doctor's data to whom the appointment is assigned
     * @param existingAppointments the list of existing, 'confirmed' appointments of the given doctor
     * @return the list of possible date suggestions
     */
    List<LocalDateTime> findAvailableSpots(MedicalService medicalService, DoctorProfile doctorProfile, List<Appointment> existingAppointments);
}
