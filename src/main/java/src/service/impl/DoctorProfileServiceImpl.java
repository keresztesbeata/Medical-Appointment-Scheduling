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
import src.model.Specialty;
import src.model.users.DoctorProfile;
import src.repository.AccountRepository;
import src.repository.DoctorRepository;
import src.repository.SpecialtyRepository;
import src.service.api.UserProfileService;
import src.validator.DataValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class DoctorProfileServiceImpl implements UserProfileService<DoctorProfileDTO> {
    private static final String USER_NOT_FOUND_ERR_MSG = "No user account was found by the given id!";
    private static final String SPECIALTY_NOT_FOUND_ERR_MSG = "No such specialty!";
    private static final String DUPLICATE_NAME_ERR_MSG = "Duplicate name!";

    private DataMapper<DoctorProfile, DoctorProfileDTO> dataMapper = new DoctorMapper();
    private DataValidator<DoctorProfileDTO> validator = new DataValidator<>();
    @Autowired
    private DoctorRepository dataRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Override
    public DoctorProfileDTO saveProfile(Integer id, DoctorProfileDTO userProfileDTO) throws InvalidDataException, EntityNotFoundException, DuplicateDataException {
        if(accountRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException(USER_NOT_FOUND_ERR_MSG);
        }

        validator.validate(userProfileDTO);

        if(dataRepository.findById(id).isEmpty()) {
            if (dataRepository.findByFirstNameAndLastName(userProfileDTO.getFirstName(), userProfileDTO.getLastName()).isPresent()) {
                throw new DuplicateDataException(DUPLICATE_NAME_ERR_MSG);
            }
        }
        DoctorProfile doctorProfile = dataMapper.mapToEntity(userProfileDTO);

        Specialty specialty = specialtyRepository.findByName(userProfileDTO.getSpecialty())
                        .orElseThrow(() -> new EntityNotFoundException(SPECIALTY_NOT_FOUND_ERR_MSG));
        doctorProfile.setSpecialty(specialty);
        doctorProfile.setId(id);

        dataRepository.save(doctorProfile);

        log.info("saveProfile: The user " + userProfileDTO.getFirstName() + " " + userProfileDTO.getLastName() + "'s profile has been successfully updated!");

        return userProfileDTO;
    }

    @Override
    public List<DoctorProfileDTO> findAll() {
        return dataRepository.findAll()
                .stream()
                .map(dataMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DoctorProfileDTO> findById(Integer id) {
        return (dataRepository.findById(id)).map(dataMapper::mapToDto);
    }

    @Override
    public List<DoctorProfileDTO> findByName(String firstName, String lastName) {
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

    public List<String> getAllSpecialties() {
        return specialtyRepository.findAll()
                .stream()
                .map(Specialty::getName)
                .collect(Collectors.toList());
    }
}
