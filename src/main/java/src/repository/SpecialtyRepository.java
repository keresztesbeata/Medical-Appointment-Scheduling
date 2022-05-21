package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.model.Specialty;

import java.util.Optional;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Integer> {
    Optional<Specialty> findByName(String name);
}
