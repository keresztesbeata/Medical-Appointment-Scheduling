package src.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import src.dto.AccountDTO;
import src.exceptions.DuplicateDataException;
import src.exceptions.InvalidDataException;
import src.mapper.AccountMapper;
import src.model.users.Account;
import src.repository.AccountRepository;
import src.service.api.AccountService;
import src.validator.DataValidator;

import java.util.Optional;

@Log4j2
@Service
public class AccountServiceImpl implements AccountService {
    private static final String DUPLICATE_USERNAME_ERROR_MESSAGE = "Duplicate username!\nThis username is already taken!";

    @Autowired
    private AccountRepository accountRepository;

    private AccountMapper accountMapper = new AccountMapper();
    private DataValidator<AccountDTO> accountValidator = new DataValidator<>();

    @Override
    public void register(AccountDTO accountDTO) throws InvalidDataException, DuplicateDataException {
        accountValidator.validate(accountDTO);

        if (accountRepository.findByUsername(accountDTO.getUsername()).isPresent()) {
            throw new DuplicateDataException(DUPLICATE_USERNAME_ERROR_MESSAGE);
        }

        // create an account instance with the given id and the other related info
        Account account = accountMapper.mapToEntity(accountDTO);

        // encode the password before saving it in the database
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(account.getPassword());
        account.setPassword(encodedPassword);
        // save the account related information with the new id
        Account savedAccount = accountRepository.save(account);

        log.info("register: The user " + savedAccount.getUsername() + "with id " + savedAccount.getId() + " having role " + account.getAccountType().name() + " has been successfully added!");
    }

    @Override
    public Optional<AccountDTO> findByUsername(String username) {
        return accountRepository.findByUsername(username)
                .map(accountMapper::mapToDto);
    }
}
