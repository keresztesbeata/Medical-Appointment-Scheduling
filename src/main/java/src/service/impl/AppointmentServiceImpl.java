package src.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.dto.AppointmentDTO;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidDataException;
import src.exceptions.InvalidOperationException;
import src.model.Appointment;
import src.model.AppointmentStatus;
import src.model.appointment_states.AbstractAppointmentState;
import src.model.appointment_states.AppointmentStateFactory;
import src.repository.AppointmentRepository;
import src.service.ErrorMessages;
import src.service.api.AppointmentService;
import src.validator.DataValidator;

import java.time.LocalDate;
import java.util.List;

@Service
@Log4j2
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    private DataValidator<Appointment> validator = new DataValidator<>();
    private AppointmentStateFactory appointmentStateFactory = new AppointmentStateFactory();
    //private AppointmentMapper mapper = new AppointmentMapper();
    @Override
    public void accept(AppointmentDTO appointmentDTO) throws InvalidDataException, EntityNotFoundException {

    }

    @Override
    public void confirm(Integer appointmentId) throws InvalidDataException, EntityNotFoundException {

    }

    @Override
    public void cancel(Integer appointmentId) throws InvalidDataException, EntityNotFoundException {

    }

    @Override
    public void updateStatus(Integer appointmentId, String newStatus) throws InvalidOperationException, EntityNotFoundException {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.ENTITY_NOT_FOUND_ERROR_MESSAGE));//todo

        AppointmentStatus oldStatus = appointment.getStatus();

        if (AppointmentStatus.valueOf(newStatus).equals(oldStatus)) {
            throw new IllegalStateException(ErrorMessages.STATE_UNCHANGED_ERROR_MESSAGE);
        }

        AbstractAppointmentState appointmentState = appointmentStateFactory.getAppointmentState(appointment);

        switch (AppointmentStatus.valueOf(newStatus)) {
            case SCHEDULED -> {
                appointmentState.setScheduled();
            }
            case CONFIRMED -> {
                appointmentState.setConfirmed();
            }
            case CANCELED -> {
                appointmentState.setCanceled();
            }
            case MISSED -> {
                appointmentState.setMissed();
            }
            case COMPLETED -> {
                appointmentState.setCompleted();
            }
            default -> {
            }
        }
        Appointment updatedAppointment = appointmentRepository.save(appointmentState.getAppointment());

        log.info("updateStatus: The status of the appointment with id" + appointmentId +
                " has been successfully updated from " + oldStatus + " to " + updatedAppointment.getStatus() + "!");
    }

    @Override
    public List<AppointmentDTO> findByPatient(Integer patientId) {
        return null;
    }

    @Override
    public List<AppointmentDTO> findByPatientAndDate(Integer patientId, LocalDate untilDate) {
        return null;
    }

    @Override
    public List<AppointmentDTO> findByDoctor(Integer doctorId) {
        return null;
    }

    @Override
    public List<AppointmentDTO> findByDoctorAndDate(Integer doctorId, LocalDate localDate) {
        return null;
    }
}
