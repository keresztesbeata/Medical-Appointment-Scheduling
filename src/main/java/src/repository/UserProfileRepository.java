package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.model.users.UserProfile;

import java.util.List;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    List<UserProfile> findByLastNameContains(String lastName);

    List<UserProfile> findByFirstNameContains(String lastName);

    List<UserProfile> findByFirstNameContainsAndLastNameContains(String firstName, String lastName);
}
