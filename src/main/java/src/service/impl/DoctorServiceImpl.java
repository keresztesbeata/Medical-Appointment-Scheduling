package src.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.dto.DoctorProfileDTO;
import src.exceptions.DuplicateDataException;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidDataException;
import src.mapper.DataMapper;
import src.mapper.DoctorMapper;
import src.model.users.DoctorProfile;
import src.model.users.User;
import src.model.users.UserProfile;
import src.repository.DoctorRepository;
import src.repository.UserProfileRepository;
import src.repository.UserRepository;
import src.service.api.UserProfileService;
import src.validator.DataValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class DoctorServiceImpl implements UserProfileService<DoctorProfileDTO> {
    private static final String USER_NOT_FOUND_ERR_MSG = "No user account was found by the given id!";

    private DataMapper<DoctorProfile, DoctorProfileDTO> dataMapper = new DoctorMapper();
    private DataValidator<DoctorProfileDTO> validator = new DataValidator<>();
    @Autowired
    private DoctorRepository dataRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public DoctorProfileDTO saveProfile(Integer id, DoctorProfileDTO userProfileDTO) throws InvalidDataException, EntityNotFoundException, DuplicateDataException {
        User user = userRepository.findByAccountId(id)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_ERR_MSG));
        UserProfile userProfile = user.getUserProfile();

        userProfileDTO.setFirstName(userProfile.getFirstName());
        userProfileDTO.setFirstName(userProfile.getLastName());

        validator.validate(userProfileDTO);

        DoctorProfile specificUserProfile = dataMapper.mapToEntity(userProfileDTO);
        specificUserProfile.setId(userProfile.getId());

        dataRepository.save(specificUserProfile);

        log.info("saveProfile: The user " + userProfileDTO.getFirstName() + " " + userProfileDTO.getLastName() + "'s profile has been successfully updated!");

        return userProfileDTO;
    }

    @Override
    public Optional<DoctorProfileDTO> findById(Integer id) {
        return (dataRepository.findById(id)).map(dataMapper::mapToDto);
    }

    @Override
    public List<DoctorProfileDTO> findByName(String firstName, String lastName) {
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

    private List<DoctorProfileDTO> filterByIdList(List<UserProfile> userProfiles) {
        return userProfiles.stream()
                .filter(userProfile -> dataRepository.findById(userProfile.getId()).isPresent())
                .map(userProfile -> dataMapper.mapToDto(dataRepository.getById(userProfile.getId())))
                .collect(Collectors.toList());
    }
}
