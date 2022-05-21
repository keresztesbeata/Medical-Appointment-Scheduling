import React from 'react'
import {Button, FormControl, FormGroup, FormLabel, FormSelect, InputGroup} from 'react-bootstrap'
import {AUTH_DOCTOR, ERROR, INFO, SUCCESS, WARNING} from "../actions/Utils";
import Notification from "../components/Notification";
import {GetCurrentUserProfile, LoadSpecialties, SetupUserProfile} from "../actions/UserActions";

class SetupDoctorProfile extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            doctorProfile: {
                firstName: "",
                lastName: "",
                specialty: "",
                startTime: new Date().toLocaleTimeString('it-IT'),
                finishTime: new Date().toLocaleTimeString('it-IT'),
            },
            specialties: [],
            notification: {
                show: false,
                message: "",
                type: ERROR,
            }
        };

        this.handleInputChange = this.handleInputChange.bind(this);
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
                console.log(currentUserData)
                this.setState({
                    doctorProfile: currentUserData,
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
                ...this.state.doctorProfile,
                [target.name]: target.value
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
                        message: "Your profile has been updated successfully!",
                        type: SUCCESS
                    }
                });
            })
            .catch(error => {
                console.log(error)
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
                    <form onSubmit={this.handleSubmit} className="card-body" id="doctor-profile-form">
                        <h3 className="card-title">Doctor profile details</h3>
                        <Notification show={this.state.notification.show} message={this.state.notification.message}
                                      type={this.state.notification.type}/>
                        <FormGroup className="mb-3">
                            <FormLabel>First name</FormLabel>
                            <FormControl type="text" required placeholder="Enter first name" name="firstName"
                                         defaultValue={this.state.doctorProfile.firstName}
                                         onChange={this.handleInputChange}/>
                        </FormGroup>
                        <FormGroup className="mb-3">
                            <FormLabel>Last name</FormLabel>
                            <FormControl type="text" required placeholder="Enter last name" name="lastName"
                                         defaultValue={this.state.doctorProfile.lastName}
                                         onChange={this.handleInputChange}/>
                        </FormGroup>
                        <FormGroup className="mb-3">
                            <FormLabel>Specialty</FormLabel>
                            <FormSelect name="specialty" onChange={this.handleInputChange} required
                                defaultValue={this.state.doctorProfile.specialty}>
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
                            <FormControl type="text" placeholder="Start time" name="startTime"
                                         defaultValue={this.state.doctorProfile.startTime}
                                         onChange={this.handleInputChange}/>
                            <InputGroup.Text>Finish time</InputGroup.Text>
                            <FormControl type="text" placeholder="Finish time" name="finishTime"
                                         defaultValue={this.state.doctorProfile.finishTime}
                                         onChange={this.handleInputChange}/>
                        </InputGroup>
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

export default SetupDoctorProfile;