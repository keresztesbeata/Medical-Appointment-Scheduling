package src.model.appointment_states;

import src.model.Appointment;
import src.model.AppointmentStatus;

public class RequestedState extends AbstractAppointmentState {

    public RequestedState(Appointment appointment) {
        super(appointment);
    }

    @Override
    public AbstractAppointmentState setScheduled() throws IllegalStateException {
        updateStatus(AppointmentStatus.SCHEDULED);
        return new ConfirmedState(getAppointment());
    }
}

