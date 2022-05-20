import React from 'react'
import {Button, FormControl, FormGroup, FormLabel, FormSelect, InputGroup} from 'react-bootstrap'
import {ERROR, SUCCESS} from "../actions/Utils";
import Notification from "../components/Notification";
import {RegisterUser, SetupUserProfile} from "../actions/UserActions";

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
    }

    handleInputChange(event) {
        // prevent page from reloading
        event.preventDefault();
        const target = event.target;
        this.setState({
            ...this.state,
            patientProfile: {
                ...this.state,
                [target.name]: target.value,
            },
            notification: {
                show: false
            }
        });
    }

    handleSubmit(event) {
        // prevent page from reloading
        event.preventDefault();
        SetupUserProfile(this.state.patientProfile, "PATIENT")
            .then(() => {
                this.setState({
                    notification: {
                        show: true,
                        message: "You have successfully completed setting up your profile!",
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

        document.getElementById("patient-profile-form").reset();
    }

    render() {
        return (
            <form onSubmit={this.props.handleSubmit} className="card-body" id="patient-profile-form">
                <h3 className="card-title">Setup patient profile</h3>
                <Notification show={this.props.notification.show} message={this.props.notification.message}
                              type={this.props.notification.type}/>
                <FormGroup className="mb-3" controlId="formBasicText">
                    <FormLabel>First name</FormLabel>
                    <FormControl type="text" required placeholder="Enter first name" name="firstName"
                                 onChange={this.handleInputChange}/>
                </FormGroup>
                <FormGroup className="mb-3" controlId="formBasicPassword">
                    <FormLabel>Last name</FormLabel>
                    <FormControl type="password" required placeholder="Enter last name" name="lastName"
                                 onChange={this.handleInputChange}/>
                </FormGroup>
                <FormGroup className="mb-3" controlId="formBasicText">
                    <FormLabel>Email</FormLabel>
                    <FormControl type="text" required placeholder="Enter email" name="email"
                                 onChange={this.handleInputChange}/>
                </FormGroup>
                <FormGroup className="mb-3" controlId="formBasicText">
                    <FormLabel>Phone</FormLabel>
                    <FormControl type="text" required placeholder="Enter last name" name="lastName"
                                 onChange={this.handleInputChange}/>
                </FormGroup>
                <FormGroup className="mb-3" controlId="formBasicText">
                <FormLabel>Birthdate</FormLabel>
                <FormControl type="text" required placeholder="Enter birth date" name="birthdate"
                             onChange={this.handleInputChange}/>
                </FormGroup>
                <FormGroup className="mb-3" controlId="formBasicText">
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
        )
    }
}

export default SetupPatientProfile;