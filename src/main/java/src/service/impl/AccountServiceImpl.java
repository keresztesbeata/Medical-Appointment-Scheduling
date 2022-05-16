package src.service.impl;

import src.dto.AccountDTO;
import src.dto.UserProfileDTO;
import src.exceptions.DuplicateDataException;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidDataException;
import src.service.api.AccountService;

import java.util.Optional;

public class AccountServiceImpl implements AccountService {
    @Override
    public UserProfileDTO login(AccountDTO accountDTO) throws InvalidDataException, EntityNotFoundException {
        return null;
    }

    @Override
    public void register(AccountDTO accountDTO) throws InvalidDataException, DuplicateDataException {

    }

    @Override
    public Optional<AccountDTO> findAccountByUsername(String username) {
        return Optional.empty();
    }
}
