import React from 'react'
import {
    Accordion,
    Button,
    Card,
    Container,
    Form,
    FormControl, FormGroup, FormLabel, FormText,
    InputGroup,
    ListGroup,
    ListGroupItem
} from 'react-bootstrap'
import Notification from "./Notification";
import {COMPACT_SCHEDULING_STRATEGY, ERROR, LOOSE_SCHEDULING_STRATEGY, SUCCESS} from "../actions/Utils";
import {LoadAvailableAppointmentDates, ScheduleAppointment} from "../actions/AppointmentActions";
import {format} from "date-fns";
import DatePicker from "react-datepicker";

class EditableAppointmentItem extends React.Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            appointment: {
                id: props.data.id,
                patientFirstName: props.data.patientFirstName,
                patientLastName: props.data.patientLastName,
                doctorFirstName: props.data.doctorFirstName,
                doctorLastName: props.data.doctorLastName,
                status: props.data.status,
                medicalService: props.data.medicalService,
            },
            appointmentDate: {
                year: new Date().getFullYear(),
                month: new Date().getMonth() + 1,
                day: new Date().getDate(),
                hours: new Date().getHours(),
                minutes: new Date().getMinutes()
            },
            availableDates: [],
            strategy: COMPACT_SCHEDULING_STRATEGY,
            notification: {
                show: false,
                message: "",
                type: ERROR
            }
        }
        this.onScheduleAppointment = this.onScheduleAppointment.bind(this);
        this.parseDateWithFormat = this.parseDateWithFormat.bind(this);
        this.handleDateChange = this.handleDateChange.bind(this);
    }

    componentDidMount() {
        LoadAvailableAppointmentDates(this.state.appointment.doctorFirstName, this.state.appointment.doctorLastName, this.state.appointment.medicalService)
            .then(freeSpots =>
                this.setState({
                    availableDates: freeSpots
                }))
            .catch(error => {
                this.setState({
                    notification: {
                        show: true,
                        message: error.message,
                        type: ERROR
                    }
                });
            })
    }

    parseDateWithFormat(date) {
        return new Date(date[0], date[1], date[2]);
    }

    handleDateChange(event) {
        event.preventDefault()
        let target = event.target

        this.setState({
            ...this.state,
            appointmentDate: {
                ...this.state.appointmentDate,
                [target.name]: target.value,
            },
            notification: {
                show: false
            }
        });
    }
    onScheduleAppointment() {
        ScheduleAppointment(this.state.appointment.id, this.state.appointmentDate)
            .then(() =>
                this.setState({
                            notification: {
                                show: true,
                                message: "Status of the appointment " + this.state.appointment.id + " has been successfully updated to SCHEDULED!",
                                type: SUCCESS
                            }
                        }))
            .catch(error => {
                this.setState({
                    notification: {
                        show: true,
                        message: error.message,
                        type: ERROR
                    }
                });
            })
    }

    render() {
        return (
            <Accordion>
                <Accordion.Item eventKey="0">
                    <Accordion.Header>
                        <Card.Body>
                            <Card.Title className="card-title">
                                Appointment #{this.state.appointment.id}
                            </Card.Title>
                            <Card.Subtitle className="mb-2 text-muted">
                                {this.state.appointment.patientFirstName + " " + this.state.appointment.patientLastName}
                            </Card.Subtitle>
                        </Card.Body>
                    </Accordion.Header>
                    <Accordion.Body>
                        <Card.Body id={this.state.appointment.id + "_collapsable"} className="collapse show"
                                   aria-labelledby="headingOne"
                                   data-parent={"#" + this.state.appointment.id + "_accordion"}>
                            <Card.Text>
                                <b>Doctor:</b> {this.state.appointment.doctorFirstName + " " + this.state.appointment.doctorLastName}
                            </Card.Text>
                            <Card.Text>
                                <b>Status:</b> {this.state.appointment.status}
                            </Card.Text>
                            <Card.Text>
                                <b>Medical service:</b> {this.state.appointment.medicalService}
                            </Card.Text>
                            <FormGroup className="mb-3">
                                <Notification show={this.state.notification.show}
                                              message={this.state.notification.message}
                                              type={this.state.notification.type}/>
                                <FormLabel>Appointment date</FormLabel>
                                <InputGroup className="mb-3">
                                    <InputGroup.Text>Year</InputGroup.Text>
                                    <FormControl type="text"
                                                 name="year"
                                                value={this.state.appointmentDate.year}
                                                onChange={this.handleDateChange} />
                                    <InputGroup.Text>Month</InputGroup.Text>
                                    <FormControl type="text"
                                                 name="month"
                                                 value={this.state.appointmentDate.month}
                                                 onChange={this.handleDateChange} />
                                    <InputGroup.Text>Day</InputGroup.Text>
                                    <FormControl type="text"
                                                 name="day"
                                                 value={this.state.appointmentDate.day}
                                                 onChange={this.handleDateChange} />
                                </InputGroup>
                                <FormLabel>Appointment time</FormLabel>
                                <InputGroup className="mb-3">
                                    <InputGroup.Text>Hours</InputGroup.Text>
                                    <FormControl type="text"
                                                 name="hours"
                                                 value={this.state.appointmentDate.hours}
                                                 onChange={this.handleDateChange} />
                                    <InputGroup.Text>Minutes</InputGroup.Text>
                                    <FormControl type="text"
                                                 name="minutes"
                                                 value={this.state.appointmentDate.minutes}
                                                 onChange={this.handleDateChange} />
                                </InputGroup>
                                <FormGroup className="mb-3">
                                    <FormText>Available spots:</FormText>
                                <Container className="fluid">
                                    <ListGroup variant="flush">
                                        {this.state.availableDates.map(item =>
                                            <ListGroup.Item key={item} format="yyyy-mm-dd hh:mm">
                                                {(this.parseDateWithFormat(item)).toLocaleDateString()}
                                            </ListGroup.Item>
                                        )}
                                    </ListGroup>
                                </Container>
                                </FormGroup>
                                <Button variant="success" onClick={this.onScheduleAppointment}>Schedule</Button>
                            </FormGroup>
                        </Card.Body>
                    </Accordion.Body>
                </Accordion.Item>
            </Accordion>
        )
    }
}

export default EditableAppointmentItem;