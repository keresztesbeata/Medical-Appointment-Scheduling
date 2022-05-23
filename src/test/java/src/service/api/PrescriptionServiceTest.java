package src.service.api;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import src.dto.PrescriptionDTO;
import src.exceptions.InvalidStateException;
import src.mapper.PrescriptionMapper;
import src.model.*;
import src.model.users.Account;
import src.model.users.AccountType;
import src.model.users.DoctorProfile;
import src.model.users.PatientProfile;
import src.repository.AppointmentRepository;
import src.repository.PatientRepository;
import src.repository.PrescriptionRepository;
import src.service.impl.PrescriptionServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import static src.service.api.TestComponentFactory.*;

@RunWith(MockitoJUnitRunner.class)
public class PrescriptionServiceTest {

    @Spy
    private AppointmentRepository appointmentRepository;
    @Spy
    private PrescriptionRepository prescriptionRepository;
    @Spy
    private PatientRepository patientRepository;
    @InjectMocks
    private PrescriptionServiceImpl prescriptionService;

    private PrescriptionMapper prescriptionMapper = new PrescriptionMapper();

    @Test
    public void addPrescription() {
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
        LocalDateTime appointmentDate = LocalDateTime.now().plusDays(10);

        Appointment appointment = createAppointment(appointmentId, doctorProfile, patientProfile, medicalService, AppointmentStatus.REQUESTED, appointmentDate);

        Mockito.when(appointmentRepository.findById(appointmentId))
                .thenReturn(Optional.of(appointment));

        Prescription prescription = createPrescription(appointment);
        PrescriptionDTO prescriptionDTO = prescriptionMapper.mapToDto(prescription);

        Mockito.when(prescriptionRepository.save(Mockito.any(Prescription.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        Assertions.assertThrows(InvalidStateException.class, () -> prescriptionService.addPrescription(appointmentId, prescriptionDTO));

        appointment.setStatus(AppointmentStatus.COMPLETED);

        Assertions.assertDoesNotThrow(() -> prescriptionService.addPrescription(appointmentId, prescriptionDTO));
    }
}