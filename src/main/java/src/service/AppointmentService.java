package src.service;

import org.springframework.stereotype.Service;
import src.dto.AppointmentDTO;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidDataException;
import src.exceptions.InvalidOperationException;

import java.time.LocalDate;
import java.util.List;

@Service
public interface AppointmentService {
    void acceptAppointment(AppointmentDTO appointmentDTO) throws InvalidDataException, EntityNotFoundException;

    void confirmAppointment(Integer appointmentId) throws InvalidDataException, EntityNotFoundException;

    void cancelAppointment(Integer appointmentId) throws InvalidDataException, EntityNotFoundException;

    void updateAppointmentStatus(Integer appointmentId) throws InvalidOperationException;

    List<AppointmentDTO> viewAllAppointmentsOfPatient(Integer patientId);

    List<AppointmentDTO> viewUpcomingAppointmentsOfPatient(Integer patientId, LocalDate untilDate);

    List<AppointmentDTO> viewAllAppointmentsOfDoctor(Integer doctorId);

    List<AppointmentDTO> viewAppointmentsOfDoctorOnGivenDate(Integer doctorId, LocalDate localDate);
}
