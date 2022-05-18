package src.model.appointment_states;

import org.springframework.stereotype.Component;
import src.model.Appointment;

/**
 * It is responsible for instantiating the classes representing the different order statuses.
 * It represents an implementation of the Factory method.
 */
@Component
public class AppointmentStateFactory {

    /**
     * Get appropriate appointment status instance for the given appointment.
     * Parametrized factory method.
     *
     * @param appointment the appointment whose status is checked
     * @return a subclass of AbstractAppointmentState representing the given order's current state
     */
    public AbstractAppointmentState getAppointmentState(Appointment appointment) {
        switch (appointment.getStatus()) {
            case REQUESTED -> {
                return new RequestedState(appointment);
            }
            case SCHEDULED -> {
                return new ScheduledState(appointment);
            }
            case CONFIRMED -> {
                return new ConfirmedState(appointment);
            }
            case COMPLETED -> {
                return new CompletedState(appointment);
            }
            case MISSED -> {
                return new MissedState(appointment);
            }
            default -> {
                return new CanceledState(appointment);
            }
        }
    }
}
