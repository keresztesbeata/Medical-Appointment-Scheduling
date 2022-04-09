package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.model.users.ReceptionistProfile;

@Repository
public interface ReceptionistRepository extends JpaRepository<ReceptionistProfile, Integer> {
}
