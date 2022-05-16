import {
    AUTH_DOCTOR, AUTH_NONE,
    AUTH_PATIENT, AUTH_RECEPTIONIST,
    BASE_URL,
    FetchRequest,
    FetchRequestWithNoReturnData,
    GET_REQUEST,
    POST_REQUEST,
    SESSION_TOKEN
} from "./Utils";

export function LoginUser(username, password) {
    const url = BASE_URL + "/perform_login"

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

export function RegisterUser(username, password, asAdmin) {
    const url = new URL(BASE_URL + "/perform_register")
    const params = {
        asAdmin: asAdmin
    };
    url.search = new URLSearchParams(params).toString();

    const data = {
        username: username,
        password: password
    }

    return FetchRequest(url, POST_REQUEST, data, false);
}

export function LogoutUser() {
    const url = BASE_URL + "/perform_logout"

    return FetchRequestWithNoReturnData(url, POST_REQUEST)
        .then(() => {
            localStorage.removeItem(SESSION_TOKEN)
        });
}

export function GetCurrentUser() {
    const url = BASE_URL + "/current_user";

    return FetchRequest(url, GET_REQUEST)
        .then(currentUserData => {
                return {
                    id: currentUserData.id,
                    profile: currentUserData.profile,
                    authority: currentUserData.userRole
                }
            }
        );
}