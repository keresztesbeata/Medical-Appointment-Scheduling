package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.model.MedicalService;

@Repository
public interface MedicalServiceRepository extends JpaRepository<MedicalService, Integer> {
}
