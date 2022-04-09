package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.model.users.DoctorProfile;

@Repository
public interface DoctorRepository extends JpaRepository<DoctorProfile, Integer> {
}
