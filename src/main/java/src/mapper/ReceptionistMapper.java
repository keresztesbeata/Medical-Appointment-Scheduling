package src.mapper;

import src.dto.ReceptionistProfileDTO;
import src.model.users.User;

public class ReceptionistMapper implements DataMapper<User, ReceptionistProfileDTO> {
    @Override
    public ReceptionistProfileDTO mapToDto(User entity) {
        ReceptionistProfileDTO dto = new ReceptionistProfileDTO();

        // todo set fields

        return dto;
    }

    @Override
    public User mapToEntity(ReceptionistProfileDTO dto) {
        User user = new User();

        // todo set fields

        return user;
    }
}
