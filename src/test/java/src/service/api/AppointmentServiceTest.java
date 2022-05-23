package src.service.api;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import src.dto.AppointmentDTO;
import src.exceptions.InvalidAccessException;
import src.exceptions.InvalidStateException;
import src.mapper.AppointmentMapper;
import src.model.Appointment;
import src.model.AppointmentStatus;
import src.model.MedicalService;
import src.model.Specialty;
import src.model.users.Account;
import src.model.users.AccountType;
import src.model.users.DoctorProfile;
import src.model.users.PatientProfile;
import src.repository.*;
import src.service.impl.AppointmentServiceImpl;
import src.service.impl.schedule.CompactSchedulingStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static src.model.AppointmentStatus.CONFIRMED;
import static src.service.api.TestComponentFactory.*;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentServiceTest {
    @Spy
    private DoctorRepository doctorRepository;
    @Spy
    private MedicalServiceRepository medicalServiceRepository;
    @Spy
    private AppointmentRepository appointmentRepository;
    @Spy
    private AccountRepository accountRepository;
    @Spy
    private PatientRepository patientRepository;
    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private AppointmentMapper appointmentMapper = new AppointmentMapper();

    @Test
    public void create() {
        Specialty specialty = createSpecialty();
        String medicalServiceName = "Facial treatment";
        MedicalService medicalService = createMedicalService(medicalServiceName, specialty);

        Mockito.when(medicalServiceRepository.findByName(medicalServiceName))
                .thenReturn(Optional.of(medicalService));

        Account doctorAccount = createAccount(AccountType.DOCTOR, "doctor", "doctor123P#", 1);

        String doctorFirstName = "Peter";
        String doctorLastName = "Parker";

        DoctorProfile doctorProfile = createDoctorProfile(doctorAccount, specialty, doctorFirstName, doctorLastName);

        Mockito.when(doctorRepository.findByFirstNameAndLastName(doctorFirstName, doctorLastName))
                .thenReturn(Optional.of(doctorProfile));

        Account patientAccount = createAccount(AccountType.PATIENT, "patient", "patient123P#", 2);

        String patientFirstName = "Mary";
        String patientLastName = "Jane";
        PatientProfile patientProfile = createPatientProfile(patientAccount, patientFirstName, patientLastName);

        Mockito.when(patientRepository.findById(patientAccount.getId()))
                .thenReturn(Optional.of(patientProfile));

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setMedicalService(medicalService.getName());
        appointmentDTO.setDoctorFirstName(doctorProfile.getFirstName());
        appointmentDTO.setDoctorLastName(doctorProfile.getLastName());
        appointmentDTO.setPatientFirstName(patientProfile.getFirstName());
        appointmentDTO.setPatientLastName(patientProfile.getLastName());

        int appointmentId = 1;
        Mockito.when(appointmentRepository.save(Mockito.any(Appointment.class)))
                .thenAnswer(i -> {
                    Appointment appointment = (Appointment) i.getArguments()[0];
                    appointment.setStatus(AppointmentStatus.REQUESTED);
                    appointment.setId(appointmentId);
                    return appointment;
                });

        Assertions.assertDoesNotThrow(() -> appointmentService.create(patientAccount.getId(), appointmentDTO));
    }

    @Test
    public void schedule() {
        Specialty specialty = createSpecialty();
        String medicalServiceName = "Facial treatment";
        MedicalService medicalService = createMedicalService(medicalServiceName, specialty);

        Account doctorAccount = createAccount(AccountType.DOCTOR, "doctor", "doctor123P#", 1);

        String doctorFirstName = "Peter";
        String doctorLastName = "Parker";

        DoctorProfile doctorProfile = createDoctorProfile(doctorAccount, specialty, doctorFirstName, doctorLastName);

        Account patientAccount = createAccount(AccountType.PATIENT, "patient", "patient123P#", 2);

        String patientFirstName = "Mary";
        String patientLastName = "Jane";
        PatientProfile patientProfile = createPatientProfile(patientAccount, patientFirstName, patientLastName);

        int appointmentId = 1;
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setMedicalService(medicalService.getName());
        appointmentDTO.setDoctorFirstName(doctorProfile.getFirstName());
        appointmentDTO.setDoctorLastName(doctorProfile.getLastName());
        appointmentDTO.setPatientFirstName(patientProfile.getFirstName());
        appointmentDTO.setPatientLastName(patientProfile.getLastName());
        appointmentDTO.setStatus(AppointmentStatus.REQUESTED.name());
        appointmentDTO.setId(appointmentId);
        LocalDateTime appointmentDate = LocalDateTime.now().plusDays(10);
        appointmentDTO.setAppointmentDate(appointmentDate);

        Appointment savedAppointment = new Appointment();
        savedAppointment.setAppointmentDate(appointmentDate);
        savedAppointment.setMedicalService(medicalService);
        savedAppointment.setDoctor(doctorProfile);
        savedAppointment.setPatient(patientProfile);
        savedAppointment.setId(appointmentId);

        Mockito.when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.of(savedAppointment));

        Mockito.when(appointmentRepository.save(Mockito.any(Appointment.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        Assertions.assertEquals(CompactSchedulingStrategy.class, appointmentService.getSchedulingStrategy().getClass());
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(AppointmentStatus.SCHEDULED.name(), appointmentService.schedule(appointmentId, appointmentDate).getStatus()));
    }

    @Test
    public void updateStatus() {
        Specialty specialty = createSpecialty();
        String medicalServiceName = "Facial treatment";
        MedicalService medicalService = createMedicalService(medicalServiceName, specialty);

        Account doctorAccount = createAccount(AccountType.DOCTOR, "doctor", "doctor123P#", 1);

        String doctorFirstName = "Peter";
        String doctorLastName = "Parker";

        DoctorProfile doctorProfile = createDoctorProfile(doctorAccount, specialty, doctorFirstName, doctorLastName);

        Account patientAccount = createAccount(AccountType.PATIENT, "patient", "patient123P#", 2);

        String patientFirstName = "Mary";
        String patientLastName = "Jane";
        PatientProfile patientProfile = createPatientProfile(patientAccount, patientFirstName, patientLastName);

        Account receptionistAccount = createAccount(AccountType.RECEPTIONIST, "receptionist", "receptionist123P#", 3);

        int appointmentId = 1;
        LocalDateTime appointmentDate = LocalDateTime.now().plusDays(10);

        Appointment appointment = new Appointment();
        appointment.setMedicalService(medicalService);
        appointment.setDoctor(doctorProfile);
        appointment.setPatient(patientProfile);
        appointment.setId(appointmentId);
        appointment.setStatus(AppointmentStatus.REQUESTED);

        AppointmentDTO appointmentDTO = appointmentMapper.mapToDto(appointment);
        appointmentDTO.setAppointmentDate(appointmentDate);

        Mockito.when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.of(appointment));

        appointment.setAppointmentDate(appointmentDate);

        Mockito.when(appointmentRepository.save(Mockito.any(Appointment.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        Assertions.assertThrows(InvalidAccessException.class, () -> appointmentService.updateStatus(appointmentId, patientAccount, AppointmentStatus.SCHEDULED.name()));
        Assertions.assertThrows(InvalidStateException.class, () -> appointmentService.updateStatus(appointmentId, patientAccount, CONFIRMED.name()));
        Assertions.assertDoesNotThrow(() -> appointmentService.updateStatus(appointmentId, receptionistAccount, AppointmentStatus.SCHEDULED.name()));

        Assertions.assertThrows(InvalidAccessException.class, () -> appointmentService.updateStatus(appointmentId, receptionistAccount, CONFIRMED.name()));
        Assertions.assertThrows(InvalidStateException.class, () -> appointmentService.updateStatus(appointmentId, patientAccount, AppointmentStatus.MISSED.name()));
        Assertions.assertDoesNotThrow(() -> appointmentService.updateStatus(appointmentId, patientAccount, CONFIRMED.name()));

        Assertions.assertThrows(InvalidStateException.class, () -> appointmentService.updateStatus(appointmentId, receptionistAccount, AppointmentStatus.SCHEDULED.name()));
        Assertions.assertThrows(InvalidAccessException.class, () -> appointmentService.updateStatus(appointmentId, patientAccount, AppointmentStatus.MISSED.name()));
        Assertions.assertDoesNotThrow(() -> appointmentService.updateStatus(appointmentId, patientAccount, AppointmentStatus.CANCELED.name()));
        Assertions.assertThrows(InvalidStateException.class, () -> appointmentService.updateStatus(appointmentId, receptionistAccount, AppointmentStatus.MISSED.name()));
    }

    @Test
    public void findAvailableDates() {
        Specialty specialty = createSpecialty();
        String medicalServiceName = "Facial treatment";
        MedicalService medicalService = createMedicalService(medicalServiceName, specialty);

        Mockito.when(medicalServiceRepository.findByName(medicalServiceName))
                .thenReturn(Optional.of(medicalService));

        Account doctorAccount = createAccount(AccountType.DOCTOR, "doctor", "doctor123P#", 1);

        String doctorFirstName = "Peter";
        String doctorLastName = "Parker";

        DoctorProfile doctorProfile = createDoctorProfile(doctorAccount, specialty, doctorFirstName, doctorLastName);

        Account patientAccount = createAccount(AccountType.PATIENT, "patient", "patient123P#", 2);

        String patientFirstName = "Mary";
        String patientLastName = "Jane";
        PatientProfile patientProfile = createPatientProfile(patientAccount, patientFirstName, patientLastName);

        List<Appointment> existingAppointments = new ArrayList<>();
        existingAppointments.add(createAppointment(1, doctorProfile, patientProfile, medicalService, CONFIRMED, LocalDateTime.now().plusDays(1).withHour(9).withMinute(30)));
        existingAppointments.add(createAppointment(2, doctorProfile, patientProfile, medicalService, CONFIRMED, LocalDateTime.now().plusDays(1).withHour(12).withMinute(0)));
        existingAppointments.add(createAppointment(3, doctorProfile, patientProfile, medicalService, CONFIRMED, LocalDateTime.now().plusDays(1).withHour(15).withMinute(30)));

        Mockito.when(doctorRepository.findByFirstNameAndLastName(doctorProfile.getFirstName(), doctorProfile.getLastName()))
                .thenReturn(Optional.of(doctorProfile));

        Mockito.when(appointmentRepository.findByDoctorAndStatus(doctorProfile, CONFIRMED))
                .thenReturn(existingAppointments);

        Assertions.assertEquals(CompactSchedulingStrategy.class, appointmentService.getSchedulingStrategy().getClass());
        Assertions.assertDoesNotThrow(() -> appointmentService.findAvailableDates(doctorProfile.getFirstName(), doctorProfile.getLastName(), medicalService.name).forEach(System.out::println));
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(4, appointmentService.findAvailableDates(doctorProfile.getFirstName(), doctorProfile.getLastName(), medicalService.name).size()));
    }
}