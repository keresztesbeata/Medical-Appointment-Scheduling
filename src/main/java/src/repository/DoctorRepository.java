package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.model.users.DoctorProfile;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<DoctorProfile, Integer> {

    List<DoctorProfile> findByLastNameContains(String lastName);

    List<DoctorProfile> findByFirstNameContains(String lastName);

    List<DoctorProfile> findByFirstNameContainsAndLastNameContains(String firstName, String lastName);

    Optional<DoctorProfile> findByFirstNameAndLastName(String firstName, String lastName);
}
