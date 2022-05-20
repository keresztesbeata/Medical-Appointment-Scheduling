package src.service.impl.schedule;

import src.model.Appointment;
import src.model.MedicalService;
import src.model.users.DoctorProfile;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LooseSchedulingStrategy implements SchedulingStrategy {
    @Override
    public List<LocalDateTime> findAvailableSpots(MedicalService medicalService, DoctorProfile doctorProfile, List<Appointment> existingAppointments) {
        // list of available dates and the max timespan until the next appointment
        List<LocalDateTime> availableSpots = new ArrayList<>();

        // sort the appointments by latest date first
        List<Appointment> sortedAppointments = existingAppointments.stream()
                .filter(appointment -> appointment.getAppointmentDate().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(Appointment::getAppointmentDate).reversed()).toList();

        if (sortedAppointments.isEmpty()) {
            // check if there is a free spot in the hours (leave 1 hour to let the customer be able to arrive on time)
            LocalDateTime estimatedFinishingTime = LocalDateTime.now().plusHours(1).plusMinutes(medicalService.getDuration());
            long availableTime = doctorProfile.getFinishTime().until(estimatedFinishingTime, ChronoUnit.MINUTES);
            if(availableTime >= 0) {
                availableSpots.add(LocalDateTime.now().plusHours(1));
            }else {
                // no appointments whatsoever, but today there is no more time, so schedule it for the next day from start hour
                availableSpots.add(LocalDateTime.now().plusDays(1).withHour(doctorProfile.getStartTime().getHour()));
            }
        }else {
            Appointment last = sortedAppointments.get(0);
            // add the last appointment date too if it still fits in the schedule of the doctor
            LocalTime finishTime = last.getAppointmentDate().toLocalTime().plusMinutes(last.getMedicalService().getDuration());
            long availableTime = doctorProfile.getFinishTime().until(finishTime, ChronoUnit.MINUTES);
            if (availableTime >= 0) {
                availableSpots.add(last.getAppointmentDate().plusMinutes(last.getMedicalService().getDuration()));
            } else {
                // no more appointments can fit in the last day, so schedule it for the next day
                availableSpots.add(last.getAppointmentDate().plusDays(1).withHour(doctorProfile.getStartTime().getHour()));
            }
        }
        return availableSpots;
    }
}
