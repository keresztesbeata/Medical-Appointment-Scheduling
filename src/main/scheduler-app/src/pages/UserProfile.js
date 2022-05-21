import React from 'react'
import {GetCurrentUser, GetCurrentUserProfile} from "../actions/UserActions";
import SetupPatientProfile from "./SetupPatientProfile";
import SetupDoctorProfile from "./SetupDoctorProfile";
import {AUTH_DOCTOR, AUTH_PATIENT, ERROR, WARNING} from "../actions/Utils";
import Error from "./Error";
import Notification from "../components/Notification";
import ViewPatientProfile from "./ViewPatientProfile";
import ViewDoctorProfile from "./ViewDoctorProfile";

class Home extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            authority: -1,
            authenticated: false,
            hasProfile: false,
            notification:{
                show: false,
                message: "",
                type: ERROR
            }
        }
    }

    componentDidMount() {
        GetCurrentUser()
            .then(currentUserData => {
                console.log(currentUserData);
                this.setState({
                    authority: currentUserData.authority,
                    authenticated: true,
                    hasProfile: currentUserData.hasProfile,
                });

            })
            .catch(e => {
                console.log(e)
                this.state = {
                    authority: -1,
                    authenticated: true,
                }
            });
    }

    render() {
        if(!this.state.authenticated) {
            return <Notification show={true} type={ERROR} message = {"No logged in user!"}/>
        }
        switch (this.state.authority) {
            case AUTH_PATIENT:
                return (this.state.profile)? <ViewPatientProfile/> : <SetupPatientProfile/>
            case AUTH_DOCTOR:
                return (this.state.profile)? <ViewDoctorProfile/> : <SetupDoctorProfile/>
            default:
                return <Home/>
        }
    }
}

export default Home;