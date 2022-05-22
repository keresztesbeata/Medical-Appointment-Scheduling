import React from 'react'
import {
    Accordion,
    Button,
    Card,
    Container,
    Form,
    FormControl, FormText,
    InputGroup,
    ListGroup,
    ListGroupItem
} from 'react-bootstrap'
import Notification from "./Notification";
import {COMPACT_SCHEDULING_STRATEGY, ERROR, LOOSE_SCHEDULING_STRATEGY, SUCCESS} from "../actions/Utils";
import {LoadAvailableAppointmentDates, ScheduleAppointment} from "../actions/AppointmentActions";

class EditableAppointmentItem extends React.Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            id: props.data.id,
            patientFirstName: props.data.patientFirstName,
            patientLastName: props.data.patientLastName,
            doctorFirstName: props.data.doctorFirstName,
            doctorLastName: props.data.doctorLastName,
            status: props.data.status,
            appointmentDate: new Date(),
            medicalService: props.data.medicalService,
            availableDates: [],
            strategy: COMPACT_SCHEDULING_STRATEGY
        }
    }

    componentDidMount() {
        LoadAvailableAppointmentDates(this.state.doctorFirstName, this.state.doctorLastName, this.state.medicalService)
            .then()
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

    onScheduleAppointment() {
        ScheduleAppointment(this.state.id, this.state.appointmentDate)
            .then(() =>
                this.setState({
                            notification: {
                                show: true,
                                message: "Status of the appointment " + this.state.id + " has been successfully updated to SCHEDULED!",
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

    parseDateWithFormat(date) {
        return new Date(date[0], date[1], date[2]);
    }

    render() {
        return (
            <Accordion>
                <Accordion.Item eventKey="0">
                    <Accordion.Header>
                        <Card.Body>
                            <Card.Title className="card-title">
                                Appointment #{this.state.id}
                            </Card.Title>
                            <Card.Subtitle className="mb-2 text-muted">
                                {this.state.patientFirstName + " " + this.state.patientLastName}
                            </Card.Subtitle>
                        </Card.Body>
                    </Accordion.Header>
                    <Accordion.Body>
                        <Card.Body id={this.state.id + "_collapsable"} className="collapse show"
                                   aria-labelledby="headingOne"
                                   data-parent={"#" + this.state.id + "_accordion"}>
                            <Card.Text>
                                <b>Doctor:</b> {this.state.doctorFirstName + " " + this.state.doctorLastName}
                            </Card.Text>
                            <Card.Text>
                                <b>Status:</b> {this.state.status}
                            </Card.Text>
                            <Card.Text>
                                <b>Medical service:</b> {this.state.medicalService}
                            </Card.Text>
                            <Form onSubmit={this.onScheduleAppointment}>
                                <Notification show={this.state.notification.show}
                                              message={this.state.notification.message}
                                              type={this.state.notification.type}/>
                                <InputGroup className="mb-3">
                                    <InputGroup.Text>Appointment date</InputGroup.Text>
                                    <FormControl type="text" required placeholder={new Date().toLocaleDateString('il-IT')}
                                                 name="appointmentDate"
                                                 onChange={this.handleInputChange}/>
                                </InputGroup>
                                <Container className="fluid">
                                    <ListGroup variant="flush">
                                        {this.state.availableDates.map(item =>
                                            <ListGroup.Item key={item.id}>
                                                {(this.parseDateWithFormat(item)).toLocaleDateString()}
                                            </ListGroup.Item>
                                        )}
                                    </ListGroup>
                                </Container>
                                <Button variant="success">Schedule</Button>
                            </Form>
                        </Card.Body>
                    </Accordion.Body>
                </Accordion.Item>
            </Accordion>
        )
    }
}

export default EditableAppointmentItem;