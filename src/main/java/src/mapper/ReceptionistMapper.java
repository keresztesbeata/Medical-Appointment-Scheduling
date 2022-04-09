package src.mapper;

import src.dto.ReceptionistDTO;
import src.model.users.Receptionist;

public class ReceptionistMapper implements DataMapper<Receptionist, ReceptionistDTO> {
    @Override
    public ReceptionistDTO mapToDto(Receptionist entity) {
        ReceptionistDTO dto = new ReceptionistDTO();

        // todo set fields

        return dto;
    }

    @Override
    public Receptionist mapToEntity(ReceptionistDTO dto) {
        Receptionist receptionist = new Receptionist();

        // todo set fields

        return receptionist;
    }
}
