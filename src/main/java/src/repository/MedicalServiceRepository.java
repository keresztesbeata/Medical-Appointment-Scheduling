package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.model.MedicalService;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface MedicalServiceRepository extends JpaRepository<MedicalService, Integer> {
    Optional<MedicalService> findByName(String name);
}
