import {
    AUTH_DOCTOR,
    AUTH_PATIENT, AUTH_RECEPTIONIST,
    BASE_URL,
    FetchRequest,
    FetchRequestWithNoReturnData,
    GET_REQUEST,
    POST_REQUEST,
    SESSION_TOKEN
} from "./Utils";
import {
    ALL_SPECIALTIES,
    CURRENT_USER, CURRENT_USER_PROFILE, DOCTOR_PREFIX, PATIENT_PREFIX,
    PERFORM_LOGIN,
    PERFORM_LOGOUT,
    PERFORM_REGISTER,
    PERFORM_SETUP_PROFILE, RECEPTIONIST_PREFIX, VIEW_PROFILE
} from "./ServerUrlCollection";

export function LoginUser(username, password) {
    const url = BASE_URL + PERFORM_LOGIN

    const data = {
        username: username,
        password: password
    };

    return FetchRequest(url, POST_REQUEST, data, false)
        .then(data => {
            const sessionToken = JSON.stringify({
                accessToken: data.accessToken,
                tokenType: data.tokenType
            });
            localStorage.setItem(SESSION_TOKEN, sessionToken);
        });
}

export function RegisterUser(username, password, accountType) {
    const url = BASE_URL + PERFORM_REGISTER

    const data = {
        username: username,
        password: password,
        accountType: accountType
    }

    return FetchRequestWithNoReturnData(url, POST_REQUEST, data, false);
}

export function LoadUserProfile(accountType) {
    const url = BASE_URL + VIEW_PROFILE

    return FetchRequest(url, GET_REQUEST);
}

export function getAuthorizationPrefix(accountType){
    switch(accountType) {
        case AUTH_PATIENT: return PATIENT_PREFIX;
        case AUTH_DOCTOR: return DOCTOR_PREFIX;
        case AUTH_RECEPTIONIST: return RECEPTIONIST_PREFIX;
    }
}

export function SetupUserProfile(profileData, accountType) {
    const prefix = getAuthorizationPrefix(accountType);
    const url = BASE_URL + prefix + PERFORM_SETUP_PROFILE;

    return FetchRequest(url, POST_REQUEST, profileData)
        .then(response => {
            window.location.href = prefix + "/view_profile"
        });
}

export function LogoutUser() {
    const url = BASE_URL + PERFORM_LOGOUT;

    return FetchRequestWithNoReturnData(url, POST_REQUEST)
        .then(() => {
            localStorage.removeItem(SESSION_TOKEN)
        });
}

export function GetCurrentUser() {
    const url = BASE_URL + CURRENT_USER;

    return FetchRequest(url, GET_REQUEST)
        .then(currentUserData => {
                return {
                    id: currentUserData.id,
                    username: currentUserData.username,
                    authority: currentUserData.accountType,
                    hasProfile: currentUserData.hasProfile,
                }
            }
        );
}

export function GetCurrentUserProfile() {
    const url = BASE_URL + CURRENT_USER_PROFILE;

    return FetchRequest(url, GET_REQUEST);
}

export function LoadSpecialties() {
    const url = BASE_URL + DOCTOR_PREFIX + ALL_SPECIALTIES

    return FetchRequest(url, GET_REQUEST);
}