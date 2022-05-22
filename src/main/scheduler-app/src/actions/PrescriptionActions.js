import {
    BASE_URL,
    FetchRequest,
    GET_REQUEST,
} from "./Utils";
import {
    PATIENT_ALL_PRESCRIPTIONS,
    PATIENT_PRESCRIPTION_BY_ID,
} from "./ServerUrlCollection";

export function LoadAllPrescriptionsOfPatient() {
    const url = BASE_URL + PATIENT_ALL_PRESCRIPTIONS;

    return FetchRequest(url, GET_REQUEST);
}

export function LoadPrescriptionByAppointmentId(appointmentId) {
    const url = new URL(BASE_URL + PATIENT_PRESCRIPTION_BY_ID)
    const params = {
        appointmentId: appointmentId
    }
    url.search = new URLSearchParams(params).toString();

    return FetchRequest(url, GET_REQUEST);
}