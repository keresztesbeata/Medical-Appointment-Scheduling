package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.model.users.PatientProfile;
import src.model.users.ReceptionistProfile;

import java.util.Optional;

@Repository
public interface ReceptionistRepository extends JpaRepository<ReceptionistProfile, Integer> {
    Optional<PatientProfile> findByEmail(String email);
}
