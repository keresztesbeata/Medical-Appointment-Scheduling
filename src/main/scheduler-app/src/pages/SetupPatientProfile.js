import React from 'react'
import {Button, FormControl, FormGroup, FormLabel} from 'react-bootstrap'
import {AUTH_PATIENT, ERROR, SUCCESS, WARNING} from "../actions/Utils";
import Notification from "../components/Notification";
import {GetCurrentUserProfile, SetupUserProfile} from "../actions/UserActions";
import DatePicker from 'react-datepicker'
import {format} from "date-fns";
import "react-datepicker/dist/react-datepicker.css";

class SetupPatientProfile extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            patientProfile: {
                firstName: "",
                lastName: "",
                email: "",
                phone: "",
                birthdate: "",
                allergies: ""
            },
            notification: {
                show: false,
                message: "",
                type: ERROR,
            }
        };

        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleDateChange = this.handleDateChange.bind(this);
    }

    componentDidMount() {
        GetCurrentUserProfile()
            .then(currentUserData => {
                this.setState({
                    patientProfile: currentUserData,
                    notification: {
                        show: false
                    }
                })
            })
            .catch(error => {
                console.log(error)
                this.setState({
                    notification: {
                        show: true,
                        message: error.message,
                        type: WARNING
                    }
                });
            });
    }

    handleInputChange(event) {
        // prevent page from reloading
        event.preventDefault();
        const target = event.target;
        this.setState({
            patientProfile: {
                ...this.state.patientProfile,
                [target.name]: target.value,
            },
            notification: {
                show: false
            }
        });
    }

    handleDateChange(newDate) {
        const formattedDate = format(newDate, "yyyy-MM-dd");
        this.setState({
            patientProfile: {
                ...this.state.patientProfile,
                birthdate: formattedDate
            },
            notification: {
                show: false
            }
        });
    }

    handleSubmit(event) {
        // prevent page from reloading
        event.preventDefault();
        SetupUserProfile(this.state.patientProfile, AUTH_PATIENT)
            .then(() => {
                this.setState({
                    notification: {
                        show: true,
                        message: "Your profile has been setup successfully!",
                        type: SUCCESS
                    }
                });
            })
            .catch(error => {
                this.setState({
                    notification: {
                        show: true,
                        message: error.message,
                        type: ERROR
                    }
                });
            });
    }

    render() {
        return (
            <div className="background-container-register bg-image d-flex justify-content-center align-items-center">
                <div className="card col-sm-6 border-dark text-left">
                    <form onSubmit={this.handleSubmit} className="card-body" id="profile-setup-form">
                        <h3 className="card-title">Patient profile details</h3>
                        <Notification show={this.state.notification.show} message={this.state.notification.message}
                                      type={this.state.notification.type}/>
                        <FormGroup className="mb-3">
                            <FormLabel>First name</FormLabel>
                            <FormControl type="text" required placeholder="Enter first name" name="firstName"
                                         onChange={this.handleInputChange}/>
                        </FormGroup>
                        <FormGroup className="mb-3">
                            <FormLabel>Last name</FormLabel>
                            <FormControl type="text" required placeholder="Enter last name" name="lastName"
                                         onChange={this.handleInputChange}/>
                        </FormGroup>
                        <FormGroup className="mb-3">
                            <FormLabel>Email</FormLabel>
                            <FormControl type="text" required placeholder="Enter email" name="email"
                                         onChange={this.handleInputChange}/>
                        </FormGroup>
                        <FormGroup className="mb-3">
                            <FormLabel>Phone</FormLabel>
                            <FormControl type="text" required placeholder="Enter phone" name="phone"
                                         onChange={this.handleInputChange}/>
                        </FormGroup>
                        <FormGroup className="mb-3">
                            <FormLabel>Birthdate</FormLabel>
                            <DatePicker name="birthdate" format="yyyy-MM-dd" placeholder="Enter birthdate"
                                        value={this.state.patientProfile.birthdate}
                                         onChange={this.handleDateChange} />
                        </FormGroup>
                        <FormGroup className="mb-3">
                            <FormLabel>Allergies</FormLabel>
                            <FormControl type="text" required placeholder="Enter allergies" name="allergies"
                                         onChange={this.handleInputChange}/>
                        </FormGroup>
                        <div className="text-center">
                            <Button variant="secondary" type="submit">
                                Save
                            </Button>
                        </div>
                    </form>
                </div>
            </div>
        )
    }
}

export default SetupPatientProfile;