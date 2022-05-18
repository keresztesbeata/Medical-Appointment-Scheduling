package src.mapper;

import org.springframework.stereotype.Component;
import src.dto.PrescriptionDTO;
import src.model.Prescription;


public class PrescriptionMapper implements DataMapper<Prescription, PrescriptionDTO> {
    @Override
    public PrescriptionDTO mapToDto(Prescription entity) {
        PrescriptionDTO dto = new PrescriptionDTO();

        // todo

        return dto;
    }

    @Override
    public Prescription mapToEntity(PrescriptionDTO dto) {
        Prescription prescription = new Prescription();

        // todo

        return prescription;
    }
}
