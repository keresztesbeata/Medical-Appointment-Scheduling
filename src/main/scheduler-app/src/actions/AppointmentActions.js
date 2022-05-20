import {
    BASE_URL,
    FetchRequest,
    FetchRequestWithNoReturnData,
    GET_REQUEST,
    POST_REQUEST,
    SESSION_TOKEN
} from "./Utils";
import {ALL_MEDICAL_SERVICES, PATIENT_PREFIX, SEARCH_DOCTOR_BY_MEDICAL_SERVICE} from "./ServerUrlCollection";

export function LoadMedicalServices() {
    const url = BASE_URL + PATIENT_PREFIX + ALL_MEDICAL_SERVICES;

    return FetchRequest(url, GET_REQUEST);
}

export function FindDoctorsByMedicalService(medicalService) {
    const url = new URL(BASE_URL + PATIENT_PREFIX + SEARCH_DOCTOR_BY_MEDICAL_SERVIC)
    const params = {
        medicalService: medicalService
    };
    url.search = new URLSearchParams(params).toString();

    return FetchRequest(url, GET_REQUEST);
}