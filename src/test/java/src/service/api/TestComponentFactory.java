package src.service.api;

import src.model.*;
import src.model.users.Account;
import src.model.users.AccountType;
import src.model.users.DoctorProfile;
import src.model.users.PatientProfile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TestComponentFactory {
    public static Appointment createAppointment(int id, DoctorProfile doctorProfile, PatientProfile patientProfile, MedicalService medicalService, AppointmentStatus status, LocalDateTime localDateTime) {
        Appointment appointment = new Appointment();
        appointment.setId(id);
        appointment.setDoctor(doctorProfile);
        appointment.setPatient(patientProfile);
        appointment.setAppointmentDate(localDateTime);
        appointment.setStatus(status);
        appointment.setMedicalService(medicalService);
        appointment.setStatus(AppointmentStatus.REQUESTED);
        return appointment;
    }

    public static Specialty createSpecialty() {
        Specialty specialty = new Specialty();
        specialty.setName("dermatology");
        return specialty;
    }

    public static MedicalService createMedicalService(String medicalServiceName, Specialty specialty) {
        MedicalService medicalService = new MedicalService();
        medicalService.setName(medicalServiceName);
        medicalService.setDuration(60);
        medicalService.setSpecialty(specialty);
        return medicalService;
    }

    public static Account createAccount(AccountType accountType, String username, String password, Integer id) {
        Account account = new Account();
        account.setAccountType(accountType);
        account.setUsername(username);
        account.setPassword(password);
        account.setId(id);
        return account;
    }

    public static DoctorProfile createDoctorProfile(Account doctorAccount, Specialty specialty, String doctorFirstName, String doctorLastName) {
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

    public static PatientProfile createPatientProfile(Account patientAccount, String patientFirstName, String patientLastName) {
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

    public static Prescription createPrescription(Appointment appointment) {
        Prescription prescription = new Prescription();
        prescription.setId(appointment.getId());
        prescription.setAppointment(appointment);
        prescription.setMedication("Medicine");
        prescription.setIndications("take it x2 a day");
        return  prescription;
    }
}
