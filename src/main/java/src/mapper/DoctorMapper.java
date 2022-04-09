package src.mapper;

import src.dto.DoctorDTO;
import src.model.users.Doctor;

public class DoctorMapper implements DataMapper<Doctor, DoctorDTO> {
    @Override
    public DoctorDTO mapToDto(Doctor entity) {
        DoctorDTO dto = new DoctorDTO();

        // todo set fields

        return dto;
    }

    @Override
    public Doctor mapToEntity(DoctorDTO dto) {
        Doctor doctor = new Doctor();

        // todo set fields

        return doctor;
    }
}
