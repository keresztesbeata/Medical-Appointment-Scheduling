package src.service;

import org.springframework.stereotype.Service;
import src.dto.AccountDTO;
import src.dto.UserProfileDTO;
import src.exceptions.DuplicateDataException;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidDataException;

@Service
public interface AccountService {
    UserProfileDTO login(AccountDTO accountDTO) throws InvalidDataException, EntityNotFoundException;

    void register(UserProfileDTO userProfileDTO) throws InvalidDataException, DuplicateDataException;
}
