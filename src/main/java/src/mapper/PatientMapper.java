package src.mapper;

import src.dto.PatientProfileDTO;
import src.model.users.User;

public class PatientMapper implements DataMapper<User, PatientProfileDTO> {
    @Override
    public PatientProfileDTO mapToDto(User entity) {
        PatientProfileDTO dto = new PatientProfileDTO();

        // todo set fields

        return dto;
    }

    @Override
    public User mapToEntity(PatientProfileDTO dto) {
        User user = new User();

        // todo set fields

        return user;
    }
}
