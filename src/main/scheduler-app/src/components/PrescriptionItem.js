import React from 'react'
import {Accordion, Card, Table} from 'react-bootstrap'
import {LoadPrescriptionByAppointmentId} from "../actions/PrescriptionActions";

class PrescriptionItem extends React.Component {
    constructor(props, context) {
        super(props, context);
        this.state = props.data;
        this.loadPrescriptionData = this.loadPrescriptionData.bind(this);
        this.parseDateWithFormat = this.parseDateWithFormat.bind(this);
    }

    loadPrescriptionData() {
        LoadPrescriptionByAppointmentId(this.state.appointmentId)
            .then(data => {
                this.setState(data)
            })
            .catch(e => {
                console.log(e);
            });
        console.log(this.state.appointmentDate)
    }

    componentDidMount() {
        this.loadPrescriptionData();
    }

    parseDateWithFormat(date) {
        return new Date(date[0], date[1], date[2]);
    }

    render() {
        if (this.props.data.status !== this.state.status) {
            this.loadPrescriptionData();
        }
        return (
            <Accordion>
                <Accordion.Item eventKey="0">
                    <Accordion.Header>
                        <Card.Body>
                            <Card.Title className="card-title">
                                Prescription #{this.state.appointmentId}
                            </Card.Title>
                            <Card.Subtitle className="mb-2 text-muted">
                                Patient: {this.state.patientFirstName + " " + this.state.patientLastName}
                            </Card.Subtitle>
                        </Card.Body>
                    </Accordion.Header>
                    <Accordion.Body>
                        <Card.Body id={this.state.appointmentId + "_collapsable"} className="collapse show"
                                   aria-labelledby="headingOne"
                                   data-parent={"#" + this.state.appointmentId + "_accordion"}>
                            <Card.Text>
                                <b>Doctor:</b> {this.state.doctorFirstName + " " + this.state.doctorLastName}
                            </Card.Text>
                            <Card.Text>
                                <b>Appointment date:</b> { (this.state.appointmentDate === null)? "-" :
                                (this.parseDateWithFormat(this.state.appointmentDate)).toLocaleDateString()}
                            </Card.Text>
                            <Card.Text>
                                <b>Medication:</b> {this.state.medication}
                            </Card.Text>
                            <Card.Text>
                                <b>Indications:</b> {this.state.indications}
                            </Card.Text>
                        </Card.Body>
                    </Accordion.Body>
                </Accordion.Item>
            </Accordion>
        )
    }
}

export default PrescriptionItem;