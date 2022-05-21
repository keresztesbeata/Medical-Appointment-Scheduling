import React from 'react'
import {GetCurrentUser} from "../actions/UserActions";
import PatientProfile from "./PatientProfile";
import DoctorProfile from "./DoctorProfile";
import {AUTH_DOCTOR, AUTH_PATIENT, ERROR} from "../actions/Utils";
import Error from "./Error";
import Notification from "../components/Notification";

class Home extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            authority: -1,
            authenticated: false,
        }
    }

    componentDidMount() {
        GetCurrentUser()
            .then(currentUserData => {
                this.setState({
                    authority: currentUserData.authority,
                    authenticated: true,
                })
            })
            .catch(e => {
                this.state = {
                    authority: -1,
                    authenticated: false,
                }
            });
    }

    render() {
        if(!this.state.authenticated) {
            return <Notification show={true} type={ERROR} message = {"No logged in user!"}/>
        }
        switch (this.state.authority) {
            case AUTH_PATIENT:
                return <PatientProfile/>
            case AUTH_DOCTOR:
                return <DoctorProfile/>
            default:
                return <div/>
        }
    }
}

export default Home;