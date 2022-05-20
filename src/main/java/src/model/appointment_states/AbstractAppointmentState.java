package src.model.appointment_states;

import lombok.Getter;
import src.exceptions.InvalidAccessException;
import src.exceptions.InvalidStateException;
import src.model.Appointment;
import src.model.AppointmentStatus;
import src.model.users.AccountType;

/**
 * Manages the status of an Appointment by providing methods for updating/changing the status of the given appointment.
 * Initially, none of the updates are supported. Each new appointment state which is added is represented by a class extending
 * the abstract class AbstractAppointmentState and providing an implementation for any of its methods.
 * It is part of the implementation of the State Design pattern.
 */
public abstract class AbstractAppointmentState {
    private static final String STATE_UNCHANGED_ERROR_MESSAGE = "State unchanged!";

    @Getter
    private Appointment appointment;

    public AbstractAppointmentState(Appointment appointment) {
        this.appointment = appointment;
    }

    /**
     * Update the state of the appointment.
     *
     * @param newStatus the new state of the appointment
     * @throws InvalidStateException if the state of the appointment cannot be updated
     */
    protected void updateStatus(AppointmentStatus newStatus) throws InvalidStateException {
        AppointmentStatus oldStatus = appointment.getStatus();
        if (newStatus.equals(oldStatus)) {
            throw new InvalidStateException(STATE_UNCHANGED_ERROR_MESSAGE);
        }
        appointment.setStatus(newStatus);
    }

    /**
     * Change the state of the appointment to 'Scheduled'.
     * The current state can be only 'Requested'.
     * Only the Receptionist has the right to change the state to 'Scheduled'.
     *
     * @param accountType the type of user which is allowed to modify the current state of the appointment
     * @return the class representing the new state of the appointment
     * @throws InvalidStateException if the state of the appointment cannot be changed to the given new state from the current state
     * @throws InvalidAccessException if the user doesn't have the right to set the appointment's new state
     */
    public AbstractAppointmentState setScheduled(AccountType accountType) throws InvalidStateException, InvalidAccessException {
        throw new InvalidStateException("You cannot update the status of the appointment from " + appointment.getStatus() + " to SCHEDULED!");
    }

    /**
     * Change the state of the appointment to 'Confirmed'.
     * The current state can be only 'Scheduled'.
     * Only the Patient has the right to change the state to 'Confirmed'.
     *
     * @param accountType the type of user which is allowed to modify the current state of the appointment
     * @return the class representing the new state of the appointment
     * @throws InvalidStateException if the state of the appointment cannot be changed to the given new state from the current state
     * @throws InvalidAccessException if the user doesn't have the right to set the appointment's new state
     */
    public AbstractAppointmentState setConfirmed(AccountType accountType) throws InvalidStateException, InvalidAccessException {
        throw new InvalidStateException("You cannot update the status of the appointment from " + appointment.getStatus() + " to CONFIRMED!");
    }

    /**
     * Change the state of the appointment to 'Canceled'.
     * The current state can be only 'Confirmed'.
     * Only the Patient has the right to change the state to 'Canceled'.
     *
     * @param accountType the type of user which is allowed to modify the current state of the appointment
     * @return the class representing the new state of the appointment
     * @throws InvalidStateException if the state of the appointment cannot be changed to the given new state from the current state
     * @throws InvalidAccessException if the user doesn't have the right to set the appointment's new state
     */
    public AbstractAppointmentState setCanceled(AccountType accountType) throws InvalidStateException, InvalidAccessException {
        throw new InvalidStateException("You cannot update the status of the appointment from " + appointment.getStatus() + " to CANCELED!");
    }

    /**
     * Change the state of the appointment to 'Completed'.
     * The current state can be only 'Confirmed'.
     * Only the Receptionist has the right to change the state to 'Completed'.
     *
     * @param accountType the type of user which is allowed to modify the current state of the appointment
     * @return the class representing the new state of the appointment
     * @throws InvalidStateException if the state of the appointment cannot be changed to the given new state from the current state
     * @throws InvalidAccessException if the user doesn't have the right to set the appointment's new state
     */
    public AbstractAppointmentState setCompleted(AccountType accountType) throws InvalidStateException, InvalidAccessException {
        throw new InvalidStateException("You cannot update the status of the appointment from " + appointment.getStatus() + " to COMPLETED!");
    }

    /**
     * Change the state of the appointment to 'Missed'.
     * The current state can be only 'Confirmed'.
     * Only the Receptionist has the right to change the state to 'Missed'.
     *
     * @param accountType the type of user which is allowed to modify the current state of the appointment
     * @return the class representing the new state of the appointment
     * @throws InvalidStateException if the state of the appointment cannot be changed to the given new state from the current state
     * @throws InvalidAccessException if the user doesn't have the right to set the appointment's new state
     */
    public AbstractAppointmentState setMissed(AccountType accountType) throws InvalidStateException, InvalidAccessException {
        throw new InvalidStateException("You cannot update the status of the appointment from " + appointment.getStatus() + " to MISSED!");
    }
}
