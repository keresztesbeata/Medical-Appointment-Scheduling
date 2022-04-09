package src.mapper;

import src.dto.PatientDTO;
import src.model.users.Patient;

public class PatientMapper implements DataMapper<Patient, PatientDTO> {
    @Override
    public PatientDTO mapToDto(Patient entity) {
        PatientDTO dto = new PatientDTO();

        // todo set fields

        return dto;
    }

    @Override
    public Patient mapToEntity(PatientDTO dto) {
        Patient patient = new Patient();

        // todo set fields

        return patient;
    }
}
