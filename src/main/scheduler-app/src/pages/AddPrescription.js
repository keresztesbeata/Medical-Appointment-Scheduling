import React from 'react'
import {Alert, Button, FormControl, FormGroup, FormLabel, FormSelect, InputGroup} from 'react-bootstrap'
import {ERROR, SUCCESS, WARNING} from "../actions/Utils";
import {GetCurrentUser} from "../actions/UserActions";
import Notification from "../components/Notification";
import {
    FindAppointmentById,
} from "../actions/AppointmentActions";
import {CreatePrescription} from "../actions/PrescriptionActions";

class AddPrescription extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            prescription: {
                medication: "",
                indications: "",
                patientFirstName: "",
                patientLastName: "",
                appointmentDate: "",
                medicalService: "",
            },
            appointmentId: 0,
            notification: {
                show: false,
                message: "",
                type: ERROR
            }
        };

        this.handleSubmit = this.handleSubmit.bind(this);
        this.parseDateWithFormat = this.parseDateWithFormat.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this);
    }

    componentDidMount() {
        const id = new URLSearchParams(window.location.search).get('appointmentId');
        FindAppointmentById(id)
            .then(data => {
                this.setState({
                    appointmentId: id,
                    prescription: {
                        ...this.state.prescription,
                        patientFirstName: data.patientFirstName,
                        patientLastName: data.patientLastName,
                        appointmentDate: data.appointmentDate,
                        medicalService: data.medicalService
                    },
                    notification: {
                        show: false,
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

    handleSubmit(event) {
        // prevent page from reloading
        event.preventDefault();

        CreatePrescription(this.state.appointmentId, this.state.prescription)
            .then(() => {
                this.setState({
                    notification: {
                        show: true,
                        message: "The prescription has been successfully added!",
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

        document.getElementById("create-prescription-form").reset();
    }

    parseDateWithFormat(date) {
        return new Date(date[0], date[1]-1, date[2], date[3], date[4]).toLocaleString();
    }

    handleInputChange(event) {
        const target = event.target
        this.setState({
            ...this.state,
            prescription: {
                ...this.state.prescription,
                [target.name]: target.value,
            },
            notification: {
                show: false,
            }
        });
    }

    render() {
        return (
            <div className="background-container-food bg-image d-flex justify-content-center align-items-center">
                <div className="card col-sm-5 border-dark text-left">
                    <form onSubmit={this.handleSubmit} className="card-body" id="create-prescription-form">
                        <h3 className="card-title text-center">Create prescription</h3>
                        <div className="prescription-icon"/>
                        <Notification show={this.state.notification.show} message={this.state.notification.message}
                                      type={this.state.notification.type}/>
                            <InputGroup className="mb-3">
                                <InputGroup.Text>Patient first name</InputGroup.Text>
                                <FormControl type="text"
                                             defaultValue={this.state.prescription.patientFirstName}
                                             disabled/>
                            </InputGroup>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Patient last name</InputGroup.Text>
                            <FormControl type="text"
                                         value={this.state.prescription.patientLastName}
                                         disabled/>
                        </InputGroup>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Appointment date</InputGroup.Text>
                            <FormControl type="text"
                                         value={this.parseDateWithFormat(this.state.prescription.appointmentDate)}
                                         disabled/>
                        </InputGroup>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Medical service</InputGroup.Text>
                            <FormControl type="text"
                                         value={this.state.prescription.medicalService}
                                         disabled/>
                        </InputGroup>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Medication</InputGroup.Text>
                            <FormControl type="text" name="medication"
                                         onChange={this.handleInputChange}
                                         required/>
                        </InputGroup>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Indications</InputGroup.Text>
                            <FormControl type="text" name="indications"
                                         onChange={this.handleInputChange}/>
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

export default AddPrescription;