package src.model.appointment_states;

import src.exceptions.InvalidAccessException;
import src.exceptions.InvalidStateException;
import src.model.Appointment;
import src.model.AppointmentStatus;
import src.model.users.AccountType;

public class ConfirmedState extends AbstractAppointmentState {
    private static final String CANNOT_UPDATE_STATUS_ERROR_MESSAGE = "You cannot update the appointment status to %s, because you do not have enough rights (you are not a %s})!";

    public ConfirmedState(Appointment appointment) {
        super(appointment);
    }

    @Override
    public AbstractAppointmentState setCompleted(AccountType accountType) throws InvalidStateException, InvalidAccessException {
        if (accountType.equals(AccountType.PATIENT)) {
            throw new InvalidAccessException(String.format(CANNOT_UPDATE_STATUS_ERROR_MESSAGE, "Completed", "receptionist"));
        }
        updateStatus(AppointmentStatus.COMPLETED);
        return new CompletedState(getAppointment());
    }

    @Override
    public AbstractAppointmentState setMissed(AccountType accountType) throws InvalidStateException, InvalidAccessException {
        if (accountType.equals(AccountType.PATIENT)) {
            throw new InvalidAccessException(String.format(CANNOT_UPDATE_STATUS_ERROR_MESSAGE, "Missed", "receptionist"));
        }
        updateStatus(AppointmentStatus.MISSED);
        return new MissedState(getAppointment());
    }

    @Override
    public AbstractAppointmentState setCanceled(AccountType accountType) throws InvalidStateException, InvalidAccessException {
        if (accountType.equals(AccountType.RECEPTIONIST)) {
            throw new InvalidAccessException(String.format(CANNOT_UPDATE_STATUS_ERROR_MESSAGE, "Canceled", "patient"));
        }
        updateStatus(AppointmentStatus.CANCELED);
        return new CanceledState(getAppointment());
    }
}
