package src.model.appointment_states;


import src.model.Appointment;
import src.model.AppointmentStatus;

public class ScheduledState extends AbstractAppointmentState {

    public ScheduledState(Appointment appointment) {
        super(appointment);
    }

    @Override
    public AbstractAppointmentState setConfirmed() throws IllegalStateException {
        updateStatus(AppointmentStatus.CONFIRMED);
        return new ConfirmedState(getAppointment());
    }


}
