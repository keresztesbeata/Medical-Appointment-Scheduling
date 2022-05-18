package src.mapper;

import src.dto.AppointmentDTO;
import src.model.Appointment;
import src.model.AppointmentStatus;

public class AppointmentMapper implements DataMapper<Appointment, AppointmentDTO> {

    @Override
    public AppointmentDTO mapToDto(Appointment entity) {
        AppointmentDTO appointmentDTO = new AppointmentDTO();

        appointmentDTO.setAppointmentDate(entity.getAppointmentDate());
        appointmentDTO.setDoctorFirstName(entity.getDoctor().getFirstName());
        appointmentDTO.setDoctorLastName(entity.getDoctor().getLastName());
        appointmentDTO.setMedicalService(entity.getMedicalService().getName());
        appointmentDTO.setMedication(entity.getPrescription().getMedication());
        appointmentDTO.setIndications(entity.getPrescription().getIndications());
        appointmentDTO.setPatientFirstName(entity.getPatient().getFirstName());
        appointmentDTO.setPatientLastName(entity.getPatient().getLastName());
        appointmentDTO.setStatus(entity.getStatus().name());

        return appointmentDTO;
    }

    @Override
    public Appointment mapToEntity(AppointmentDTO dto) {
        Appointment appointment = new Appointment();

        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setStatus(AppointmentStatus.valueOf(dto.getStatus()));

        return null;
    }
}
