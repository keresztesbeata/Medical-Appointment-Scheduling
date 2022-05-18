package src.mapper;

import org.springframework.stereotype.Component;
import src.dto.DoctorProfileDTO;
import src.model.users.DoctorProfile;

@Component
public class DoctorMapper implements DataMapper<DoctorProfile, DoctorProfileDTO> {
    @Override
    public DoctorProfileDTO mapToDto(DoctorProfile entity) {
        DoctorProfileDTO dto = new DoctorProfileDTO();

        // todo set fields

        return dto;
    }

    @Override
    public DoctorProfile mapToEntity(DoctorProfileDTO dto) {
        DoctorProfile doctorProfile = new DoctorProfile();

        // todo set fields

        return doctorProfile;
    }
}
