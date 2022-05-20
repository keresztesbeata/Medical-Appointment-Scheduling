package src.mapper;

import org.springframework.stereotype.Component;
import src.dto.AppointmentDTO;
import src.model.Appointment;
import src.model.AppointmentStatus;

@Component
public class AppointmentMapper implements DataMapper<Appointment, AppointmentDTO> {

    @Override
    public AppointmentDTO mapToDto(Appointment entity) {
        AppointmentDTO appointmentDTO = new AppointmentDTO();

        appointmentDTO.setId(entity.getId());
        appointmentDTO.setAppointmentDate(entity.getAppointmentDate());
        appointmentDTO.setDoctorFirstName(entity.getDoctor().getFirstName());
        appointmentDTO.setDoctorLastName(entity.getDoctor().getLastName());
        appointmentDTO.setMedicalService(entity.getMedicalService().getName());
        if(entity.getPrescription() != null) {
            appointmentDTO.setPrescription((new PrescriptionMapper()).mapToDto(entity.getPrescription()));
        }
        appointmentDTO.setPatientFirstName(entity.getPatient().getFirstName());
        appointmentDTO.setPatientLastName(entity.getPatient().getLastName());
        appointmentDTO.setStatus(entity.getStatus().name());

        return appointmentDTO;
    }

    @Override
    public Appointment mapToEntity(AppointmentDTO dto) {
        Appointment appointment = new Appointment();

        if(dto.getId() != null) {
            appointment.setId(dto.getId());
            appointment.setAppointmentDate(dto.getAppointmentDate());
            appointment.setStatus(AppointmentStatus.valueOf(dto.getStatus()));
        }
        return appointment;
    }
}
