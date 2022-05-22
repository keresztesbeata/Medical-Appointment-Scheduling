import {
    BASE_URL,
    FetchRequest, FetchRequestWithNoReturnData,
    GET_REQUEST, POST_REQUEST,
} from "./Utils";
import {
    DOCTOR_CREATE_PRESCRIPTION,
    PATIENT_ALL_PRESCRIPTIONS, PATIENT_EXPORT_PRESCRIPTION,
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

export function ExportPrescriptionAsPDF(appointmentId) {
    const url = new URL(BASE_URL + PATIENT_EXPORT_PRESCRIPTION)
    const params = {
        appointmentId: appointmentId
    }
    url.search = new URLSearchParams(params).toString();

    return FetchRequestWithNoReturnData(url, POST_REQUEST);
}

export function CreatePrescription(appointmentId, prescriptionDTO) {
    const url = new URL(BASE_URL + DOCTOR_CREATE_PRESCRIPTION)
    const params = {
        appointmentId: appointmentId
    }
    url.search = new URLSearchParams(params).toString();

    return FetchRequestWithNoReturnData(url, POST_REQUEST, prescriptionDTO);
}