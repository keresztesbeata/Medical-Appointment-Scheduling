package src.mapper;

import org.springframework.stereotype.Component;
import src.dto.DoctorProfileDTO;
import src.model.users.DoctorProfile;

@Component
public class DoctorMapper implements DataMapper<DoctorProfile, DoctorProfileDTO> {
    @Override
    public DoctorProfileDTO mapToDto(DoctorProfile entity) {
        DoctorProfileDTO dto = new DoctorProfileDTO();

        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setSpecialty(entity.getSpecialty().getName());
        dto.setStartTime(entity.getStartTime());
        dto.setFinishTime(entity.getFinishTime());

        return dto;
    }

    @Override
    public DoctorProfile mapToEntity(DoctorProfileDTO dto) {
        DoctorProfile doctorProfile = new DoctorProfile();

        doctorProfile.setFirstName(dto.getFirstName());
        doctorProfile.setLastName(dto.getLastName());
        doctorProfile.setStartTime(dto.getStartTime());
        doctorProfile.setFinishTime(dto.getFinishTime());

        return doctorProfile;
    }
}
