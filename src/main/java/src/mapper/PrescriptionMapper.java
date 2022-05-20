package src.mapper;

import src.dto.PrescriptionDTO;
import src.model.Prescription;


public class PrescriptionMapper implements DataMapper<Prescription, PrescriptionDTO> {
    @Override
    public PrescriptionDTO mapToDto(Prescription entity) {
        PrescriptionDTO dto = new PrescriptionDTO();

        dto.setId(entity.getId());
        dto.setIndications(entity.getIndications());
        dto.setMedication(entity.getMedication());
        dto.setPatientFirstName(entity.getAppointment().getPatient().getFirstName());
        dto.setPatientLastName(entity.getAppointment().getPatient().getLastName());
        dto.setDoctorFirstName(entity.getAppointment().getDoctor().getFirstName());
        dto.setDoctorLastName(entity.getAppointment().getDoctor().getLastName());
        dto.setAppointmentDate(entity.getAppointment().getAppointmentDate());

        return dto;
    }

    @Override
    public Prescription mapToEntity(PrescriptionDTO dto) {
        Prescription prescription = new Prescription();

        prescription.setId(dto.getId());
        prescription.setIndications(dto.getIndications());
        prescription.setMedication(dto.getMedication());

        return prescription;
    }
}
