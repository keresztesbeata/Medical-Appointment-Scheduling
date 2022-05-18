package src.service.api;

import org.springframework.stereotype.Service;
import src.dto.AppointmentDTO;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidDataException;
import src.exceptions.InvalidOperationException;

import java.time.LocalDate;
import java.util.List;

@Service
public interface AppointmentService {
    void accept(AppointmentDTO appointmentDTO) throws InvalidDataException, EntityNotFoundException;

    void confirm(Integer appointmentId) throws InvalidDataException, EntityNotFoundException;

    void cancel(Integer appointmentId) throws InvalidDataException, EntityNotFoundException;

    void updateStatus(Integer appointmentId) throws InvalidOperationException;

    List<AppointmentDTO> findByPatient(Integer patientId);

    List<AppointmentDTO> findByPatientAndDate(Integer patientId, LocalDate untilDate);

    List<AppointmentDTO> findByDoctor(Integer doctorId);

    List<AppointmentDTO> findByDoctorAndDate(Integer doctorId, LocalDate localDate);
}
