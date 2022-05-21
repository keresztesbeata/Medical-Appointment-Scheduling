import React from 'react'
import {Alert, Button, FormControl, FormLabel, FormSelect, InputGroup} from 'react-bootstrap'
import {ERROR, SUCCESS, WARNING} from "../actions/Utils";
import {GetCurrentUser} from "../actions/UserActions";
import Notification from "../components/Notification";
import {LoadMedicalServices, RequestNewAppointment} from "../actions/AppointmentActions";

class CreateAppointment extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            medicalServices: [],
            doctors: "",
            appointment: {
                doctorFirstName: "",
                doctorLastName: "",
                medicalService: "",
            },
            notification: {
                show: false,
                message: "",
                type: ERROR
            }
        };

        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
        LoadMedicalServices()
            .then(medicalServices => {
                this.setState({
                    ...this.state,
                    medicalServices: medicalServices,
                })
            })
    }

    handleInputChange(event) {
        // prevent page from reloading
        event.preventDefault();
        const target = event.target;
        let value = target.value;

        this.setState({
            ...this.state,
            appointment: {
                ...this.state.appointment,
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

        RequestNewAppointment(this.state.appointment)
            .then(() => {
                this.setState({
                    notification: {
                        show: true,
                        message: "The appointment has been successfully created!",
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

        document.getElementById("create-appointment-form").reset();
    }

    render() {
        return (
            <div className="background-container-food bg-image d-flex justify-content-center align-items-center">
                <div className="card col-sm-5 border-dark text-left">
                    <form onSubmit={this.handleSubmit} className="card-body" id="create-appointment-form">
                        <h3 className="card-title text-center">Create appointment</h3>
                        <Notification show={this.state.notification.show} message={this.state.notification.message}
                                      type={this.state.notification.type}/>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Doctor</InputGroup.Text>
                            <InputGroup.Text>First name</InputGroup.Text>
                            <FormControl type="text" required placeholder="Doctor First Name" name="doctorFirstName"
                                         onChange={this.handleInputChange}/>
                            <InputGroup.Text>Last name</InputGroup.Text>
                            <FormControl type="text" required placeholder="Doctor Last Name" name="doctorLastName"
                                         onChange={this.handleInputChange}/>
                        </InputGroup>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Medical service</InputGroup.Text>
                            <FormSelect placeholder="Select medical service" name="medicalService"
                                        onChange={this.handleInputChange} required>
                                {
                                    this.state.medicalServices.map(service =>
                                        <option value={service} key={"key_" + service}>
                                            {service}
                                        </option>
                                    )
                                }
                            </FormSelect>
                        </InputGroup>
                        <div className="text-center">
                            <Button variant="secondary" type="submit">
                                Create
                            </Button>
                        </div>
                    </form>
                </div>
            </div>
        )
    }
}

export default CreateAppointment;