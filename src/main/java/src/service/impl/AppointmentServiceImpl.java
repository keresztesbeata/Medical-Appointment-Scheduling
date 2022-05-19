package src.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.dto.AppointmentDTO;
import src.dto.PrescriptionDTO;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidAccessException;
import src.exceptions.InvalidDataException;
import src.exceptions.InvalidStateException;
import src.mapper.AppointmentMapper;
import src.model.Appointment;
import src.model.AppointmentStatus;
import src.model.Prescription;
import src.model.appointment_states.AbstractAppointmentState;
import src.model.appointment_states.AppointmentStateFactory;
import src.model.users.Account;
import src.model.users.AccountType;
import src.model.users.DoctorProfile;
import src.model.users.PatientProfile;
import src.repository.*;
import src.service.api.AppointmentService;
import src.validator.DataValidator;

import java.time.LocalDate;
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
    @Autowired
    private PrescriptionRepository prescriptionRepository;

    private DataValidator<AppointmentDTO> validator = new DataValidator<>();
    private AppointmentStateFactory appointmentStateFactory = new AppointmentStateFactory();
    private AppointmentMapper appointmentMapper = new AppointmentMapper();
    
    private static final String APPOINTMENT_NOT_FOUND_ERROR_MESSAGE = "Appointment not found by id!";
    private static final String PATIENT_NOT_FOUND_ERROR_MESSAGE = "Patient not found by id!";
    private static final String DOCTOR_NOT_FOUND_ERROR_MESSAGE = "Doctor not found by id!";
    private static final String MEDICAL_SERVICE_NOT_FOUND_ERROR_MESSAGE = "Medical service not found by name!";
    private static final String CANNOT_ADD_PRESCRIPTION_ERROR_MESSAGE = "Prescriptions cannot be added in the current state of the appointment!";
    private static final String CANNOT_UPDATE_STATUS_ERROR_MESSAGE = "You cannot update the appointment status, because you do not have enough rights!";
    
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
    public AppointmentDTO update(AppointmentDTO appointmentDTO) throws InvalidDataException, EntityNotFoundException {
        validator.validate(appointmentDTO);

        Appointment appointment = appointmentMapper.mapToEntity(appointmentDTO);

        appointment.setDoctor(doctorRepository.findByFirstNameAndLastName(appointmentDTO.getDoctorFirstName(), appointmentDTO.getDoctorLastName())
                .orElseThrow(() -> new EntityNotFoundException(DOCTOR_NOT_FOUND_ERROR_MESSAGE)));
        appointment.setPatient(patientRepository.findByFirstNameAndLastName(appointmentDTO.getPatientFirstName(), appointmentDTO.getPatientLastName())
                .orElseThrow(() -> new EntityNotFoundException(PATIENT_NOT_FOUND_ERROR_MESSAGE)));
        appointment.setMedicalService(medicalServiceRepository.findByName(appointmentDTO.getMedicalService())
                .orElseThrow(() -> new EntityNotFoundException(MEDICAL_SERVICE_NOT_FOUND_ERROR_MESSAGE)));

        Appointment savedAppointment = appointmentRepository.save(appointment);

        log.info("update: Appointment was successfully updated!");

        return appointmentMapper.mapToDto(savedAppointment);
    }

    @Override
    public void addPrescription(Integer appointmentId, PrescriptionDTO prescriptionDTO) throws InvalidStateException, EntityNotFoundException {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException(APPOINTMENT_NOT_FOUND_ERROR_MESSAGE));

        if(!appointment.getStatus().equals(AppointmentStatus.CONFIRMED)) {
             throw new InvalidStateException(CANNOT_ADD_PRESCRIPTION_ERROR_MESSAGE);
        }

        Prescription prescription = new Prescription();
        prescription.setMedication(prescriptionDTO.getMedication());
        prescription.setIndications(prescriptionDTO.getIndications());
        prescription.setAppointment(appointment);
        Prescription savedPrescription = prescriptionRepository.save(prescription);

        log.info("addPrescription: Prescription with id {} was successfully added to the appointment with id {}!",savedPrescription.getId(), appointment.getId());
    }

    @Override
    public void updateStatus(Integer appointmentId, Account account, String newAppointmentStatus) throws InvalidStateException, EntityNotFoundException, InvalidAccessException {
        AppointmentStatus newStatus = AppointmentStatus.valueOf(newAppointmentStatus);

        if((newStatus.equals(AppointmentStatus.CONFIRMED) ||
                newStatus.equals(AppointmentStatus.CANCELED)) &&
                !account.getAccountType().equals(AccountType.PATIENT)) {
            throw new InvalidAccessException(CANNOT_UPDATE_STATUS_ERROR_MESSAGE);
        }else if((newStatus.equals(AppointmentStatus.SCHEDULED) ||
                newStatus.equals(AppointmentStatus.COMPLETED) ||
                newStatus.equals(AppointmentStatus.MISSED)) &&
                !account.getAccountType().equals(AccountType.RECEPTIONIST)) {
            throw new InvalidAccessException(CANNOT_UPDATE_STATUS_ERROR_MESSAGE);
        }

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException(APPOINTMENT_NOT_FOUND_ERROR_MESSAGE));

        AppointmentStatus oldStatus = appointment.getStatus();

        AbstractAppointmentState appointmentState = appointmentStateFactory.getAppointmentState(appointment);

        switch (newStatus) {
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
        return appointmentRepository.findByPatientAndAppointmentDateBefore(patientProfile, untilDate)
                .stream()
                .map(appointmentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> findByPatientAndDateUpTo(Integer patientId, LocalDate toDate) throws EntityNotFoundException {
        PatientProfile patientProfile = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException(PATIENT_NOT_FOUND_ERROR_MESSAGE));
        return appointmentRepository.findByPatientAndAppointmentDateBefore(patientProfile, toDate)
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
}
