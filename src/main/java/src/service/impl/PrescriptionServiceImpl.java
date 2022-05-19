package src.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.dto.PrescriptionDTO;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidStateException;
import src.mapper.PrescriptionMapper;
import src.model.Appointment;
import src.model.AppointmentStatus;
import src.model.Prescription;
import src.repository.AppointmentRepository;
import src.repository.PrescriptionRepository;
import src.service.api.PrescriptionService;

import java.util.Optional;

@Service
@Log4j2
public class PrescriptionServiceImpl implements PrescriptionService {
    private static final String APPOINTMENT_NOT_FOUND_ERROR_MESSAGE = "Appointment not found by id!";
    private static final String CANNOT_ADD_PRESCRIPTION_ERROR_MESSAGE = "Prescriptions cannot be added in the current state of the appointment!";

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    private PrescriptionMapper prescriptionMapper = new PrescriptionMapper();

    @Override
    public void addPrescription(Integer appointmentId, PrescriptionDTO prescriptionDTO) throws InvalidStateException, EntityNotFoundException {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException(APPOINTMENT_NOT_FOUND_ERROR_MESSAGE));

        if (!appointment.getStatus().equals(AppointmentStatus.COMPLETED)) {
            throw new InvalidStateException(CANNOT_ADD_PRESCRIPTION_ERROR_MESSAGE);
        }

        Prescription prescription = new Prescription();
        prescription.setMedication(prescriptionDTO.getMedication());
        prescription.setIndications(prescriptionDTO.getIndications());
        prescription.setAppointment(appointment);
        Prescription savedPrescription = prescriptionRepository.save(prescription);

        log.info("addPrescription: Prescription with id {} was successfully added to the appointment with id {}!", savedPrescription.getId(), appointment.getId());
    }

    @Override
    public Optional<PrescriptionDTO> findPrescriptionOfAppointment(Integer appointmentId) throws EntityNotFoundException {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException(APPOINTMENT_NOT_FOUND_ERROR_MESSAGE));

        return prescriptionRepository.findByAppointment(appointment).map(prescriptionMapper::mapToDto);
    }
}
