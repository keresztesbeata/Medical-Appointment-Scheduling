import React from 'react'
import {Button, FormControl, FormGroup, FormLabel, FormSelect} from 'react-bootstrap'
import {RegisterUser} from "../actions/UserActions";
import {ERROR, SUCCESS} from "../actions/Utils";
import Notification from "../components/Notification";
import RegisterForm from "./RegisterForm";
import SetupPatientProfile from "./SetupPatientProfile";
import SetupDoctorProfile from "./SetupDoctorProfile";

class Register extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            accountType: "",
            notification: {
                show: false,
                message: "",
                type: ERROR,
            },
            successfulRegistration: false
        };
        //
        // this.handleInputChange = this.handleInputChange.bind(this);
        // this.handleSubmit = this.handleSubmit.bind(this);
    }

    // handleInputChange(event) {
    //     // prevent page from reloading
    //     event.preventDefault();
    //     const target = event.target;
    //     this.setState({
    //         ...this.state,
    //         [target.name]: target.value,
    //         notification: {
    //             show: false
    //         }
    //     });
    // }
    //
    // handleSubmit(event) {
    //     // prevent page from reloading
    //     event.preventDefault();
    //     RegisterUser(this.state.username, this.state.password, this.state.accountType)
    //         .then(() => {
    //             this.setState({
    //                 notification: {
    //                     show: true,
    //                     message: "You have successfully registered! Next, log in to access the application and complete the setup process!",
    //                     type: SUCCESS
    //                 }
    //             });
    //         })
    //         .catch(error => {
    //             this.setState({
    //                 notification: {
    //                     show: true,
    //                     message: error.message,
    //                     type: ERROR
    //                 }
    //             });
    //         });
    //
    //     document.getElementById("registration-form").reset();
    // }

    render() {
        return (
            <div className="background-container-register bg-image d-flex justify-content-center align-items-center">
                <div className="card col-sm-3 border-dark text-left">
                    {
                        (this.state.successfulRegistration) ?
                            ((this.state.accountType === "PATIENT")?
                                <SetupPatientProfile />
                                :
                                <SetupDoctorProfile/>
                            )
                            :
                            <RegisterForm success={this.state.successfulRegistration}/>
                    }
                </div>
            </div>
        )
    }
}

export default Register;