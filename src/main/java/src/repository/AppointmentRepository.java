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

    @Transactional
    @Query("select appointment from Appointment appointment where appointment.doctor = ?1 and date(appointment.appointmentDate) = ?2")
    List<Appointment> findByDoctorAndAppointmentDate(DoctorProfile doctor, LocalDate appointmentDate);

    List<Appointment> findByPatientAndStatus(PatientProfile patient, AppointmentStatus status);

    List<Appointment> findByDoctorAndStatus(DoctorProfile doctor, AppointmentStatus status);
}
