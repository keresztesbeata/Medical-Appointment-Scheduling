package src.mapper;

import src.dto.DoctorProfileDTO;
import src.model.users.User;

public class DoctorMapper implements DataMapper<User, DoctorProfileDTO> {
    @Override
    public DoctorProfileDTO mapToDto(User entity) {
        DoctorProfileDTO dto = new DoctorProfileDTO();

        // todo set fields

        return dto;
    }

    @Override
    public User mapToEntity(DoctorProfileDTO dto) {
        User user = new User();

        // todo set fields

        return user;
    }
}
