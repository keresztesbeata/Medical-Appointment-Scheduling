package src.model.appointment_states;

import src.exceptions.InvalidAccessException;
import src.exceptions.InvalidStateException;
import src.model.Appointment;
import src.model.AppointmentStatus;
import src.model.users.AccountType;

public class RequestedState extends AbstractAppointmentState {
    public static final String MISSING_APPOINTMENT_DATE_ERROR_MESSAGE = "Appointment cannot be scheduled without setting the appointment date first!";
    private static final String CANNOT_UPDATE_STATUS_ERROR_MESSAGE = "You cannot update the appointment status to Scheduled, because you do not have enough rights (you are not a receptionist)!";

    public RequestedState(Appointment appointment) {
        super(appointment);
    }

    @Override
    public AbstractAppointmentState setScheduled(AccountType accountType) throws InvalidStateException, InvalidAccessException {
        if(accountType.equals(AccountType.PATIENT)) {
            throw new InvalidAccessException(CANNOT_UPDATE_STATUS_ERROR_MESSAGE);
        }
        if(getAppointment().getAppointmentDate() == null) {
            throw new InvalidStateException(MISSING_APPOINTMENT_DATE_ERROR_MESSAGE);
        }
        updateStatus(AppointmentStatus.SCHEDULED);
        return new ConfirmedState(getAppointment());
    }
}

