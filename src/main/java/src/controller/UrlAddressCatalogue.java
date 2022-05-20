package src.controller;

public class UrlAddressCatalogue {
    public static final String PATIENT_CREATE_APPOINTMENT = "/patient/appointment/new";
    public static final String PATIENT_CANCEL_APPOINTMENT = "/patient/appointment/cancel";
    public static final String PATIENT_CONFIRM_APPOINTMENT = "/patient/appointment/confirm";
    public static final String PATIENT_VIEW_APPOINTMENTS = "/patient/appointments/all";
    public static final String PATIENT_VIEW_UPCOMING_APPOINTMENTS = "/patient/appointments/upcoming";
    public static final String PATIENT_VIEW_PAST_APPOINTMENTS = "/patient/appointments/history";
    public static final String PATIENT_VIEW_DOCTOR_PROFILE = "/patient/search_doctor";
    public static final String PATIENT_VIEW_ALL_PRESCRIPTIONS = "/patient/prescriptions/all";
    public static final String PATIENT_EXPORT_PRESCRIPTION = "/patient/print_prescription";
    public static final String PATIENT_VIEW_PROFILE = "/patient/my_profile";
    public static final String PATIENT_UPDATE_PROFILE = "/patient/update_profile";

    public static final String RECEPTIONIST_SCHEDULE_APPOINTMENT = "/receptionist/appointment/schedule";
    public static final String RECEPTIONIST_UPDATE_APPOINTMENT = "/receptionist/appointment/update";
    public static final String RECEPTIONIST_DELETE_APPOINTMENT = "/receptionist/appointment/delete";
    public static final String RECEPTIONIST_CHECK_IN_PATIENT = "/receptionist/patient_check_in";
    public static final String RECEPTIONIST_VIEW_ALL_APPOINTMENTS_OF_PATIENT = "/receptionist/patient_appointments/all";
    public static final String RECEPTIONIST_VIEW_APPOINTMENTS_OF_PATIENT_BY_GIVEN_DATE = "/receptionist/patient_appointments/date";
    public static final String RECEPTIONIST_VIEW_SCHEDULE_OF_DOCTOR = "/receptionist/doctor_schedule";
    public static final String RECEPTIONIST_VIEW_ALL_APPOINTMENTS_OF_DOCTOR = "/receptionist/doctor_appointments/all";
    public static final String RECEPTIONIST_VIEW_APPOINTMENTS_OF_DOCTOR_BY_GIVEN_DATE = "/receptionist/doctor_appointments/date";

    public static final String DOCTOR_VIEW_ALL_APPOINTMENTS = "/doctor/appointments/all";
    public static final String DOCTOR_VIEW_APPOINTMENTS_BY_GIVEN_DATE = "/doctor/appointments/date";
    public static final String DOCTOR_VIEW_PATIENT_PROFILE = "/doctor/patient_profile";
    public static final String DOCTOR_VIEW_PATIENTS_PAST_PRESCRIPTIONS = "/doctor/patient_prescriptions";
    public static final String DOCTOR_CREATE_PRESCRIPTION = "/doctor/prescription/new";
    public static final String DOCTOR_VIEW_PROFILE = "/doctor/my_profile";
    public static final String DOCTOR_UPDATE_PROFILE = "/doctor/update_profile";

    public static final String CURRENT_USER = "/current_user";
    public static final String PERFORM_LOGIN = "/perform_login";
    public static final String PERFORM_LOGOUT = "/perform_logout";
    public static final String PERFORM_REGISTER = "/perform_register";
    public static final String HOME = "/";
    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";
    public static final String LOGOUT = "/logout";
}
