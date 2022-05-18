package src.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import src.dto.AccountDTO;
import src.dto.UserProfileDTO;
import src.exceptions.DuplicateDataException;
import src.exceptions.InvalidDataException;
import src.mapper.AccountMapper;
import src.mapper.UserProfileMapper;
import src.model.users.Account;
import src.model.users.User;
import src.model.users.UserProfile;
import src.repository.AccountRepository;
import src.repository.PatientRepository;
import src.repository.UserProfileRepository;
import src.repository.UserRepository;
import src.service.api.AccountService;
import src.validator.DataValidator;

import java.util.Optional;

@Log4j2
@Service
public class AccountServiceImpl implements AccountService {
    private static final String DUPLICATE_USERNAME_ERROR_MESSAGE = "Duplicate username!\nThis username is already taken!";

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;

    private AccountMapper accountMapper = new AccountMapper();
    private UserProfileMapper userProfileMapper = new UserProfileMapper();
    private DataValidator<AccountDTO> accountValidator = new DataValidator<>();

    @Override
    public void register(AccountDTO accountDTO, UserProfileDTO userProfileDTO) throws InvalidDataException, DuplicateDataException {
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

        UserProfile userProfile = userProfileMapper.mapToEntity(userProfileDTO);
        // save the general information about the user's profile
        UserProfile savedUserProfile = userProfileRepository.save(userProfile);

        User user = new User();
        user.setAccount(savedAccount);
        user.setUserProfile(savedUserProfile);
        User savedUser = userRepository.save(user);

        log.info("register: The user " + account.getUsername() + "with id " + savedUser.getId() + " having role " + account.getAccountType().name() + " has been successfully added!");
    }

    @Override
    public Optional<AccountDTO> findByUsername(String username) {
        return accountRepository.findByUsername(username)
                .map(accountMapper::mapToDto);
    }
}
