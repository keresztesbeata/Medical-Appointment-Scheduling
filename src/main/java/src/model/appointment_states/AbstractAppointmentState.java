package src.model.appointment_states;

import lombok.Getter;
import src.model.Appointment;
import src.model.AppointmentStatus;

/**
 * Manages the status of a Restaurantappointment by providing methods for updating/changing the status of the given appointment.
 * Initially, none of the updates are supported. Each new appointment state which is added is represented by a class extending
 * the abstract class AbstractappointmentState and providing an implementation for any of its methods.
 * It is part of the implementation of the State Design pattern.
 */
public abstract class AbstractAppointmentState {

    @Getter
    private Appointment appointment;

    public AbstractAppointmentState(Appointment appointment) {
        this.appointment = appointment;
    }

    /**
     * Update the state of the appointment.
     *
     * @param appointmentStatus the new state of the appointment
     */
    protected void updateStatus(AppointmentStatus appointmentStatus) {
        appointment.setStatus(appointmentStatus);
    }

    /**
     * Change the state of the appointment to 'Scheduled'.
     * The current state can be only 'Requested'.
     *
     * @return the class representing the new state of the appointment
     * @throws IllegalStateException if the state of the appointment cannot be changed to the given new state from the current state
     */
    public AbstractAppointmentState setScheduled() throws IllegalStateException {
        throw new IllegalStateException("You cannot update the status of the appointment from "+ appointment.getStatus() + " to SCHEDULED!");
    }

    /**
     * Change the state of the appointment to 'Confirmed'.
     * The current state can be only 'Scheduled'.
     *
     * @return the class representing the new state of the appointment
     * @throws IllegalStateException if the state of the appointment cannot be changed to the given new state from the current state
     */
    public AbstractAppointmentState setConfirmed() throws IllegalStateException {
        throw new IllegalStateException("You cannot update the status of the appointment from "+ appointment.getStatus() + " to CONFIRMED!");
    }

    /**
     * Change the state of the appointment to 'Canceled'.
     * The current state can be only 'Confirmed'.
     *
     * @return the class representing the new state of the appointment
     * @throws IllegalStateException if the state of the appointment cannot be changed to the given new state from the current state
     */
    public AbstractAppointmentState setCanceled() throws IllegalStateException {
        throw new IllegalStateException("You cannot update the status of the appointment from "+ appointment.getStatus() + " to CANCELED!");
    }

    /**
     * Change the state of the appointment to 'Completed'.
     * The current state can be only 'Confirmed'.
     *
     * @return the class representing the new state of the appointment
     * @throws IllegalStateException if the state of the appointment cannot be changed to the given new state from the current state
     */
    public AbstractAppointmentState setCompleted() throws IllegalStateException {
        throw new IllegalStateException("You cannot update the status of the appointment from "+ appointment.getStatus() + " to COMPLETED!");
    }

    /**
     * Change the state of the appointment to 'Missed'.
     * The current state can be only 'Confirmed'.
     *
     * @return the class representing the new state of the appointment
     * @throws IllegalStateException if the state of the appointment cannot be changed to the given new state from the current state
     */
    public AbstractAppointmentState setMissed() throws IllegalStateException {
        throw new IllegalStateException("You cannot update the status of the appointment from "+ appointment.getStatus() + " to MISSED!");
    }
}
