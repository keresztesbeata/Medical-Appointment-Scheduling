import React from 'react'
import {Button, ButtonGroup, Card, Container, Form, ListGroup, Navbar, NavItem} from "react-bootstrap";
import {ERROR, SUCCESS} from "../actions/Utils";
import {
    LoadAllAppointmentsOfPatientByStatus,
    LoadAppointmentStatuses,
    LoadPastAppointmentsOfPatient,
    LoadUpcomingAppointmentsOfPatient, PatientUpdateAppointmentStatus,
} from "../actions/AppointmentActions";
import Notification from "../components/Notification";
import AppointmentItem from "../components/AppointmentItem";

class PatientViewAppointments extends React.Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            appointments: [],
            statuses: [],
            selectedStatus: "",
            selectedDate: 0,
            medicalServices: [],
            selectedMedicalService: "",
            notification: {
                show: false,
                message: "",
                type: ERROR,
            }
        };
        this.onFilterAppointmentsByStatus = this.onFilterAppointmentsByStatus.bind(this);
        this.onFilterAppointmentsByDate = this.onFilterAppointmentsByDate.bind(this);
        this.onUpdateAppointmentStatus = this.onUpdateAppointmentStatus.bind(this);
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

    onFilterAppointmentsByStatus() {
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

    onFilterAppointmentsByDate() {
        if (this.state.selectedDate === "PAST") {
            LoadPastAppointmentsOfPatient()
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
        } else if (this.state.selectedDate === "UPCOMING") {
            LoadUpcomingAppointmentsOfPatient()
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
    }

    onUpdateAppointmentStatus(id, newStatus) {
        PatientUpdateAppointmentStatus(id, newStatus)
            .then(() => {
                LoadAllAppointmentsOfPatientByStatus(this.state.selectedStatus)
                    .then(appointmentsData => {
                        this.setState({
                            appointments: appointmentsData,
                            notification: {
                                show: true,
                                message: "Status of the appointment " + id + " has been successfully updated to " + newStatus + "!",
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
                    <div className="text-center transparent-background">
                        <h1 className="text-white">
                            My appointments
                        </h1>
                    </div>
                    <Notification show={this.state.notification.show} message={this.state.notification.message}
                                  type={this.state.notification.type}/>
                    <div className="flex justify-content-center">
                        <Navbar className="justify-content-center">
                            <NavItem className="px-5">
                                <Form className="d-flex">
                                    <Form.Select aria-label="Appointment status" name="selectedStatus"
                                                 onChange={this.handleInputChange}>
                                        <option value="" key="All">All</option>
                                        {
                                            this.state.statuses.map(status =>
                                                <option value={status} key={status}>{status}</option>
                                            )
                                        }
                                    </Form.Select>
                                    <Button variant="success" onClick={this.onFilterAppointmentsByStatus}>Filter by
                                        status</Button>
                                </Form>
                            </NavItem>
                            <NavItem className="px-5">
                                <Form className="d-flex">
                                    <Form.Select aria-label="Appointment date" name="selectedDate"
                                                 onChange={this.handleInputChange}>
                                        <option value="PAST" key={0}>Past</option>
                                        <option value="UPCOMING" key={1}>Upcoming</option>
                                    </Form.Select>
                                    <Button variant="success" onClick={this.onFilterAppointmentsByDate}>Filter by
                                        date</Button>
                                </Form>
                            </NavItem>
                        </Navbar>
                        <Container className="fluid">
                            <ListGroup variant="flush">
                                {this.state.appointments.map(item =>
                                    <ListGroup.Item key={item.id}>
                                        <Card>
                                            <AppointmentItem data={item}/>
                                            <ButtonGroup className="btn-group">
                                                <Button variant="success"
                                                        onClick={() => this.onUpdateAppointmentStatus(item.id, "CONFIRMED")}
                                                        disabled={item.status !== "SCHEDULED"}>
                                                    Confirm</Button>
                                                <Button variant="danger"
                                                        onClick={() => this.onUpdateAppointmentStatus(item.id, "CANCELED")}
                                                        disabled={item.status !== "CONFIRMED"}>
                                                    Cancel</Button>
                                            </ButtonGroup>
                                        </Card>
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

export default PatientViewAppointments;