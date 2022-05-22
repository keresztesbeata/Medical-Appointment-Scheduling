import React from 'react'
import {
    Accordion,
    Button,
    Card,
    Container,
    Form,
    FormControl, FormGroup, FormText,
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
                appointmentDate: new Date(),
            },
            appointmentTime: "",
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
        this.handleTimeChange = this.handleTimeChange.bind(this);
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

    handleTimeChange(event) {
        const target = event.target
        this.setState({
            ...this.state,
            appointmentTime: target.value,
            notification: {
                show: false,
            }
        });
    }

    parseDateWithFormat(date) {
        return new Date(date[0], date[1], date[2]);
    }

    handleDateChange(newDate) {
        const formattedDate = format(newDate, "yyyy-MM-dd");
        this.setState({
            ...this.state,
            appointment: {
                ...this.state.appointment,
                appointmentDate: formattedDate,
            },
            notification: {
                show: false
            }
        });
    }
    onScheduleAppointment() {
        let finalDate = this.state.appointment.appointmentDate + " " + this.state.appointmentTime;
        ScheduleAppointment(this.state.appointment.id, finalDate)
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
                            <Form>
                                <Notification show={this.state.notification.show}
                                              message={this.state.notification.message}
                                              type={this.state.notification.type}/>
                                <InputGroup className="mb-3">
                                    <InputGroup.Text>Appointment date</InputGroup.Text>
                                    <DatePicker name="appointmentDate" format="yyyy-mm-dd" placeholder="Enter date"
                                                value={this.state.appointment.appointmentDate}
                                                onChange={this.handleDateChange} />
                                </InputGroup>
                                <InputGroup className="mb-3">
                                    <InputGroup.Text>Appointment time</InputGroup.Text>
                                    <FormControl type="text" required placeholder={new Date().toLocaleTimeString('il-IT')}
                                                 name="appointmentTime"
                                                 onChange={this.handleTimeChange}/>
                                </InputGroup>
                                <FormGroup className="mb-3">
                                    <FormText>Available spots:</FormText>
                                <Container className="fluid">
                                    <ListGroup variant="flush">
                                        {this.state.availableDates.map(item =>
                                            <ListGroup.Item key={item.id} format="yyyy-mm-dd hh:mm">
                                                {(this.parseDateWithFormat(item)).toLocaleDateString()}
                                            </ListGroup.Item>
                                        )}
                                    </ListGroup>
                                </Container>
                                </FormGroup>
                                <Button variant="success" onClick={this.onScheduleAppointment}>Schedule</Button>
                            </Form>
                        </Card.Body>
                    </Accordion.Body>
                </Accordion.Item>
            </Accordion>
        )
    }
}

export default EditableAppointmentItem;