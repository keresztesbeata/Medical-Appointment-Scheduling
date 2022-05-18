package src.mapper;

import org.springframework.stereotype.Component;
import src.dto.ReceptionistProfileDTO;
import src.model.users.ReceptionistProfile;

@Component
public class ReceptionistMapper implements DataMapper<ReceptionistProfile, ReceptionistProfileDTO> {
    @Override
    public ReceptionistProfileDTO mapToDto(ReceptionistProfile entity) {
        ReceptionistProfileDTO dto = new ReceptionistProfileDTO();

        // todo set fields

        return dto;
    }

    @Override
    public ReceptionistProfile mapToEntity(ReceptionistProfileDTO dto) {
        ReceptionistProfile receptionistProfile = new ReceptionistProfile();

        // todo set fields

        return receptionistProfile;
    }
}
