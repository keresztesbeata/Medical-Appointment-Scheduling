package src.service.impl.schedule;

import org.apache.commons.lang3.tuple.ImmutablePair;
import src.model.Appointment;
import src.model.MedicalService;
import src.model.users.DoctorProfile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Schedules appointments in a compact manner, very close to each other with the intent to fill up the free time slots between dispersed appointment dates.
 * If there is enough time between 2 existing appointments in order to add the new appointment, then it will be considered as a positive candidate.
 */
public class CompactSchedulingStrategy implements SchedulingStrategy {

    @Override
    public List<LocalDateTime> findAvailableSpots(MedicalService medicalService, DoctorProfile doctorProfile, List<Appointment> existingAppointments) {
        // list of available dates and the max timespan until the next appointment
        List<ImmutablePair<LocalDateTime, Long>> availableSpots = new ArrayList<>();

        // sort the appointments by earliest date first
        List<Appointment> sortedAppointments = existingAppointments.stream()
                // check if the existing appointments are already in scheduled state <=> they have their date field set
                .filter(appointment -> appointment.getAppointmentDate() != null && appointment.getAppointmentDate().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(Appointment::getAppointmentDate)).toList();

        // iterate through the appointments with an iterator to find the next available free spot between 2 dates
        Iterator<Appointment> iterator = sortedAppointments.listIterator();

        if (sortedAppointments.isEmpty()) {
            // check if there is a free spot in the hours (leave 1 hour to let the customer be able to arrive on time)
            LocalDateTime estimatedFinishingTime = LocalDateTime.now().plusHours(1).plusMinutes(medicalService.getDuration());
            long availableTime = doctorProfile.getFinishTime().until(estimatedFinishingTime, ChronoUnit.MINUTES);
            if (availableTime >= 0) {
                return List.of(LocalDateTime.now().plusHours(1));
            } else {
                // no appointments whatsoever, but today there is no more time, so schedule it for the next day from start hour
                return List.of(LocalDateTime.now().plusDays(1).withHour(doctorProfile.getStartTime().getHour()));
            }
        }

        // consider as first element (reference) the current time + 1 hour
        LocalDateTime prev = LocalDateTime.now().plusHours(1);

        while (iterator.hasNext()) {
            Appointment curr = iterator.next();
            long availableTime = prev.until(curr.getAppointmentDate(), ChronoUnit.MINUTES);

            // check if the available time spot would be enough for the medical service and add the datetime to the list
            if (availableTime >= medicalService.getDuration()) {
                availableSpots.add(new ImmutablePair<>(prev, availableTime));
            }
            // update the prev date
            prev = curr.getAppointmentDate().plusMinutes(curr.getMedicalService().getDuration());
        }

        // add the last appointment date too if it still fits in the schedule of the doctor
        long availableTime = doctorProfile.getFinishTime().getHour() - prev.getHour();
        if (availableTime > 0) {
            availableSpots.add(new ImmutablePair<>(prev, availableTime));
        }
        // sort the available time spots by min duration in order to fill up as many space as possible in the timetable (to have a tight schedule, packed up with appointments
        // instead of a sparse schedule with few appointments per day
        return availableSpots.stream()
                .sorted(Comparator.comparing(s -> s.right))
                .map(ImmutablePair::getLeft)
                .collect(Collectors.toList());
    }
}
