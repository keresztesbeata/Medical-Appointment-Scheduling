package src.model.appointment_states;

import src.model.Appointment;
import src.model.AppointmentStatus;

public class CompletedState extends AbstractAppointmentState {
    public CompletedState(Appointment appointment) {
        super(appointment);
    }
}
