package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.model.Appointment;
import src.model.Prescription;

import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    Optional<Prescription> findByAppointment(Appointment appointment);
}
