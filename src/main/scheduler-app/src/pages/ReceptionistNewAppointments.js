import React from 'react'
import {Button, Container, Form, ListGroup, Navbar} from "react-bootstrap";
import {COMPACT_SCHEDULING_STRATEGY, ERROR, LOOSE_SCHEDULING_STRATEGY, SUCCESS} from "../actions/Utils";
import {
    ChangeSchedulingStrategy,
    LoadAllAppointmentsByStatus,
    LoadAllAppointmentsOfPatientByStatus,
    LoadAppointmentStatuses,
    LoadPastAppointmentsOfPatient, LoadUpcomingAppointmentsOfPatient, UpdateAppointmentStatus
} from "../actions/AppointmentActions";
import Notification from "../components/Notification";
import AppointmentItem from "../components/AppointmentItem";
import EditableAppointmentItem from "../components/EditableAppointmentItem";

class ReceptionistNewAppointments extends React.Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            appointments: [],
            selectedStrategy: COMPACT_SCHEDULING_STRATEGY,
            notification: {
                show: false,
                message: "",
                type: ERROR,
            }
        };
        this.handleInputChange = this.handleInputChange.bind(this);
    }

    componentDidMount() {
        LoadAllAppointmentsByStatus("")
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


    onChangeSchedulingStrategy() {
        ChangeSchedulingStrategy(this.state.selectedStrategy)
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


    render() {
        return (
            <div className="background-container-menu justify-content-center ">
                <Container>
                    <Notification show={this.state.notification.show} message={this.state.notification.message}
                                  type={this.state.notification.type}/>
                    <div className="flex justify-content-center">
                        <Navbar className="justify-content-center">
                            <Form className="d-flex">
                                <Form.Select aria-label="Scheduling strategy" className="me-2" name="selectedStrategy"
                                             onChange={this.handleInputChange}>
                                    <option value={COMPACT_SCHEDULING_STRATEGY} key={1}>{COMPACT_SCHEDULING_STRATEGY}</option>
                                    <option value={LOOSE_SCHEDULING_STRATEGY} key={2}>{LOOSE_SCHEDULING_STRATEGY}</option>
                                </Form.Select>
                                <Button variant="success" onClick={this.onChangeSchedulingStrategy}>Change strategy</Button>
                            </Form>
                        </Navbar>
                        <Container className="fluid">
                            <ListGroup variant="flush">
                                {this.state.appointments.map(item =>
                                    <ListGroup.Item key={item.id}>
                                        <EditableAppointmentItem data={item}/>
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

export default ReceptionistNewAppointments;