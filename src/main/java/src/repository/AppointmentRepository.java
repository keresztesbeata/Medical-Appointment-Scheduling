package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import src.model.Appointment;
import src.model.AppointmentStatus;
import src.model.users.DoctorProfile;
import src.model.users.PatientProfile;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByPatient(PatientProfile patient);

    List<Appointment> findByPatientAndAppointmentDateBefore(PatientProfile patient, LocalDateTime appointmentDate);

    List<Appointment> findByPatientAndAppointmentDateAfter(PatientProfile patient, LocalDateTime appointmentDate);

    List<Appointment> findByDoctor(DoctorProfile doctorProfile);

    List<Appointment> findByStatus(AppointmentStatus status);

    List<Appointment> findByDoctorAndAppointmentDateAfter(DoctorProfile doctor, LocalDateTime appointmentDate);

    List<Appointment> findByDoctorAndAppointmentDateBefore(DoctorProfile doctor, LocalDateTime appointmentDate);

    List<Appointment> findByDoctorAndAppointmentDateBetween(DoctorProfile doctor, LocalDateTime start, LocalDateTime end);

    List<Appointment> findByPatientAndStatus(PatientProfile patient, AppointmentStatus status);

    List<Appointment> findByDoctorAndStatus(DoctorProfile doctor, AppointmentStatus status);
}
