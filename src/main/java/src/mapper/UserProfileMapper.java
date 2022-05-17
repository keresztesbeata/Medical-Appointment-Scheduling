package src.mapper;

import src.dto.UserProfileDTO;
import src.model.users.UserProfile;

public class UserProfileMapper implements DataMapper<UserProfile, UserProfileDTO> {
    @Override
    public UserProfileDTO mapToDto(UserProfile entity) {
        UserProfileDTO dto = new UserProfileDTO();

        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());

        return dto;
    }

    @Override
    public UserProfile mapToEntity(UserProfileDTO dto) {
        UserProfile userProfile = new UserProfile();

        userProfile.setFirstName(dto.getFirstName());
        userProfile.setLastName(dto.getLastName());

        return userProfile;
    }
}
