package src.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import src.dto.AccountDTO;
import src.dto.UserProfileDTO;
import src.exceptions.DuplicateDataException;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidDataException;
import src.mapper.AccountMapper;
import src.mapper.UserProfileMapper;
import src.model.users.Account;
import src.model.users.PatientProfile;
import src.model.users.User;
import src.model.users.UserProfile;
import src.repository.AccountRepository;
import src.repository.UserProfileRepository;
import src.repository.UserRepository;
import src.service.api.UserService;
import src.validator.DataValidator;

import java.util.Optional;

@Log4j2
@Service
public class UserServiceImpl implements UserService {
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
    public UserProfileDTO register(AccountDTO accountDTO, UserProfileDTO userProfileDTO) throws InvalidDataException, DuplicateDataException {
        accountValidator.validate(accountDTO);

        if (accountRepository.findByUsername(accountDTO.getUsername()).isPresent()) {
            throw new DuplicateDataException(DUPLICATE_USERNAME_ERROR_MESSAGE);
        }

        // create an id for the new user by creating a new user entry in the 'Users' table
        User savedUser = userRepository.save(new User());

        // create an account instance with the given id and the other related info
        Account account = accountMapper.mapToEntity(accountDTO);
        account.setId(savedUser.getId());

        // encode the password before saving it in the database
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(account.getPassword());
        account.setPassword(encodedPassword);
        // save the account related information with the new id
        Account savedAccount = accountRepository.save(account);

        // create a user profile instance with the given id and the other related info
        UserProfile userProfile = userProfileMapper.mapToEntity(userProfileDTO);
        userProfile.setId(savedUser.getId());
        // save the profile related information with the new id
        UserProfile savedProfile = userProfileRepository.save(userProfile);

        savedUser.setAccount(savedAccount);
        savedUser.setUserProfile(savedProfile);
        savedUser = userRepository.save(savedUser);

        log.info("addUser: The user " + account.getUsername() + " with role " + account.getAccountType().name() + " has been successfully added!");
        return userProfileMapper.mapToDto(savedProfile);
    }

    @Override
    public UserProfileDTO updateProfile(UserProfileDTO userProfileDTO) throws InvalidDataException, EntityNotFoundException {
        return null;
    }

    @Override
    public Optional<AccountDTO> findAccountByUsername(String username) {
        return accountRepository.findByUsername(username)
                .map(accountMapper::mapToDto);
    }
}
