package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.model.users.PatientProfile;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<PatientProfile, Integer> {
    Optional<PatientProfile> findByEmail(String email);

    List<PatientProfile> findByLastNameContains(String lastName);

    List<PatientProfile> findByFirstNameContains(String lastName);

    List<PatientProfile> findByFirstNameContainsAndLastNameContains(String firstName, String lastName);
}
