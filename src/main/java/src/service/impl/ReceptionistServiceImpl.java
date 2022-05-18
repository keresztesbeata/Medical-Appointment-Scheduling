package src.service.impl;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.dto.ReceptionistProfileDTO;
import src.exceptions.DuplicateDataException;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidDataException;
import src.mapper.DataMapper;
import src.mapper.ReceptionistMapper;
import src.model.users.ReceptionistProfile;
import src.model.users.User;
import src.model.users.UserProfile;
import src.repository.ReceptionistRepository;
import src.repository.UserProfileRepository;
import src.repository.UserRepository;
import src.service.api.UserProfileService;
import src.validator.DataValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ReceptionistServiceImpl implements UserProfileService<ReceptionistProfileDTO> {
    private static final String USER_NOT_FOUND_ERR_MSG = "No user account was found by the given id!";
    private static final String DUPLICATE_EMAIL_ERR_MSG = "Duplicate email!";

    private DataMapper<ReceptionistProfile, ReceptionistProfileDTO> dataMapper = new ReceptionistMapper();
    private DataValidator<ReceptionistProfileDTO> validator = new DataValidator<>();
    @Autowired
    private ReceptionistRepository dataRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ReceptionistProfileDTO saveProfile(Integer id, ReceptionistProfileDTO userProfileDTO) throws InvalidDataException, EntityNotFoundException, DuplicateDataException {
        User user = userRepository.findByAccountId(id)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_ERR_MSG));
        UserProfile userProfile = user.getUserProfile();

        userProfileDTO.setFirstName(userProfile.getFirstName());
        userProfileDTO.setFirstName(userProfile.getLastName());

        validator.validate(userProfileDTO);

        if(dataRepository.findByEmail(userProfileDTO.getEmail()).isPresent()) {
            throw new DuplicateDataException(DUPLICATE_EMAIL_ERR_MSG);
        }

        ReceptionistProfile specificUserProfile = dataMapper.mapToEntity(userProfileDTO);
        specificUserProfile.setId(userProfile.getId());

        dataRepository.save(specificUserProfile);

        log.info("saveProfile: The user " + userProfileDTO.getFirstName() + " " + userProfileDTO.getLastName() + "'s profile has been successfully updated!");

        return userProfileDTO;
    }

    @Override
    public Optional<ReceptionistProfileDTO> findById(Integer id) {
        return (dataRepository.findById(id)).map(dataMapper::mapToDto);
    }

    @Override
    public List<ReceptionistProfileDTO> findByName(String firstName, String lastName) {
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

    private List<ReceptionistProfileDTO> filterByIdList(List<UserProfile> userProfiles) {
        return userProfiles.stream()
                .filter(userProfile -> dataRepository.findById(userProfile.getId()).isPresent())
                .map(userProfile -> dataMapper.mapToDto(dataRepository.getById(userProfile.getId())))
                .collect(Collectors.toList());
    }
}
