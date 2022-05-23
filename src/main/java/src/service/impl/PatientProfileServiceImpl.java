package src.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.dto.PatientProfileDTO;
import src.exceptions.DuplicateDataException;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidDataException;
import src.mapper.DataMapper;
import src.mapper.PatientMapper;
import src.model.users.PatientProfile;
import src.repository.AccountRepository;
import src.repository.PatientRepository;
import src.service.api.UserProfileService;
import src.validator.DataValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class PatientProfileServiceImpl implements UserProfileService<PatientProfileDTO> {
    private static final String USER_NOT_FOUND_ERR_MSG = "No user account was found by the given id!";
    private static final String DUPLICATE_EMAIL_ERR_MSG = "Duplicate email!";
    private static final String DUPLICATE_PHONE_NUMBER_ERR_MSG = "Duplicate phone number!";
    private static final String DUPLICATE_NAME_ERR_MSG = "Duplicate name!";

    private DataMapper<PatientProfile, PatientProfileDTO> dataMapper = new PatientMapper();
    private DataValidator<PatientProfileDTO> validator = new DataValidator<>();
    @Autowired
    private PatientRepository dataRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public PatientProfileDTO saveProfile(Integer id, PatientProfileDTO userProfileDTO) throws InvalidDataException, EntityNotFoundException, DuplicateDataException {
        if (accountRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException(USER_NOT_FOUND_ERR_MSG);
        }

        validator.validate(userProfileDTO);

        if (dataRepository.findById(id).isEmpty()) {
            // set up a new user profile and check for duplicate emails
            if (dataRepository.findByEmail(userProfileDTO.getEmail()).isPresent()) {
                throw new DuplicateDataException(DUPLICATE_EMAIL_ERR_MSG);
            }
            if (dataRepository.findByPhone(userProfileDTO.getPhone()).isPresent()) {
                throw new DuplicateDataException(DUPLICATE_PHONE_NUMBER_ERR_MSG);
            }
            if (dataRepository.findByFirstNameAndLastName(userProfileDTO.getFirstName(), userProfileDTO.getLastName()).isPresent()) {
                throw new DuplicateDataException(DUPLICATE_NAME_ERR_MSG);
            }
        }

        PatientProfile patientProfile = dataMapper.mapToEntity(userProfileDTO);
        patientProfile.setId(id);

        dataRepository.save(patientProfile);
        log.info("saveProfile: The user " + userProfileDTO.getFirstName() + " " + userProfileDTO.getLastName() + "'s profile has been successfully updated!");

        return userProfileDTO;
    }

    @Override
    public List<PatientProfileDTO> findAll() {
        return dataRepository.findAll()
                .stream()
                .map(dataMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PatientProfileDTO> findById(Integer id) {
        return (dataRepository.findById(id)).map(dataMapper::mapToDto);
    }

    @Override
    public List<PatientProfileDTO> findByName(String firstName, String lastName) {
        boolean firstNamePresent = firstName != null && !firstName.isEmpty();
        boolean lastNamePresent = lastName != null && !lastName.isEmpty();

        if (firstNamePresent && lastNamePresent) {
            return dataRepository.findByFirstNameContainsAndLastNameContains(firstName, lastName)
                    .stream()
                    .map(dataMapper::mapToDto)
                    .collect(Collectors.toList());
        } else if (firstNamePresent) {
            return dataRepository.findByFirstNameContains(firstName)
                    .stream()
                    .map(dataMapper::mapToDto)
                    .collect(Collectors.toList());
        } else if (lastNamePresent) {
            return dataRepository.findByLastNameContains(lastName)
                    .stream()
                    .map(dataMapper::mapToDto)
                    .collect(Collectors.toList());
        } else {
            return dataRepository.findAll()
                    .stream()
                    .map(dataMapper::mapToDto)
                    .collect(Collectors.toList());
        }
    }
}
