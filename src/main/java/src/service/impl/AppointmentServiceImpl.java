package src.service.impl;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.dto.AppointmentDTO;
import src.dto.DoctorProfileDTO;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidAccessException;
import src.exceptions.InvalidDataException;
import src.exceptions.InvalidStateException;
import src.mapper.AppointmentMapper;
import src.mapper.DoctorMapper;
import src.model.Appointment;
import src.model.AppointmentStatus;
import src.model.MedicalService;
import src.model.appointment_states.AbstractAppointmentState;
import src.model.appointment_states.AppointmentStateFactory;
import src.model.users.Account;
import src.model.users.AccountType;
import src.model.users.DoctorProfile;
import src.model.users.PatientProfile;
import src.repository.*;
import src.service.api.AppointmentService;
import src.service.impl.schedule.CompactSchedulingStrategy;
import src.service.impl.schedule.LooseSchedulingStrategy;
import src.service.impl.schedule.SchedulingStrategy;
import src.service.impl.schedule.SchedulingType;
import src.validator.DataValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private MedicalServiceRepository medicalServiceRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;

    private DataValidator<AppointmentDTO> validator = new DataValidator<>();
    private AppointmentStateFactory appointmentStateFactory = new AppointmentStateFactory();
    private AppointmentMapper appointmentMapper = new AppointmentMapper();
    @Getter
    private SchedulingStrategy schedulingStrategy = new CompactSchedulingStrategy();

    private static final String APPOINTMENT_NOT_FOUND_ERROR_MESSAGE = "Appointment not found by id!";
    private static final String PATIENT_NOT_FOUND_ERROR_MESSAGE = "Patient not found by id!";
    private static final String DOCTOR_NOT_FOUND_ERROR_MESSAGE = "Doctor not found by id!";
    private static final String MEDICAL_SERVICE_NOT_FOUND_ERROR_MESSAGE = "Medical service not found by name!";

    @Override
    public void create(AppointmentDTO appointmentDTO) throws InvalidDataException, EntityNotFoundException {
        validator.validate(appointmentDTO);

        Appointment appointment = new Appointment();

        appointment.setDoctor(doctorRepository.findByFirstNameAndLastName(appointmentDTO.getDoctorFirstName(), appointmentDTO.getDoctorLastName())
                .orElseThrow(() -> new EntityNotFoundException(DOCTOR_NOT_FOUND_ERROR_MESSAGE)));
        appointment.setPatient(patientRepository.findByFirstNameAndLastName(appointmentDTO.getPatientFirstName(), appointmentDTO.getPatientLastName())
                .orElseThrow(() -> new EntityNotFoundException(PATIENT_NOT_FOUND_ERROR_MESSAGE)));
        appointment.setMedicalService(medicalServiceRepository.findByName(appointmentDTO.getMedicalService())
                .orElseThrow(() -> new EntityNotFoundException(MEDICAL_SERVICE_NOT_FOUND_ERROR_MESSAGE)));
        appointment.setStatus(AppointmentStatus.REQUESTED);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        log.info("request: Appointment with id {} was successfully created by patient {} {}!", savedAppointment.getId(), appointmentDTO.getPatientFirstName(), appointmentDTO.getPatientLastName());
    }

    @Override
    public AppointmentDTO schedule(Integer appointmentId, LocalDateTime dateTime) throws InvalidDataException, EntityNotFoundException {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException(APPOINTMENT_NOT_FOUND_ERROR_MESSAGE));

        appointment.setAppointmentDate(dateTime);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        log.info("update: Appointment was successfully scheduled!");

        return appointmentMapper.mapToDto(savedAppointment);
    }

    @Override
    public void updateStatus(Integer appointmentId, Account account, String newAppointmentStatus) throws InvalidStateException, EntityNotFoundException, InvalidAccessException {
        AppointmentStatus newStatus = AppointmentStatus.valueOf(newAppointmentStatus);

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException(APPOINTMENT_NOT_FOUND_ERROR_MESSAGE));

        AppointmentStatus oldStatus = appointment.getStatus();

        AbstractAppointmentState appointmentState = appointmentStateFactory.getAppointmentState(appointment);

        switch (newStatus) {
            case SCHEDULED -> {
                appointmentState.setScheduled(account.getAccountType());
            }
            case CONFIRMED -> {
                appointmentState.setConfirmed(account.getAccountType());
            }
            case CANCELED -> {
                appointmentState.setCanceled(account.getAccountType());
            }
            case MISSED -> {
                appointmentState.setMissed(account.getAccountType());
            }
            case COMPLETED -> {
                appointmentState.setCompleted(account.getAccountType());
            }
            default -> {
            }
        }
        Appointment updatedAppointment = appointmentRepository.save(appointmentState.getAppointment());

        log.info("updateStatus: The status of the appointment with id " + appointmentId +
                " has been successfully updated from " + oldStatus + " to " + updatedAppointment.getStatus() + "!");
    }

    @Override
    public List<AppointmentDTO> findByPatient(Integer patientId) throws EntityNotFoundException {
        PatientProfile patientProfile = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException(PATIENT_NOT_FOUND_ERROR_MESSAGE));

        return appointmentRepository.findByPatient(patientProfile)
                .stream()
                .map(appointmentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> findByPatientAndDateUntil(Integer patientId, LocalDate untilDate) throws EntityNotFoundException {
        PatientProfile patientProfile = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException(PATIENT_NOT_FOUND_ERROR_MESSAGE));

        return appointmentRepository.findByPatientAndAppointmentDateBefore(patientProfile, untilDate.atStartOfDay())
                .stream()
                .map(appointmentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> findByPatientAndDateUpTo(Integer patientId, LocalDate toDate) throws EntityNotFoundException {
        PatientProfile patientProfile = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException(PATIENT_NOT_FOUND_ERROR_MESSAGE));

        return appointmentRepository.findByPatientAndAppointmentDateBefore(patientProfile, toDate.atStartOfDay())
                .stream()
                .map(appointmentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> findByDoctor(Integer doctorId) throws EntityNotFoundException {
        DoctorProfile doctorProfile = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException(DOCTOR_NOT_FOUND_ERROR_MESSAGE));

        return appointmentRepository.findByDoctor(doctorProfile)
                .stream()
                .map(appointmentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> findByDoctorAndDate(Integer doctorId, LocalDate exactDate) throws EntityNotFoundException {
        DoctorProfile doctorProfile = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException(DOCTOR_NOT_FOUND_ERROR_MESSAGE));

        return appointmentRepository.findByDoctorAndAppointmentDate(doctorProfile, exactDate)
                .stream()
                .map(appointmentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void changeSchedulingStrategy(SchedulingType schedulingType) {
        switch (schedulingType) {
            case LOOSE -> schedulingStrategy = new LooseSchedulingStrategy();
            case COMPACT -> schedulingStrategy = new CompactSchedulingStrategy();
        }
    }

    @Override
    public List<LocalDateTime> findAvailableDates(Integer doctorId, MedicalService medicalService) throws EntityNotFoundException {
        DoctorProfile doctorProfile = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException(DOCTOR_NOT_FOUND_ERROR_MESSAGE));

        return schedulingStrategy.findAvailableSpots(medicalService, doctorProfile, appointmentRepository.findByDoctor(doctorProfile));
    }

    @Override
    public List<DoctorProfileDTO> findDoctorsByMedicalService(String medicalServiceName) throws EntityNotFoundException {
        MedicalService medicalService = medicalServiceRepository.findByName(medicalServiceName)
                .orElseThrow(() -> new EntityNotFoundException(MEDICAL_SERVICE_NOT_FOUND_ERROR_MESSAGE));

        return doctorRepository.findByMedicalService(medicalService)
                .stream()
                .map(doctor -> (new DoctorMapper()).mapToDto(doctor))
                .collect(Collectors.toList());
    }
}
