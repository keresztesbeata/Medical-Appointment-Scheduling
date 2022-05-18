package src.mapper;

import org.springframework.stereotype.Component;
import src.dto.PatientProfileDTO;
import src.model.users.PatientProfile;

@Component
public class PatientMapper implements DataMapper<PatientProfile, PatientProfileDTO> {

    @Override
    public PatientProfileDTO mapToDto(PatientProfile entity) {
        PatientProfileDTO dto = new PatientProfileDTO();

        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());
        dto.setAllergies(entity.getAllergies());
        dto.setBirthdate(entity.getBirthDate());
        dto.setEmail(entity.getEmail());

        return dto;
    }

    @Override
    public PatientProfile mapToEntity(PatientProfileDTO dto) {
        PatientProfile patientProfile = new PatientProfile();

        patientProfile.setFirstName(dto.getFirstName());
        patientProfile.setLastName(dto.getLastName());
        patientProfile.setAllergies(dto.getAllergies());
        patientProfile.setEmail(dto.getEmail());
        patientProfile.setPhone(dto.getPhone());
        patientProfile.setBirthDate(dto.getBirthdate());

        return patientProfile;
    }
}
