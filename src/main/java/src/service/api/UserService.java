package src.service.api;

import org.springframework.stereotype.Service;
import src.dto.AccountDTO;
import src.dto.UserProfileDTO;
import src.exceptions.DuplicateDataException;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidDataException;

import java.util.Optional;

@Service
public interface UserService {

    /**
     * Register a new user account.
     *
     * @param accountDTO contains the credentials of the new user, such as the username, password and the type of account to be created.
     * @param userProfileDTO contains the specific data related to the user, for example the full name
     * @return the created user's profile with the relevant information
     * @throws InvalidDataException when either the username or the password is missing or invalid, or the user profile contains some invalid data
     * @throws DuplicateDataException when the username is already taken
     */
    UserProfileDTO register(AccountDTO accountDTO, UserProfileDTO userProfileDTO) throws InvalidDataException, DuplicateDataException;

    /**
     * Update the relevant user information about their profile.
     * @param userProfileDTO the new profile information
     * @return the updated and saved user profile info
     * @throws InvalidDataException when the introduced data is invalid or missing some fields
     * @throws EntityNotFoundException if the user profile information is not accessible b
     */
    UserProfileDTO updateProfile(UserProfileDTO userProfileDTO) throws InvalidDataException, EntityNotFoundException;

    /**
     * Retrieve a user's account by its username.
     *
     * @param username the name which uniquely identifies the user
     * @return the existing user's information wrapped in an optional
     */
    Optional<AccountDTO> findAccountByUsername(String username);
}