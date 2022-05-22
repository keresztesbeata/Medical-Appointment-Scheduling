import React from 'react'
import {Alert, Button, Container, Form, Navbar, Table} from "react-bootstrap";
import {COMPACT_SCHEDULING_STRATEGY, ERROR, LOOSE_SCHEDULING_STRATEGY, SUCCESS, WARNING} from "../actions/Utils";
import AppointmentItem from "../components/AppointmentItem";
import {
    ChangeSchedulingStrategy,
    LoadAllAppointmentsByStatus,
    LoadAppointmentStatuses, UpdateAppointmentStatus
} from "../actions/AppointmentActions";
import Notification from "../components/Notification";

class ReceptionistManageAppointments extends React.Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            appointments: [],
            allAppointmentStatuses: [],
            selectedAppointmentStatus: "",
            updatedAppointmentStatus: "",
            notification: {
                show: false,
                message: "",
                type: ERROR,
            }
        };
        this.onLoadFilteredAppointments = this.onLoadFilteredAppointments.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this);
        this.onUpdateAppointmentStatus = this.onUpdateAppointmentStatus.bind(this);
    }

    componentDidMount() {
        LoadAppointmentStatuses()
            .then(data => {
                this.setState({
                    ...this.state,
                    allAppointmentStatuses: data
                });
                this.onLoadFilteredAppointments();
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

    onLoadFilteredAppointments() {
        LoadAllAppointmentsByStatus(this.state.selectedAppointmentStatus)
            .then(appointmentsData => {
                this.setState({
                    ...this.state,
                    appointments: appointmentsData
                });
                if (appointmentsData.length === 0) {
                    this.setState({
                        notification: {
                            show: true,
                            message: "No appointments were found with the status: "+this.state.selectedAppointmentStatus,
                            type: WARNING
                        }
                    })
                }
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
            ...this.state,
            [target.name]: target.value,
        });
    }

    onUpdateAppointmentStatus(id) {
        UpdateAppointmentStatus(id, this.state.updatedAppointmentStatus)
            .then(() => {
                LoadAllAppointmentsByStatus(this.state.selectedAppointmentStatus)
                    .then(appointmentsData => {
                        this.setState({
                            appointments: appointmentsData,
                            notification: {
                                show: true,
                                message: "Status of the appointment has been successfully updated to " + this.state.updatedAppointmentStatus + "!",
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

render()
{
    return (
        <div className="background-container-order bg-image justify-content-center ">
            <Container className="white-background">
                <div className="flex justify-content-center">
                    <Navbar className="justify-content-center">
                        <Form className="d-flex">
                            <Form.Select aria-label="Appointment status" className="me-2" name="selectedAppointmentStatus"
                                         onChange={this.handleInputChange}>
                                <option value="All" key="All">All</option>
                                {
                                    this.state.allAppointmentStatuses.map(state =>
                                        <option value={state} key={state}>{state}</option>
                                    )
                                }
                            </Form.Select>
                            <Button variant="success" onClick={this.onLoadFilteredAppointments}>Filter</Button>
                        </Form>
                    </Navbar>
                    <Container className="fluid">
                        <Notification show={this.state.notification.show} message={this.state.notification.message}
                                      type={this.state.notification.type}/>
                        <Table variant="flush">
                            <thead>
                            <tr>
                                <th>
                                    Appointment
                                </th>
                                <th>
                                    Status
                                </th>
                                <th>
                                    Actions
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            {this.state.appointments.map(item =>
                                <tr key={item.id}>
                                    <td>
                                        <AppointmentItem data={item}/>
                                    </td>
                                    <td>
                                        <Form className="d-flex">
                                            <Form.Select aria-label="Appointment status" className="me-2"
                                                         name="updatedAppointmentStatus"
                                                         onChange={this.handleInputChange}
                                                         defaultValue={item.status}>
                                                {
                                                    this.state.allAppointmentStatuses
                                                        .map(status =>
                                                            <option value={status}
                                                                    key={"new_" + status}>
                                                                {status}
                                                            </option>
                                                        )
                                                }
                                            </Form.Select>
                                        </Form>
                                    </td>
                                    <td>
                                        <Button variant="outline-success"
                                                onClick={() => this.onUpdateAppointmentStatus(item.id)}>
                                            Update status
                                        </Button>
                                    </td>
                                </tr>
                            )}
                            </tbody>
                        </Table>
                    </Container>
                </div>
            </Container>
        </div>
    );
}
}

export default ReceptionistManageAppointments;