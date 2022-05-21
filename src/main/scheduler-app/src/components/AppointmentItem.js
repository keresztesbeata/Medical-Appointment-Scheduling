import React from 'react'
import {Accordion, Card} from 'react-bootstrap'

class AppointmentItem extends React.Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            id: props.data.id,
            patientFirstName: props.data.patientFirstName,
            patientLastName: props.data.patientLastName,
            doctorFirstName: props.data.doctorFirstName,
            doctorLastName: props.data.doctorLastName,
            status: props.data.status,
            appointmentDate: props.data.appointmentDate,
            medicalService: props.data.medicalService,
        }
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
                                <b>Date:</b> { (this.state.appointmentDate === null)? "-" : (new Date(this.state.appointmentDate)).toLocaleDateString()}
                            </Card.Text>
                            <Card.Text>
                                <b>Status:</b> {this.state.status}
                            </Card.Text>
                            <Card.Text>
                                <b>Medical service:</b> {this.state.medicalService}
                            </Card.Text>
                        </Card.Body>
                    </Accordion.Body>
                </Accordion.Item>
            </Accordion>
        )
    }
}

export default AppointmentItem;