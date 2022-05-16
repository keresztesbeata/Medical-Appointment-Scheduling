package src.service.api;

import org.springframework.stereotype.Service;
import src.dto.AccountDTO;
import src.dto.UserProfileDTO;
import src.exceptions.DuplicateDataException;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidDataException;

import java.util.Optional;

@Service
public interface AccountService {
    UserProfileDTO login(AccountDTO accountDTO) throws InvalidDataException, EntityNotFoundException;

    void register(AccountDTO accountDTO) throws InvalidDataException, DuplicateDataException;

    Optional<AccountDTO> findAccountByUsername(String username);
}
