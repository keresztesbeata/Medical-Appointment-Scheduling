import {
    BASE_URL,
    FetchRequest, FetchRequestWithNoReturnData,
    GET_REQUEST,
    POST_REQUEST,
} from "./Utils";
import {
    ALL_APPOINTMENT_STATUSES,
    ALL_MEDICAL_SERVICES,
    DOCTOR_FIND_APPOINTMENT_BY_ID,
    DOCTOR_FUTURE_APPOINTMENTS,
    DOCTOR_PAST_APPOINTMENTS,
    DOCTOR_PRESENT_APPOINTMENTS,
    PATIENT_ALL_APPOINTMENTS,
    PATIENT_NEW_APPOINTMENT,
    PATIENT_PAST_APPOINTMENTS,
    PATIENT_UPCOMING_APPOINTMENTS,
    PATIENT_UPDATE_APPOINTMENT_STATE,
    RECEPTIONIST_ALL_APPOINTMENTS_BY_STATUS,
    RECEPTIONIST_AVAILABLE_APPOINTMENTS,
    RECEPTIONIST_CHANGE_SCHEDULING_STRATEGY,
    RECEPTIONIST_SCHEDULE_APPOINTMENT,
    RECEPTIONIST_UPDATE_APPOINTMENT_STATE,
    SEARCH_DOCTOR_BY_MEDICAL_SERVICE,
    SEARCH_DOCTOR_BY_NAME,
    SEARCH_PATIENT_BY_NAME
} from "./ServerUrlCollection";

export const PAST_APPOINTMENTS = "PAST"
export const PRESENT_APPOINTMENTS = "PRESENT"
export const FUTURE_APPOINTMENTS = "FUTURE"

export function LoadMedicalServices() {
    const url = BASE_URL + ALL_MEDICAL_SERVICES;

    return FetchRequest(url, GET_REQUEST);
}

export function RequestNewAppointment(appointmentData) {
    const url = BASE_URL + PATIENT_NEW_APPOINTMENT;

    return FetchRequestWithNoReturnData(url, POST_REQUEST, appointmentData);
}

export function LoadAppointmentStatuses() {
    const url = BASE_URL + ALL_APPOINTMENT_STATUSES;

    return FetchRequest(url, GET_REQUEST);
}

export function FilterDoctorsByMedicalService(medicalService) {
    const url = new URL(BASE_URL + SEARCH_DOCTOR_BY_MEDICAL_SERVICE)
    const params = {
        medicalService: medicalService
    }
    url.search = new URLSearchParams(params).toString();

    return FetchRequest(url, GET_REQUEST);
}

export function FindDoctorByName(firstName, lastName) {
    const url = new URL(BASE_URL + SEARCH_DOCTOR_BY_NAME)
    const params = {
        firstName: firstName,
        lastName: lastName
    }
    url.search = new URLSearchParams(params).toString();

    return FetchRequest(url, GET_REQUEST);
}

export function FindPatientByName(firstName, lastName) {
    const url = new URL(BASE_URL + SEARCH_PATIENT_BY_NAME)
    const params = {
        firstName: firstName,
        lastName: lastName
    }
    url.search = new URLSearchParams(params).toString();

    return FetchRequest(url, GET_REQUEST);
}

export function LoadAllAppointmentsOfPatientByStatus(status) {
    const url = new URL(BASE_URL + PATIENT_ALL_APPOINTMENTS)
    const params = {
        status: status
    }
    url.search = new URLSearchParams(params).toString();

    return FetchRequest(url, GET_REQUEST);
}

export function LoadPastAppointmentsOfPatient() {
    const url = BASE_URL + PATIENT_PAST_APPOINTMENTS;

    return FetchRequest(url, GET_REQUEST);
}

export function LoadUpcomingAppointmentsOfPatient() {
    const url = BASE_URL + PATIENT_UPCOMING_APPOINTMENTS;

    return FetchRequest(url, GET_REQUEST);
}

export function LoadNewAppointments() {
    const url = new URL(BASE_URL + RECEPTIONIST_ALL_APPOINTMENTS_BY_STATUS)
    const params = {
        status: "REQUESTED"
    }
    url.search = new URLSearchParams(params).toString();

    return FetchRequest(url, GET_REQUEST);
}

export function LoadAllAppointmentsByStatus(status) {
    const url = new URL(BASE_URL + RECEPTIONIST_ALL_APPOINTMENTS_BY_STATUS)
    const params = {
        status: status
    }
    url.search = new URLSearchParams(params).toString();

    return FetchRequest(url, GET_REQUEST);
}

export function PatientUpdateAppointmentStatus(appointmentId, newState) {
    const url = new URL(BASE_URL + PATIENT_UPDATE_APPOINTMENT_STATE)
    const params = {
        appointmentId: appointmentId,
        newState: newState
    }
    url.search = new URLSearchParams(params).toString();

    return FetchRequestWithNoReturnData(url, POST_REQUEST);
}

export function ReceptionistUpdateAppointmentStatus(appointmentId, newState) {
    const url = new URL(BASE_URL + RECEPTIONIST_UPDATE_APPOINTMENT_STATE)
    const params = {
        appointmentId: appointmentId,
        newState: newState
    }
    url.search = new URLSearchParams(params).toString();

    return FetchRequestWithNoReturnData(url, POST_REQUEST);
}

export function ScheduleAppointment(appointmentId, appointmentDate) {
    const url = new URL(BASE_URL + RECEPTIONIST_SCHEDULE_APPOINTMENT)
    const params = {
        appointmentId: appointmentId
    }
    url.search = new URLSearchParams(params).toString();

    return FetchRequest(url, POST_REQUEST, appointmentDate);
}

export function LoadAvailableAppointmentDates(firstName, lastName, medicalService) {
    const url = new URL(BASE_URL + RECEPTIONIST_AVAILABLE_APPOINTMENTS)
    const params = {
        firstName: firstName,
        lastName: lastName,
        medicalService: medicalService
    }
    url.search = new URLSearchParams(params).toString();

    return FetchRequest(url, GET_REQUEST);
}

export function ChangeSchedulingStrategy(strategy) {
    const url = new URL(BASE_URL + RECEPTIONIST_CHANGE_SCHEDULING_STRATEGY)
    const params = {
        strategy: strategy
    }
    url.search = new URLSearchParams(params).toString();

    return FetchRequestWithNoReturnData(url, POST_REQUEST);
}

function mapDateTypeToUrl(date) {
    switch(date) {
        case PAST_APPOINTMENTS: return DOCTOR_PAST_APPOINTMENTS;
        case PRESENT_APPOINTMENTS: return DOCTOR_PRESENT_APPOINTMENTS;
        case FUTURE_APPOINTMENTS: return DOCTOR_FUTURE_APPOINTMENTS;
    }
}

export function LoadAllAppointmentsOfDoctorByDate(date) {
    const url = BASE_URL + mapDateTypeToUrl(date);

    return FetchRequest(url, GET_REQUEST);
}

export function FindAppointmentById(appointmentId) {
    const url = new URL(BASE_URL + DOCTOR_FIND_APPOINTMENT_BY_ID)
    const params = {
        appointmentId: appointmentId
    }
    url.search = new URLSearchParams(params).toString();

    return FetchRequest(url, GET_REQUEST);
}