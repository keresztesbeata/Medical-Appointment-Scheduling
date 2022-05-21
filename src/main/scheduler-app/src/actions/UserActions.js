import {
    AUTH_PATIENT,
    BASE_URL,
    FetchRequest,
    FetchRequestWithNoReturnData,
    GET_REQUEST,
    POST_REQUEST,
    SESSION_TOKEN
} from "./Utils";
import {
    CURRENT_USER, CURRENT_USER_PROFILE, DOCTOR_PREFIX, PATIENT_PREFIX,
    PERFORM_LOGIN,
    PERFORM_LOGOUT,
    PERFORM_REGISTER,
    PERFORM_SETUP_PROFILE, VIEW_PROFILE
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

export function LoadSpecialties() {
    const url = BASE_URL + VIEW_PROFILE

    return FetchRequest(url, GET_REQUEST);
}

export function SetupUserProfile(profileData, accountType) {
    const url = BASE_URL + ((accountType === AUTH_PATIENT) ? PATIENT_PREFIX : DOCTOR_PREFIX) + PERFORM_SETUP_PROFILE

    return FetchRequest(url, POST_REQUEST, profileData);
}

export function LogoutUser() {
    const url = BASE_URL + PERFORM_LOGOUT

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
                    authority: currentUserData.accountType
                }
            }
        );
}

export function GetCurrentUserProfile() {
    const url = BASE_URL + CURRENT_USER_PROFILE;

    return FetchRequest(url, GET_REQUEST);
}