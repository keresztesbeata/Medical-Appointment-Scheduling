package src.service.impl.schedule;

import src.model.Appointment;
import src.model.MedicalService;
import src.model.users.DoctorProfile;

import java.time.LocalDateTime;
import java.util.List;

public interface SchedulingStrategy {
    List<LocalDateTime> findAvailableSpots(MedicalService medicalService, DoctorProfile doctorProfile, List<Appointment> existingAppointments);
}
