package src.mapper;

import src.dto.PrescriptionDTO;
import src.model.Prescription;


public class PrescriptionMapper implements DataMapper<Prescription, PrescriptionDTO> {
    @Override
    public PrescriptionDTO mapToDto(Prescription entity) {
        PrescriptionDTO dto = new PrescriptionDTO();

        dto.setIndications(entity.getIndications());
        dto.setMedication(entity.getMedication());

        return dto;
    }

    @Override
    public Prescription mapToEntity(PrescriptionDTO dto) {
        Prescription prescription = new Prescription();

        prescription.setIndications(dto.getIndications());
        prescription.setMedication(dto.getMedication());

        return prescription;
    }
}
