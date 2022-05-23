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
import src.model.users.DoctorProfile;
import src.model.users.PatientProfile;
import src.repository.AppointmentRepository;
import src.repository.DoctorRepository;
import src.repository.MedicalServiceRepository;
import src.repository.PatientRepository;
import src.service.api.AppointmentService;
import src.service.impl.schedule.CompactSchedulingStrategy;
import src.service.impl.schedule.LooseSchedulingStrategy;
import src.service.impl.schedule.SchedulingStrategy;
import src.service.impl.schedule.SchedulingType;
import src.validator.DataValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
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
    private static final String INVALID_DATE_ERROR_MESSAGE = "The date is invalid, it should be after the current date!";

    @Override
    public void create(Integer patientId, AppointmentDTO appointmentDTO) throws InvalidDataException, EntityNotFoundException {
        validator.validate(appointmentDTO);

        Appointment appointment = new Appointment();

        appointment.setPatient(patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException(PATIENT_NOT_FOUND_ERROR_MESSAGE)));
        appointment.setDoctor(doctorRepository.findByFirstNameAndLastName(appointmentDTO.getDoctorFirstName(), appointmentDTO.getDoctorLastName())
                .orElseThrow(() -> new EntityNotFoundException(DOCTOR_NOT_FOUND_ERROR_MESSAGE)));
        appointment.setMedicalService(medicalServiceRepository.findByName(appointmentDTO.getMedicalService())
                .orElseThrow(() -> new EntityNotFoundException(MEDICAL_SERVICE_NOT_FOUND_ERROR_MESSAGE)));
        appointment.setStatus(AppointmentStatus.REQUESTED);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        log.info("create: Appointment with id {} was successfully created by patient {} {}!", savedAppointment.getId(), appointmentDTO.getPatientFirstName(), appointmentDTO.getPatientLastName());
    }

    @Override
    public AppointmentDTO schedule(Integer appointmentId, LocalDateTime dateTime) throws InvalidDataException, EntityNotFoundException {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException(APPOINTMENT_NOT_FOUND_ERROR_MESSAGE));

        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidDataException(INVALID_DATE_ERROR_MESSAGE);
        }
        appointment.setAppointmentDate(dateTime);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        log.info("schedule: Appointment was successfully scheduled!");

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
    public Optional<AppointmentDTO> findById(Integer appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .map(appointmentMapper::mapToDto);
    }

    @Override
    public List<AppointmentDTO> findAllAppointmentsByStatus(String status) {
        if (status == null || status.isEmpty()) {
            return appointmentRepository.findAll()
                    .stream()
                    .map(appointmentMapper::mapToDto)
                    .collect(Collectors.toList());
        }
        return appointmentRepository.findByStatus(AppointmentStatus.valueOf(status))
                .stream()
                .map(appointmentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllMedicalServices() {
        return medicalServiceRepository.findAll()
                .stream()
                .map(MedicalService::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> findAllAppointmentsOfPatient(String firstName, String lastName) throws EntityNotFoundException {
        PatientProfile patientProfile = patientRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new EntityNotFoundException(PATIENT_NOT_FOUND_ERROR_MESSAGE));

        return appointmentRepository.findByPatient(patientProfile)
                .stream()
                .map(appointmentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> findPastAppointmentsOfPatient(Integer patientId) throws EntityNotFoundException {
        PatientProfile patientProfile = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException(PATIENT_NOT_FOUND_ERROR_MESSAGE));

        return appointmentRepository.findByPatientAndAppointmentDateBefore(patientProfile, LocalDateTime.now())
                .stream()
                .map(appointmentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> findUpcomingAppointmentsOfPatient(Integer patientId) throws EntityNotFoundException {
        PatientProfile patientProfile = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException(PATIENT_NOT_FOUND_ERROR_MESSAGE));

        return appointmentRepository.findByPatientAndAppointmentDateAfter(patientProfile, LocalDateTime.now())
                .stream()
                .map(appointmentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> filterByDoctorAndStatus(Integer doctorId, String appointmentStatus) throws EntityNotFoundException {
        DoctorProfile doctorProfile = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException(DOCTOR_NOT_FOUND_ERROR_MESSAGE));

        if (appointmentStatus == null || appointmentStatus.isEmpty()) {
            return appointmentRepository.findByDoctor(doctorProfile)
                    .stream()
                    .map(appointmentMapper::mapToDto)
                    .collect(Collectors.toList());
        }

        return appointmentRepository.findByDoctorAndStatus(doctorProfile, AppointmentStatus.valueOf(appointmentStatus))
                .stream()
                .map(appointmentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> findPastAppointmentsOfDoctor(Integer doctorId) throws EntityNotFoundException {
        DoctorProfile doctorProfile = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException(DOCTOR_NOT_FOUND_ERROR_MESSAGE));

        return appointmentRepository.findByDoctorAndAppointmentDateBefore(doctorProfile, LocalDateTime.now())
                .stream()
                .map(appointmentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> findPresentAppointmentsOfDoctor(Integer doctorId) throws EntityNotFoundException {
        DoctorProfile doctorProfile = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException(DOCTOR_NOT_FOUND_ERROR_MESSAGE));

        return appointmentRepository.findByDoctorAndAppointmentDateBetween(doctorProfile, LocalDate.now().atStartOfDay(), LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT))
                .stream()
                .map(appointmentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> findFutureAppointmentsOfDoctor(Integer doctorId) throws EntityNotFoundException {
        DoctorProfile doctorProfile = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException(DOCTOR_NOT_FOUND_ERROR_MESSAGE));

        return appointmentRepository.findByDoctorAndAppointmentDateAfter(doctorProfile, LocalDateTime.now())
                .stream()
                .map(appointmentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> findAllAppointmentsOfDoctor(String firstName, String lastName) throws EntityNotFoundException {
        DoctorProfile doctorProfile = doctorRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new EntityNotFoundException(DOCTOR_NOT_FOUND_ERROR_MESSAGE));

        return appointmentRepository.findByDoctor(doctorProfile)
                .stream()
                .map(appointmentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void changeSchedulingStrategy(String schedulingType) {
        switch (SchedulingType.valueOf(schedulingType)) {
            case LOOSE -> schedulingStrategy = new LooseSchedulingStrategy();
            case COMPACT -> schedulingStrategy = new CompactSchedulingStrategy();
        }

        log.info("changeSchedulingStrategy: The scheduling strategy has been changed to " + schedulingType + "!");
    }

    @Override
    public List<LocalDateTime> findAvailableDates(String doctorFirstName, String doctorLastName, String medicalServiceName) throws EntityNotFoundException {
        DoctorProfile doctorProfile = doctorRepository.findByFirstNameAndLastName(doctorFirstName, doctorLastName)
                .orElseThrow(() -> new EntityNotFoundException(DOCTOR_NOT_FOUND_ERROR_MESSAGE));
        MedicalService medicalService = medicalServiceRepository.findByName(medicalServiceName)
                .orElseThrow(() -> new EntityNotFoundException(MEDICAL_SERVICE_NOT_FOUND_ERROR_MESSAGE));

        List<Appointment> existingAppointments = appointmentRepository.findByDoctorAndStatus(doctorProfile, AppointmentStatus.CONFIRMED);

        return schedulingStrategy.findAvailableSpots(medicalService, doctorProfile, existingAppointments);
    }

    @Override
    public List<DoctorProfileDTO> findDoctorsByMedicalService(String medicalServiceName) throws EntityNotFoundException {
        if (medicalServiceName == null || medicalServiceName.isEmpty()) {
            return doctorRepository.findAll()
                    .stream()
                    .map(doctor -> (new DoctorMapper()).mapToDto(doctor))
                    .collect(Collectors.toList());
        }
        MedicalService medicalService = medicalServiceRepository.findByName(medicalServiceName)
                .orElseThrow(() -> new EntityNotFoundException(MEDICAL_SERVICE_NOT_FOUND_ERROR_MESSAGE));

        return doctorRepository.findBySpecialty(medicalService.getSpecialty())
                .stream()
                .map(doctor -> (new DoctorMapper()).mapToDto(doctor))
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> filterByPatientAndStatus(Integer patientId, String appointmentStatus) throws EntityNotFoundException {
        PatientProfile patientProfile = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException(PATIENT_NOT_FOUND_ERROR_MESSAGE));

        if (appointmentStatus == null || appointmentStatus.isEmpty()) {
            return appointmentRepository.findByPatient(patientProfile)
                    .stream()
                    .map(appointmentMapper::mapToDto)
                    .collect(Collectors.toList());
        }

        return appointmentRepository.findByPatientAndStatus(patientProfile, AppointmentStatus.valueOf(appointmentStatus))
                .stream()
                .map(appointmentMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
