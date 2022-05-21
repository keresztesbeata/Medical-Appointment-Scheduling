import React from 'react'
import {Button, Container, Form, ListGroup, Navbar} from "react-bootstrap";
import {ERROR} from "../actions/Utils";
import {LoadAllAppointmentsOfPatientByStatus, LoadAppointmentStatuses} from "../actions/AppointmentActions";
import Notification from "../components/Notification";
import AppointmentItem from "../components/AppointmentItem";

class PatientAppointmentsHistory extends React.Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            appointments: [],
            statuses: [],
            selectedStatus: "",
            doctors: [],
            doctorFirstName: "",
            doctorLastName: "",
            medicalServices: [],
            selectedMedicalService: "",
            notification: {
                show: false,
                message: "",
                type: ERROR,
            }
        };
        this.onFilterAppointments = this.onFilterAppointments.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this);
    }

    componentDidMount() {
        LoadAppointmentStatuses()
            .then(data => {
                this.setState({
                    ...this.state,
                    statuses: data
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

    onFilterAppointments() {
        LoadAllAppointmentsOfPatientByStatus(this.state.selectedStatus)
            .then(appointmentsData => {
                this.setState({
                    appointments: appointmentsData
                })
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

    handleInputChange(event) {
        const target = event.target
        this.setState({
            [target.name]: target.value,
            notification: {
                show: false,
            }
        });
    }

    render() {
        return (
            <div className="background-container-menu justify-content-center ">
                <Container>
                    <Notification show={this.state.notification.show} message={this.state.notification.message}
                                  type={this.state.notification.type}/>
                    <div className="flex justify-content-center">
                        <Navbar className="justify-content-center">
                            <Form className="d-flex">
                                <Form.Select aria-label="Appointment status" className="me-2" name="selectedStatus"
                                             onChange={this.handleInputChange}>
                                    <option value="" key="All">All</option>
                                    {
                                        this.state.statuses.map(status =>
                                            <option value={status} key={status}>{status}</option>
                                        )
                                    }
                                </Form.Select>
                                <Button variant="success" onClick={this.onFilterAppointments}>Filter</Button>
                            </Form>
                        </Navbar>
                        <Container className="fluid">
                            <ListGroup variant="flush">
                                {this.state.appointments.map(item =>
                                    <ListGroup.Item key={item.id}>
                                        <AppointmentItem data={item}/>
                                    </ListGroup.Item>
                                )}
                            </ListGroup>
                        </Container>
                    </div>
                </Container>
            </div>
        );
    }
}

export default PatientAppointmentsHistory;