package src.controller;

public class RequestAddressRegistry {
    private static final String PATIENT_REQUEST_APPOINTMENT = "/patient/appointment/request";
    private static final String PATIENT_CANCEL_APPOINTMENT = "/patient/appointment/cancel";
    private static final String PATIENT_CONFIRM_APPOINTMENT = "/patient/appointment/confirm";
    private static final String PATIENT_VIEW_APPOINTMENTS = "/patient/appointments/all";
    private static final String PATIENT_VIEW_UPCOMING_APPOINTMENTS = "/patient/appointments/upcoming";
    private static final String PATIENT_VIEW_PAST_APPOINTMENTS = "/patient/appointments/history";
    private static final String PATIENT_VIEW_DOCTOR_PROFILE = "/patient/search_doctor";
    private static final String PATIENT_VIEW_ALL_PRESCRIPTIONS = "/patient/prescriptions/all";

    private static final String RECEPTIONIST_CREATE_APPOINTMENT = "/receptionist/appointment/new";
    private static final String RECEPTIONIST_UPDATE_APPOINTMENT = "/receptionist/appointment/update";
    private static final String RECEPTIONIST_DELETE_APPOINTMENT = "/receptionist/appointment/delete";
    private static final String RECEPTIONIST_CHECK_IN_PATIENT = "/receptionist/patient_check_in";
    private static final String RECEPTIONIST_VIEW_ALL_APPOINTMENTS_OF_PATIENT = "/receptionist/patient_appointments/all";
    private static final String RECEPTIONIST_VIEW_APPOINTMENTS_OF_PATIENT_BY_GIVEN_DATE = "/receptionist/patient_appointments/date";
    private static final String RECEPTIONIST_VIEW_SCHEDULE_OF_DOCTOR = "/receptionist/doctor_schedule";
    private static final String RECEPTIONIST_VIEW_ALL_APPOINTMENTS_OF_DOCTOR = "/receptionist/doctor_appointments/all";
    private static final String RECEPTIONIST_VIEW_APPOINTMENTS_OF_DOCTOR_BY_GIVEN_DATE = "/receptionist/doctor_appointments/date";

    private static final String DOCTOR_VIEW_ALL_APPOINTMENTS = "/doctor/appointments/all";
    private static final String DOCTOR_VIEW_APPOINTMENTS_BY_GIVEN_DATE = "/doctor/appointments/date";
    private static final String DOCTOR_VIEW_PATIENT_PROFILE = "/doctor/search_patient";
    private static final String DOCTOR_VIEW_PATIENTS_PAST_PRESCRIPTIONS = "/doctor/patient_prescriptions";
    private static final String DOCTOR_CREATE_PRESCRIPTION = "/doctor/prescription/new";
}
