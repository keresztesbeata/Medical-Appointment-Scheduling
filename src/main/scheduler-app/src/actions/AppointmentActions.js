import {
    BASE_URL,
    FetchRequest,
    FetchRequestWithNoReturnData,
    GET_REQUEST,
    POST_REQUEST,
    SESSION_TOKEN
} from "./Utils";
import {
    ALL_MEDICAL_SERVICES, PATIENT_ALL_APPOINTMENT_STATUSES, PATIENT_APPOINTMENTS_HISTORY, PATIENT_NEW_APPOINTMENT,
    PATIENT_PREFIX, PATIENT_SEARCH_DOCTOR_BY_MEDICAL_SERVICE,
    PERFORM_SETUP_PROFILE,
    SEARCH_DOCTOR_BY_MEDICAL_SERVICE, SEARCH_DOCTOR_BY_NAME
} from "./ServerUrlCollection";
import {getAuthorizationPrefix} from "./UserActions";

export function LoadMedicalServices() {
    const url = BASE_URL + PATIENT_PREFIX + ALL_MEDICAL_SERVICES;

    return FetchRequest(url, GET_REQUEST);
}

export function FindDoctorsByMedicalService(medicalService) {
    const url = new URL(BASE_URL + PATIENT_PREFIX + SEARCH_DOCTOR_BY_MEDICAL_SERVICE)
    const params = {
        medicalService: medicalService
    };
    url.search = new URLSearchParams(params).toString();

    return FetchRequest(url, GET_REQUEST);
}

export function RequestNewAppointment(appointmentData) {
    const url = BASE_URL + PATIENT_NEW_APPOINTMENT;

    return FetchRequest(url, POST_REQUEST, appointmentData);
}

export function LoadAppointmentStatuses() {
    const url = BASE_URL + PATIENT_ALL_APPOINTMENT_STATUSES;

    return FetchRequest(url, GET_REQUEST);
}

export function FilterDoctorsByMedicalService(medicalService) {
    const url = new URL(BASE_URL + PATIENT_SEARCH_DOCTOR_BY_MEDICAL_SERVICE)
    const params = {
        medicalService: medicalService
    }
    url.search = new URLSearchParams(params).toString();

    return FetchRequest(url, GET_REQUEST);
}

export function FindDoctorByName(firstName, lastName) {
    const url = new URL(BASE_URL + PATIENT_PREFIX + SEARCH_DOCTOR_BY_NAME)
    const params = {
        firstName: firstName,
        lastName: lastName
    }
    url.search = new URLSearchParams(params).toString();

    return FetchRequest(url, GET_REQUEST);
}

export function LoadAllAppointmentsOfPatientByStatus(status) {
    const url = new URL(BASE_URL + PATIENT_APPOINTMENTS_HISTORY)
    const params = {
        status: status
    }
    url.search = new URLSearchParams(params).toString();
    return FetchRequest(url, GET_REQUEST);
}