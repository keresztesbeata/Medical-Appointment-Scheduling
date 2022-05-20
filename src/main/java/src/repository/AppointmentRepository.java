package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import src.model.Appointment;
import src.model.users.DoctorProfile;
import src.model.users.PatientProfile;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Transactional
    List<Appointment> findByPatient(PatientProfile patient);
    @Transactional
    List<Appointment> findByPatientAndAppointmentDateBefore(PatientProfile patient, LocalDateTime appointmentDate);
    @Transactional
    List<Appointment> findByDoctor(DoctorProfile doctorProfile);
    @Transactional
    @Query("select appointment from Appointment appointment where appointment.doctor = ?1 and date(appointment.appointmentDate) = ?2")
    List<Appointment> findByDoctorAndAppointmentDate(DoctorProfile doctor, LocalDate appointmentDate);

    @Transactional
    @Query("select appointment from Appointment appointment where date(appointment.appointmentDate) = ?1")
    List<Appointment> findByAppointmentDate(LocalDate localDate);
}
