package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.model.users.PatientProfile;

@Repository
public interface PatientRepository extends JpaRepository<PatientProfile, Integer> {
}
