import React from 'react'
import {Button, Card, Container, Form, ListGroup, Navbar, NavItem} from "react-bootstrap";
import {ERROR} from "../actions/Utils";
import {
    FUTURE_APPOINTMENTS,
    LoadAllAppointmentsOfDoctorByDate,
    LoadAppointmentStatuses,
    PAST_APPOINTMENTS,
    PRESENT_APPOINTMENTS,
} from "../actions/AppointmentActions";
import Notification from "../components/Notification";
import AppointmentItem from "../components/AppointmentItem";
import {Link} from "react-router-dom";

class DoctorViewAppointments extends React.Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            appointments: [],
            selectedDate: PRESENT_APPOINTMENTS,
            notification: {
                show: false,
                message: "",
                type: ERROR,
            }
        };
        this.onFilterAppointmentsByDate = this.onFilterAppointmentsByDate.bind(this);
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


    handleInputChange(event) {
        const target = event.target
        this.setState({
            [target.name]: target.value,
            notification: {
                show: false,
            }
        });
    }

    onFilterAppointmentsByDate() {
        LoadAllAppointmentsOfDoctorByDate(this.state.selectedDate)
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

    render() {
        return (
            <div className="background-container-menu justify-content-center ">
                <Container>
                    <div className="text-center transparent-background">
                        <h1 className="text-white">
                            My patients' appointments
                        </h1>
                    </div>
                    <Notification show={this.state.notification.show} message={this.state.notification.message}
                                  type={this.state.notification.type}/>
                    <div className="flex justify-content-center">
                        <Navbar className="justify-content-center">
                            <NavItem className="px-5">
                                <Form className="d-flex">
                                    <Form.Select aria-label="Appointment date" name="selectedDate"
                                                 onChange={this.handleInputChange}>
                                        <option value={PAST_APPOINTMENTS} key={0}>Previous</option>
                                        <option value={PRESENT_APPOINTMENTS} key={1}>Today</option>
                                        <option value={FUTURE_APPOINTMENTS} key={2}>Upcoming</option>
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
                                            <div className="text-center">
                                                <Button variant="outline-info" id="patient_setup_profile_btn">
                                                    <Link to={"/doctor/add_prescription?appointmentId="+item.id} className="text-decoration-none">
                                                        Add prescription
                                                    </Link>
                                                </Button>
                                            </div>
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

export default DoctorViewAppointments;