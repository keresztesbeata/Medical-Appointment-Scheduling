package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import src.model.users.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select user from User user left join user.account account where account.id = ?1")
    Optional<User> findByAccountId(Integer accountId);
}
