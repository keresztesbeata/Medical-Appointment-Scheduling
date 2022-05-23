import React from 'react'
import {Alert, Button, FormControl, FormLabel, FormSelect, InputGroup} from 'react-bootstrap'
import {ERROR, SUCCESS, WARNING} from "../actions/Utils";
import {GetCurrentUser} from "../actions/UserActions";
import Notification from "../components/Notification";
import {FilterDoctorsByMedicalService, LoadMedicalServices, RequestNewAppointment} from "../actions/AppointmentActions";

class CreateAppointment extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            medicalServices: [],
            doctors: [],
            appointment: {
                doctor: null,
                medicalService: "",
            },
            notification: {
                show: false,
                message: "",
                type: ERROR
            }
        };

        this.handleMedicalServiceChange = this.handleMedicalServiceChange.bind(this);
        this.handleDoctorChange = this.handleDoctorChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
        LoadMedicalServices()
            .then(medicalServices => {
                this.setState({
                    ...this.state,
                    medicalServices: medicalServices,
                    notification: {
                        show: false,
                    }
                })
            })
            .catch(error => {
                this.setState({
                    notification: {
                        show: true,
                        message: error.message,
                        type: ERROR
                    }
                })
            });
    }

    handleMedicalServiceChange(event) {
        let selectedMedicalService = event.target.value

        FilterDoctorsByMedicalService(selectedMedicalService)
            .then(doctorsList => {
                if(doctorsList.length === 0) {
                    throw new Error("No doctors for service!");
                }
                this.setState({
                    ...this.state,
                    doctors: doctorsList,
                    appointment: {
                        medicalService: selectedMedicalService,
                        doctor: doctorsList[0]
                    },
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
                        type: ERROR
                    }
                })
            });

    }

    handleDoctorChange(event) {
        // prevent page from reloading
        event.preventDefault();
        const target = event.target;
        let value = target.value;

        this.setState({
            ...this.state,
            appointment: {
                ...this.state.appointment,
                doctor: value,
            },
            notification: {
                show: false
            }
        });
    }

    handleSubmit(event) {
        // prevent page from reloading
        event.preventDefault();

        let appointmentDto = {
            doctorFirstName: this.state.appointment.doctor.firstName,
            doctorLastName: this.state.appointment.doctor.lastName,
            medicalService: this.state.appointment.medicalService
        }

        RequestNewAppointment(appointmentDto)
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
                        <div className="appointment-icon"/>
                        <Notification show={this.state.notification.show} message={this.state.notification.message}
                                      type={this.state.notification.type}/>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Medical service</InputGroup.Text>
                            <FormSelect placeholder="Select medical service" name="medicalService"
                                        onChange={this.handleMedicalServiceChange} required>
                                {
                                    this.state.medicalServices.map(service =>
                                        <option value={service} key={"key_" + service}>
                                            {service}
                                        </option>
                                    )
                                }
                            </FormSelect>
                        </InputGroup>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Doctors</InputGroup.Text>
                            <FormSelect placeholder="Select doctor" name="doctor"
                                        onChange={this.handleDoctorChange} required>
                                {
                                    this.state.doctors.map(doctor =>
                                        <option value={doctor} key={"key_" + doctor.id}>
                                            {doctor.firstName + " " + doctor.lastName}
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