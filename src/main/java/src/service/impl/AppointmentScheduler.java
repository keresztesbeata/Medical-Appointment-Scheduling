package src.service.impl;

import org.apache.commons.lang3.tuple.ImmutablePair;
import src.model.Appointment;
import src.model.MedicalService;
import src.model.users.DoctorProfile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentScheduler {

    public List<LocalDateTime> findAvailableSpots(MedicalService medicalService, DoctorProfile doctorProfile, List<Appointment> existingAppointments) {
        // list of available dates and the max timespan until the next appointment
        List<ImmutablePair<LocalDateTime, Long>> availableSpots = new ArrayList<>();
        // srt the appointments by oldest date
        List<Appointment> sortedAppointments = existingAppointments.stream()
                .filter(appointment -> appointment.getAppointmentDate().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(Appointment::getAppointmentDate)).toList();
        // iterate through the appointments with an iterator to find the next available free spot between 2 dates
        Iterator<Appointment> iterator = sortedAppointments.listIterator();
        // consider as first element (reference) the first appointments start of day or the start of day of the next day (tomorrow)
        LocalDateTime prev = ((sortedAppointments.isEmpty()) ? LocalDateTime.now().plusDays(1) : sortedAppointments.get(0).getAppointmentDate())
                .toLocalDate().atStartOfDay().plusHours(doctorProfile.getStartTime().getHour());
        // get the time at which the first appointment gets completed if it is the case
        if (!sortedAppointments.isEmpty()) {
            prev = prev.plusMinutes(sortedAppointments.get(0).getMedicalService().getDuration());
        }
        while (iterator.hasNext()) {
            Appointment curr = iterator.next();
            long availableTime = (Duration.between(curr.getAppointmentDate(), prev)).toMinutes();
            // check if the available time spot would be enough to
            if (availableTime > medicalService.getDuration()) {
                availableSpots.add(new ImmutablePair<>(curr.getAppointmentDate(), availableTime));
            }
            prev = curr.getAppointmentDate().plusMinutes(curr.getMedicalService().getDuration());
        }
        // add the last appointment too if it still fits in the schedule of the doctor
        long availableTime = doctorProfile.getFinishTime().getHour() - prev.getHour();
        if (availableTime > 0) {
            availableSpots.add(new ImmutablePair<>(prev, availableTime));
        }
        // sort the available time spots by min duration in order to fill up as many space as possible in the timetable (to have a tight schedule, packed up with appointments
        // instead of a sparse schedule with few appointments per day
        // todo: refine using Strategy pattern -> ex. holiday: apply a looser strategy = fewer appointments/day with more breaks
        return availableSpots.stream()
                .sorted(Comparator.comparing(s -> s.right))
                .map(ImmutablePair::getLeft)
                .collect(Collectors.toList());
    }
}
