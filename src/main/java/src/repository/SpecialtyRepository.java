package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.model.Specialty;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Integer> {
}
