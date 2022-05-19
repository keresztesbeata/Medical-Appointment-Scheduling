package src.model.appointment_states;

import src.exceptions.InvalidStateException;
import src.model.Appointment;
import src.model.AppointmentStatus;

public class RequestedState extends AbstractAppointmentState {
    public static final String MISSING_APPOINTMENT_DATE_ERROR_MESSAGE = "Appointment cannot be scheduled without setting the appointment date first!";

    public RequestedState(Appointment appointment) {
        super(appointment);
    }

    @Override
    public AbstractAppointmentState setScheduled() throws InvalidStateException {
        if(getAppointment().getAppointmentDate() == null) {
            throw new InvalidStateException(MISSING_APPOINTMENT_DATE_ERROR_MESSAGE);
        }
        updateStatus(AppointmentStatus.SCHEDULED);
        return new ConfirmedState(getAppointment());
    }
}

