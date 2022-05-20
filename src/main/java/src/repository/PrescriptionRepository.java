package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import src.model.Appointment;
import src.model.Prescription;
import src.model.users.PatientProfile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    @Transactional
    Optional<Prescription> findByAppointment(Appointment appointment);

    @Transactional
    @Query("select prescription from Prescription prescription left join prescription.appointment appointment where appointment.patient = ?1")
    List<Prescription> findByPatient(PatientProfile patientProfile);
}
