import React from 'react'
import {Button, FormControl, FormGroup, FormLabel, FormSelect, InputGroup} from 'react-bootstrap'
import {ERROR, INFO, SUCCESS} from "../actions/Utils";
import Notification from "../components/Notification";
import {LoadSpecialties, SetupUserProfile} from "../actions/UserActions";

class SetupDoctorProfile extends React.Component {
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
                        message: "Set up your profile to complete the registration process!",
                        type: INFO,
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
            doctorProfile:{
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
        SetupUserProfile(this.state.doctorProfile, "DOCTOR")
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
            <form onSubmit={this.props.handleSubmit} className="card-body" id="doctor-profile-form">
                <h3 className="card-title">Setup doctor profile</h3>
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

export default SetupDoctorProfile;