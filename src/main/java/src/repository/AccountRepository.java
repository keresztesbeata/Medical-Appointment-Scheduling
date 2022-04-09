package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.model.users.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

}
