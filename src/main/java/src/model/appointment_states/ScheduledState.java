package src.model.appointment_states;


import src.exceptions.InvalidStateException;
import src.model.Appointment;
import src.model.AppointmentStatus;

public class ScheduledState extends AbstractAppointmentState {

    public ScheduledState(Appointment appointment) {
        super(appointment);
    }

    @Override
    public AbstractAppointmentState setConfirmed() throws InvalidStateException {
        updateStatus(AppointmentStatus.CONFIRMED);
        return new ConfirmedState(getAppointment());
    }


}
