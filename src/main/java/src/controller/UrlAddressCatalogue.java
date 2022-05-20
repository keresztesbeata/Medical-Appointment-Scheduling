package src.controller;

public class UrlAddressCatalogue {
    public static final String PATIENT_GET_ALL_MEDICAL_SERVICES = "/patient/medical_services/all";
    public static final String PATIENT_CREATE_APPOINTMENT = "/patient/appointment/new";
    public static final String PATIENT_UPDATE_APPOINTMENT_STATE = "/patient/appointment/update_state";
    public static final String PATIENT_GET_APPOINTMENTS = "/patient/appointments/all";
    public static final String PATIENT_GET_UPCOMING_APPOINTMENTS = "/patient/appointments/upcoming";
    public static final String PATIENT_GET_PAST_APPOINTMENTS = "/patient/appointments/history";
    public static final String PATIENT_SEARCH_DOCTOR_BY_MEDICAL_SERVICE = "/patient/search_doctor_by_medical_service";
    public static final String PATIENT_SEARCH_DOCTOR_BY_NAME = "/patient/search_doctor_by_name";
    public static final String PATIENT_GET_ALL_PRESCRIPTIONS = "/patient/prescriptions/all";
    public static final String PATIENT_EXPORT_PRESCRIPTION = "/patient/print_prescription";
    public static final String PATIENT_VIEW_PROFILE = "/patient/view_profile";
    public static final String PATIENT_SETUP_PROFILE = "/patient/setup_profile";

    public static final String RECEPTIONIST_CHANGE_SCHEDULING_STRATEGY = "/receptionist/change_strategy";
    public static final String RECEPTIONIST_SCHEDULE_APPOINTMENT = "/receptionist/appointment/schedule";
    public static final String RECEPTIONIST_UPDATE_APPOINTMENT_STATE = "/receptionist/appointment/update_state";
    public static final String RECEPTIONIST_GET_ALL_PATIENTS = "/receptionist/patients/all";
    public static final String RECEPTIONIST_GET_ALL_DOCTORS = "/receptionist/doctors/all";
    public static final String RECEPTIONIST_GET_ALL_APPOINTMENTS_OF_PATIENT = "/receptionist/patient_appointments/all";
    public static final String RECEPTIONIST_GET_AVAILABLE_APPOINTMENTS_FOR_DOCTOR = "/receptionist/available_appointments";
    public static final String RECEPTIONIST_GET_ALL_APPOINTMENTS_OF_DOCTOR = "/receptionist/doctor_appointments/all";
    public static final String RECEPTIONIST_GET_APPOINTMENTS_OF_DOCTOR_BY_GIVEN_DATE = "/receptionist/doctor_appointments/date";

    public static final String DOCTOR_GET_ALL_APPOINTMENTS = "/doctor/appointments/all";
    public static final String DOCTOR_GET_APPOINTMENTS_OF_TODAY = "/doctor/appointments/today";
    public static final String DOCTOR_SEARCH_PATIENT_BY_NAME = "/doctor/search_patient";
    public static final String DOCTOR_GET_PATIENTS_PRESCRIPTIONS = "/doctor/patient_prescriptions";
    public static final String DOCTOR_CREATE_PRESCRIPTION = "/doctor/prescription/new";
    public static final String DOCTOR_VIEW_PROFILE = "/doctor/view_profile";
    public static final String DOCTOR_SETUP_PROFILE = "/doctor/setup_update";
    public static final String DOCTOR_GET_ALL_SPECIALTIES = "/doctor/specialties/all";

    public static final String CURRENT_USER = "/current_user";
    public static final String PERFORM_LOGIN = "/perform_login";
    public static final String PERFORM_LOGOUT = "/perform_logout";
    public static final String PERFORM_REGISTER = "/perform_register";
    public static final String HOME = "/";
    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";
    public static final String LOGOUT = "/logout";
}
