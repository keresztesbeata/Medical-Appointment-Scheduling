package src.service.api;

import org.springframework.stereotype.Service;
import src.dto.AccountDTO;
import src.exceptions.DuplicateDataException;
import src.exceptions.InvalidDataException;

import java.util.Optional;

@Service
public interface AccountService {

    /**
     * Register a new user account.
     *
     * @param accountDTO contains the credentials of the new user, such as the username, password and the type of account to be created.
     * @throws InvalidDataException   when either the username or the password is missing or invalid, or the user profile contains some invalid data
     * @throws DuplicateDataException when the username is already taken
     */
    void register(AccountDTO accountDTO) throws InvalidDataException, DuplicateDataException;

    /**
     * Retrieve a user's account by its username.
     *
     * @param username the name which uniquely identifies the user
     * @return the existing user's information wrapped in an optional
     */
    Optional<AccountDTO> findByUsername(String username);
}
