
// base server url
export const BASE_URL = 'http://localhost:8080/medplan';

export const INFO = "alert-info";
export const SUCCESS = "alert-success";
export const WARNING = "alert-warning";
export const ERROR = "alert-danger";

export const SESSION_TOKEN = 'sessionToken'

export const POST_REQUEST = "POST"
export const GET_REQUEST = "GET"

export const AUTH_PATIENT = 0;
export const AUTH_RECEPTIONIST = 1;
export const AUTH_DOCTOR = 2;

function GetSessionToken() {
    let sessionToken = JSON.parse(localStorage.getItem(SESSION_TOKEN));
    if (sessionToken === null) {
        return ""
    }
    return sessionToken.tokenType + " " + sessionToken.accessToken;
}

function HandleErrorResponse(response, errorCode, errorMessage) {
    if (response.status === errorCode) {
        window.location.href = "/error?message=" + errorMessage
    } else {
        return response;
    }
}

export function FetchRequest(url, method, body = null, authorized = true, returnData = true) {
    const requestOptions = {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        },
    };

    if (body !== null) {
        requestOptions.body = JSON.stringify(body);
    }

    if (authorized) {
        const sessionToken = GetSessionToken()
        if(sessionToken !== "") {
            requestOptions.headers["Authorization"] = sessionToken
        }
    }

    return fetch(url, requestOptions)
        .then(response => HandleErrorResponse(response, 401, "Unauthorized access! You must authenticate yourself before accessing this resource!"))
        .then(response => HandleErrorResponse(response, 403, "Forbidden! You cannot view the content of this page! You do not have the corresponding privileges."))
        .then(response => {
            if (!response.ok && response.status !== 201) {
                return response
                    .json()
                    .then(function (err) {
                        throw new Error(err.message);
                    });
            }
            if (returnData) {
                return response.json();
            }
        });
}

export function FetchRequestWithNoReturnData(url, method, body = null, authorized = true) {
    return FetchRequest(url, method, body, authorized, false);
}