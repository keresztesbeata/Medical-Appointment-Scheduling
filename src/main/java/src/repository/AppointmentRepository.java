package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import src.model.Appointment;
import src.model.users.DoctorProfile;
import src.model.users.PatientProfile;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Transactional
    List<Appointment> findByPatient(PatientProfile patient);
    @Transactional
    List<Appointment> findByPatientAndAppointmentDateBefore(PatientProfile patient, LocalDate appointmentDate);
    @Transactional
    List<Appointment> findByDoctor(DoctorProfile doctorProfile);
    @Transactional
    List<Appointment> findByDoctorAndAppointmentDate(DoctorProfile doctor, LocalDate appointmentDate);
}
