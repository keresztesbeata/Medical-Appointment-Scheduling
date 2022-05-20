package src.model.appointment_states;


import src.exceptions.InvalidAccessException;
import src.exceptions.InvalidStateException;
import src.model.Appointment;
import src.model.AppointmentStatus;
import src.model.users.AccountType;

public class ScheduledState extends AbstractAppointmentState {
    private static final String CANNOT_UPDATE_STATUS_ERROR_MESSAGE = "You cannot update the appointment status to Confirmed, because you do not have enough rights (you are not a patient)!";

    public ScheduledState(Appointment appointment) {
        super(appointment);
    }

    @Override
    public AbstractAppointmentState setConfirmed(AccountType accountType) throws InvalidStateException, InvalidAccessException {
        if(accountType.equals(AccountType.RECEPTIONIST)) {
            throw new InvalidAccessException(CANNOT_UPDATE_STATUS_ERROR_MESSAGE);
        }
        updateStatus(AppointmentStatus.CONFIRMED);
        return new ConfirmedState(getAppointment());
    }


}
