package src.service.api;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.MockitoJUnitRunner;
import src.dto.AppointmentDTO;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidDataException;
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
import src.service.impl.schedule.SchedulingType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        Mockito.when(accountRepository.findByUsername("doctor"))
                .thenReturn(Optional.of(doctorAccount));
        Mockito.when(accountRepository.findById(1))
                .thenReturn(Optional.of(doctorAccount));

        String doctorFirstName = "Peter";
        String doctorLastName = "Parker";

        DoctorProfile doctorProfile = createDoctorProfile(doctorAccount, specialty, doctorFirstName, doctorLastName);

        Mockito.when(doctorRepository.findByFirstNameAndLastName(doctorFirstName, doctorLastName))
                .thenReturn(Optional.of(doctorProfile));
        Mockito.when(doctorRepository.findById(doctorAccount.getId()))
                .thenReturn(Optional.of(doctorProfile));

        Account patientAccount = createAccount(AccountType.PATIENT,"patient", "patient123P#", 2);

        Mockito.when(accountRepository.findByUsername("patient"))
                .thenReturn(Optional.of(doctorAccount));
        Mockito.when(accountRepository.findById(2))
                .thenReturn(Optional.of(doctorAccount));

        String patientFirstName = "Mary";
        String patientLastName = "Jane";
        PatientProfile patientProfile = createPatientProfile(patientAccount, patientFirstName, patientLastName);

        Mockito.when(patientRepository.findByFirstNameAndLastName(patientFirstName, patientLastName))
                .thenReturn(Optional.of(patientProfile));
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

        Assertions.assertDoesNotThrow(() -> appointmentService.create(appointmentDTO));
    }

    @Test
    public void schedule() {
        Specialty specialty = createSpecialty();
        String medicalServiceName = "Facial treatment";
        MedicalService medicalService = createMedicalService(medicalServiceName, specialty);

        Mockito.when(medicalServiceRepository.findByName(medicalServiceName))
                .thenReturn(Optional.of(medicalService));

        Account doctorAccount = createAccount(AccountType.DOCTOR, "doctor", "doctor123P#", 1);

        Mockito.when(accountRepository.findByUsername("doctor"))
                .thenReturn(Optional.of(doctorAccount));
        Mockito.when(accountRepository.findById(1))
                .thenReturn(Optional.of(doctorAccount));

        String doctorFirstName = "Peter";
        String doctorLastName = "Parker";

        DoctorProfile doctorProfile = createDoctorProfile(doctorAccount, specialty, doctorFirstName, doctorLastName);

        Mockito.when(doctorRepository.findByFirstNameAndLastName(doctorFirstName, doctorLastName))
                .thenReturn(Optional.of(doctorProfile));
        Mockito.when(doctorRepository.findById(doctorAccount.getId()))
                .thenReturn(Optional.of(doctorProfile));

        Account patientAccount = createAccount(AccountType.PATIENT,"patient", "patient123P#", 2);

        Mockito.when(accountRepository.findByUsername("patient"))
                .thenReturn(Optional.of(doctorAccount));
        Mockito.when(accountRepository.findById(2))
                .thenReturn(Optional.of(doctorAccount));

        String patientFirstName = "Mary";
        String patientLastName = "Jane";
        PatientProfile patientProfile = createPatientProfile(patientAccount, patientFirstName, patientLastName);

        Mockito.when(patientRepository.findByFirstNameAndLastName(patientFirstName, patientLastName))
                .thenReturn(Optional.of(patientProfile));
        Mockito.when(patientRepository.findById(patientAccount.getId()))
                .thenReturn(Optional.of(patientProfile));

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

        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(AppointmentStatus.SCHEDULED.name(), appointmentService.schedule(appointmentId, appointmentDate).getStatus()));
    }

    @Test
    public void updateStatus() {

    }

    @Test
    public void findAvailableDates() {
        Specialty specialty = createSpecialty();
        String medicalServiceName = "Facial treatment";
        MedicalService medicalService = createMedicalService(medicalServiceName, specialty);

        Mockito.when(medicalServiceRepository.findByName(medicalServiceName))
                .thenReturn(Optional.of(medicalService));

        Account doctorAccount = createAccount(AccountType.DOCTOR, "doctor", "doctor123P#", 1);

        Mockito.when(accountRepository.findByUsername("doctor"))
                .thenReturn(Optional.of(doctorAccount));
        Mockito.when(accountRepository.findById(1))
                .thenReturn(Optional.of(doctorAccount));

        String doctorFirstName = "Peter";
        String doctorLastName = "Parker";

        DoctorProfile doctorProfile = createDoctorProfile(doctorAccount, specialty, doctorFirstName, doctorLastName);

        List<Appointment> existingAppointments = new ArrayList<>();
        existingAppointments.add(createAppointment(1, doctorProfile, medicalService, LocalDateTime.now().plusDays(1).withHour(9).withMinute(30)));
        existingAppointments.add(createAppointment(2, doctorProfile, medicalService, LocalDateTime.now().plusDays(1).withHour(12).withMinute(0)));
        existingAppointments.add(createAppointment(3, doctorProfile, medicalService, LocalDateTime.now().plusDays(1).withHour(15).withMinute(30)));

        Mockito.when(doctorRepository.findById(doctorProfile.getId()))
                .thenReturn(Optional.of(doctorProfile));

        Mockito.when(appointmentRepository.findByDoctor(doctorProfile))
                .thenReturn(existingAppointments);
        try {
            appointmentService.findAvailableDates(doctorAccount.getId(), medicalService)
                    .forEach(System.out::println);
        }catch(EntityNotFoundException e){
            e.printStackTrace();
        }
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(4, appointmentService.findAvailableDates(doctorAccount.getId(), medicalService).size()));
    }

    private Appointment createAppointment(int id, DoctorProfile doctorProfile, MedicalService medicalService, LocalDateTime localDateTime) {
        Appointment appointment = new Appointment();
        appointment.setId(id);
        appointment.setDoctor(doctorProfile);
        appointment.setAppointmentDate(localDateTime);
        appointment.setMedicalService(medicalService);
        return appointment;
    }

    private Specialty createSpecialty() {
        Specialty specialty = new Specialty();
        specialty.setName("dermatology");
        return specialty;
    }

    private MedicalService createMedicalService(String medicalServiceName, Specialty specialty) {
        MedicalService medicalService = new MedicalService();
        medicalService.setName(medicalServiceName);
        medicalService.setDuration(60);
        medicalService.setSpecialty(specialty);
        return medicalService;
    }

    private Account createAccount(AccountType accountType, String username, String password, Integer id) {
        Account account = new Account();
        account.setAccountType(accountType);
        account.setUsername(username);
        account.setPassword(password);
        account.setId(id);
        return account;
    }

    private DoctorProfile createDoctorProfile(Account doctorAccount, Specialty specialty, String doctorFirstName, String doctorLastName) {
        DoctorProfile doctorProfile = new DoctorProfile();
        doctorProfile.setAccount(doctorAccount);
        doctorProfile.setId(doctorAccount.getId());
        doctorProfile.setFirstName(doctorFirstName);
        doctorProfile.setLastName(doctorLastName);
        doctorProfile.setSpecialty(specialty);
        doctorProfile.setStartTime(LocalTime.of(8, 0));
        doctorProfile.setFinishTime(LocalTime.of(18, 0));
        return doctorProfile;
    }

    private PatientProfile createPatientProfile(Account patientAccount, String patientFirstName, String patientLastName) {
        PatientProfile patientProfile = new PatientProfile();
        patientProfile.setAccount(patientAccount);
        patientProfile.setId(patientAccount.getId());
        patientProfile.setFirstName(patientFirstName);
        patientProfile.setLastName(patientLastName);
        patientProfile.setPhone("0123456789");
        patientProfile.setEmail("maryyane@email.com");
        patientProfile.setAllergies("nuts");
        patientProfile.setBirthDate(LocalDate.of(1990, 10, 10));
        return patientProfile;
    }
}