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
import src.model.users.User;
import src.model.users.UserProfile;
import src.repository.PatientRepository;
import src.repository.UserProfileRepository;
import src.repository.UserRepository;
import src.service.api.UserProfileService;
import src.validator.DataValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class PatientServiceImpl implements UserProfileService<PatientProfileDTO> {
    private static final String USER_NOT_FOUND_ERR_MSG = "No user account was found by the given id!";
    private static final String DUPLICATE_EMAIL_ERR_MSG = "Duplicate email!";

    private DataMapper<PatientProfile, PatientProfileDTO> dataMapper = new PatientMapper();
    private DataValidator<PatientProfileDTO> validator = new DataValidator<>();
    @Autowired
    private PatientRepository dataRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public PatientProfileDTO saveProfile(Integer id, PatientProfileDTO userProfileDTO) throws InvalidDataException, EntityNotFoundException, DuplicateDataException {
        User user = userRepository.findByAccountId(id)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_ERR_MSG));
        UserProfile userProfile = user.getUserProfile();

        userProfileDTO.setFirstName(userProfile.getFirstName());
        userProfileDTO.setFirstName(userProfile.getLastName());

        validator.validate(userProfileDTO);

        if(dataRepository.findByEmail(userProfileDTO.getEmail()).isPresent()) {
            throw new DuplicateDataException(DUPLICATE_EMAIL_ERR_MSG);
        }

        PatientProfile specificUserProfile = dataMapper.mapToEntity(userProfileDTO);
        specificUserProfile.setId(userProfile.getId());

        dataRepository.save(specificUserProfile);

        log.info("saveProfile: The user " + userProfileDTO.getFirstName() + " " + userProfileDTO.getLastName() + "'s profile has been successfully updated!");

        return userProfileDTO;
    }

    @Override
    public Optional<PatientProfileDTO> findById(Integer id) {
        return (dataRepository.findById(id)).map(dataMapper::mapToDto);
    }

    @Override
    public List<PatientProfileDTO> findByName(String firstName, String lastName) {
        boolean firstNamePresent = firstName == null || firstName.isEmpty();
        boolean lastNamePresent = lastName == null || lastName.isEmpty();

        if (firstNamePresent && lastNamePresent) {
            return filterByIdList(userProfileRepository.findByFirstNameContainsAndLastNameContains(firstName, lastName));
        } else if (firstNamePresent) {
            return filterByIdList(userProfileRepository.findByFirstNameContains(firstName));
        } else if (lastNamePresent) {
            return filterByIdList(userProfileRepository.findByLastNameContains(lastName));
        } else {
            return dataRepository.findAll()
                    .stream()
                    .map(dataMapper::mapToDto)
                    .collect(Collectors.toList());
        }
    }

    private List<PatientProfileDTO> filterByIdList(List<UserProfile> userProfiles) {
        return userProfiles.stream()
                .filter(userProfile -> dataRepository.findById(userProfile.getId()).isPresent())
                .map(userProfile -> dataMapper.mapToDto(dataRepository.getById(userProfile.getId())))
                .collect(Collectors.toList());
    }
}
