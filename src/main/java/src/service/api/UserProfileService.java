package src.service.api;

import org.springframework.stereotype.Service;
import src.dto.PatientProfileDTO;
import src.dto.UserProfileDTO;
import src.exceptions.DuplicateDataException;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidDataException;

import java.util.List;
import java.util.Optional;

@Service
public interface UserProfileService<T extends UserProfileDTO> {
    /**
     * Update and save the user's profile information. If the user had no profile set up, then it will create a new one, otherwise it
     * will update the existing profile.
     * @param id the id of the user whose profile is updated.
     * @param userProfileDTO the new profile information
     * @return the updated and saved user profile info
     * @throws InvalidDataException    when the introduced data is invalid or missing some fields
     * @throws EntityNotFoundException if the user profile information is not accessible
     * @throws DuplicateDataException if any of the unique fields from the user profile's data are duplicated (there is already another user having the same data)
     */
    T saveProfile(Integer id, T userProfileDTO) throws InvalidDataException, EntityNotFoundException, DuplicateDataException;

    /**
     * Find profile of user by id.
     * @param id the id of the user
     * @return the user profile wrapped in an optional or an empty optional if it was not found
     */
    Optional<T> findById(Integer id);

    /**
     * Find profile of user by name. If multiple users exist with the same name, then all of them are returned.
     * The names do not have to be an exact match, some letters can be omitted, but the result will be more general (more matches will be found).
     * If either the first or last name is left empty then it is not taken into consideration. If both are missing then all the users are returned.
     * @param firstName the first (given) name of the user
     * @param lastName the last (family) name of the user
     * @return the profile of the users with the given name
     */
    List<T> findByName(String firstName, String lastName);
}
