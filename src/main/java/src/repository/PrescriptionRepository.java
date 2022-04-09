package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.model.Prescription;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
}
