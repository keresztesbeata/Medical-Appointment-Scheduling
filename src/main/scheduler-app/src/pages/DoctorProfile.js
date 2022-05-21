import React from 'react'
import {Button, FormControl, FormGroup, FormLabel, FormSelect, InputGroup} from 'react-bootstrap'
import {AUTH_DOCTOR, ERROR, INFO, SUCCESS, WARNING} from "../actions/Utils";
import Notification from "../components/Notification";
import {GetCurrentUserProfile, LoadSpecialties, SetupUserProfile} from "../actions/UserActions";

class DoctorProfile extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            doctorProfile: {
                firstName: "",
                lastName: "",
                specialty: "",
                startTime: null,
                finishTime: null,
            },
            specialties: [],
            notification: {
                show: false,
                message: "",
                type: ERROR,
            }
        };

        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleNumericInputChange = this.handleNumericInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
        LoadSpecialties()
            .then(data => {
                this.setState({
                    ...this.state,
                    specialties: data,
                });
            })
            .catch(error => {
                this.setState({
                    notification: {
                        show: true,
                        message: error.message,
                        type: ERROR,
                    }
                });
            });
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
            ...this.state,
            doctorProfile: {
                ...this.state,
                [target.name]: target.value
            },
            notification: {
                show: false
            }
        });
    }

    handleNumericInputChange(event) {
        // prevent page from reloading
        event.preventDefault();
        const target = event.target;
        let value = target.value;

        if (isNaN(value)) {
            this.setState({
                notification: {
                    show: true,
                    message: "Input must be a number!",
                    type: ERROR,
                },
            });
            return;
        }

        this.setState({
            ...this.state,
            restaurant: {
                ...this.state.restaurant,
                [target.name]: value,
            },
            notification: {
                show: false
            }
        });
    }


    handleSubmit(event) {
        // prevent page from reloading
        event.preventDefault();
        SetupUserProfile(this.state.doctorProfile, AUTH_DOCTOR)
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

        document.getElementById("doctor-profile-form").reset();
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit} className="card-body" id="doctor-profile-form">
                <h3 className="card-title">Setup doctor profile</h3>
                <Notification show={this.state.notification.show} message = {this.state.notification.message} type={this.state.notification.type}/>
                <FormGroup className="mb-3">
                    <FormLabel>First name</FormLabel>
                    <FormControl type="text" required placeholder="Enter first name" name="firstName"
                                 onChange={this.handleInputChange}/>
                </FormGroup>
                <FormGroup className="mb-3">
                    <FormLabel>Last name</FormLabel>
                    <FormControl type="password" required placeholder="Enter last name" name="lastName"
                                 onChange={this.handleInputChange}/>
                </FormGroup>
                <FormGroup className="mb-3">
                    <FormLabel>Specialty</FormLabel>
                    <FormSelect name="specialty" onChange={this.handleInputChange} required>
                        <option value="" key={-1}>Select specialty ...</option>
                        {
                            this.state.specialties.map(specialty =>
                                <option value={specialty} key={specialty}>{specialty}</option>
                            )
                        }
                    </FormSelect>
                </FormGroup>
                <InputGroup className="mb-3">
                    <InputGroup.Text>Start time</InputGroup.Text>
                    <FormControl type="text" placeholder="Start time"
                                 defaultValue={new Date().toLocaleTimeString()} name="startTime"
                                 onChange={this.handleInputChange}/>
                    <InputGroup.Text>Finish time</InputGroup.Text>
                    <FormControl type="text" placeholder="Finish time" name="finishTime"
                                 onChange={this.handleInputChange}/>
                </InputGroup>
                <div className="text-center">
                    <Button variant="secondary" type="submit">
                        Save
                    </Button>
                </div>
            </form>
        )
    }
}

export default DoctorProfile;