package src.model.appointment_states;

import src.model.Appointment;
import src.model.AppointmentStatus;

public class ConfirmedState extends AbstractAppointmentState {

    public ConfirmedState(Appointment appointment) {
        super(appointment);
    }

    @Override
    public AbstractAppointmentState setCompleted() throws IllegalStateException {
        updateStatus(AppointmentStatus.COMPLETED);
        return new CompletedState(getAppointment());
    }

    @Override
    public AbstractAppointmentState setMissed() throws IllegalStateException {
        updateStatus(AppointmentStatus.MISSED);
        return new MissedState(getAppointment());
    }

    @Override
    public AbstractAppointmentState setCanceled() throws IllegalStateException {
        updateStatus(AppointmentStatus.CANCELED);
        return new CanceledState(getAppointment());
    }
}
