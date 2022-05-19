package src.model.appointment_states;

import src.exceptions.InvalidStateException;
import src.model.Appointment;
import src.model.AppointmentStatus;

public class ConfirmedState extends AbstractAppointmentState {

    public ConfirmedState(Appointment appointment) {
        super(appointment);
    }

    @Override
    public AbstractAppointmentState setCompleted() throws InvalidStateException {
        updateStatus(AppointmentStatus.COMPLETED);
        return new CompletedState(getAppointment());
    }

    @Override
    public AbstractAppointmentState setMissed() throws InvalidStateException {
        updateStatus(AppointmentStatus.MISSED);
        return new MissedState(getAppointment());
    }

    @Override
    public AbstractAppointmentState setCanceled() throws InvalidStateException {
        updateStatus(AppointmentStatus.CANCELED);
        return new CanceledState(getAppointment());
    }
}
