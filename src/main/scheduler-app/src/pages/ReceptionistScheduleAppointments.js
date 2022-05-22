import React from 'react'
import {Button, Container, Form, ListGroup, Navbar} from "react-bootstrap";
import {COMPACT_SCHEDULING_STRATEGY, ERROR, LOOSE_SCHEDULING_STRATEGY, SUCCESS} from "../actions/Utils";
import {
    ChangeSchedulingStrategy, LoadAvailableAppointmentDates, LoadNewAppointments,
} from "../actions/AppointmentActions";
import Notification from "../components/Notification";
import EditableAppointmentItem from "../components/EditableAppointmentItem";

class ReceptionistScheduleAppointments extends React.Component {
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
        this.onChangeStrategy = this.onChangeStrategy.bind(this);
    }

    componentDidMount() {
        LoadNewAppointments()
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

    onChangeStrategy(event) {
        ChangeSchedulingStrategy(this.state.selectedStrategy)
            .then(() => {
                this.setState({
                    ...this.state,
                    notification: {
                        show: true,
                        message: "Strategy successfully changed to "+this.state.selectedStrategy + "!",
                        type: SUCCESS
                    }
                })
            })
            .catch(error => {
                this.setState({
                    ...this.state,
                    notification: {
                        show: true,
                        message: error.message,
                        type: ERROR
                    }
                })
            })
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
                                <Form.Select aria-label="Scheduling strategy" name="selectedStrategy"
                                             onChange={this.handleInputChange}>
                                    <option value={COMPACT_SCHEDULING_STRATEGY} key={1}>{COMPACT_SCHEDULING_STRATEGY}</option>
                                    <option value={LOOSE_SCHEDULING_STRATEGY} key={2}>{LOOSE_SCHEDULING_STRATEGY}</option>
                                </Form.Select>
                                <Button variant="success" onClick={this.onChangeStrategy}>Change strategy</Button>
                            </Form>
                        </Navbar>
                        <Container className="fluid">
                            <ListGroup variant="flush">
                                {this.state.appointments.map(item =>
                                    <ListGroup.Item key={"key_"+item.id}>
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

export default ReceptionistScheduleAppointments;