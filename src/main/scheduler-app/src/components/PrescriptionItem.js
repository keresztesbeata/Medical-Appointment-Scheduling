import React from 'react'
import {Accordion, Button, Card, Table} from 'react-bootstrap'
import {ExportPrescriptionAsPDF, LoadPrescriptionByAppointmentId} from "../actions/PrescriptionActions";
import Notification from "./Notification";
import {ERROR, SUCCESS} from "../actions/Utils";

class PrescriptionItem extends React.Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            prescription: props.data,
            notification: {
                show: false,
                message: "",
                type: ERROR
            }
        };
        this.loadPrescriptionData = this.loadPrescriptionData.bind(this);
        this.parseDateWithFormat = this.parseDateWithFormat.bind(this);
        this.onExportPrescription = this.onExportPrescription.bind(this);
    }

    loadPrescriptionData() {
        LoadPrescriptionByAppointmentId(this.state.prescription.appointmentId)
            .then(data => {
                this.setState(data)
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

    componentDidMount() {
        this.loadPrescriptionData();
    }

    onExportPrescription() {
        ExportPrescriptionAsPDF(this.state.prescription.appointmentId)
            .then(() => {
                this.setState({
                    ...this.state,
                    notification: {
                        show: true,
                        message: "Prescription successfully exported as PDF! You can find it at /prescriptions folder.",
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
    }

    parseDateWithFormat(date) {
        return new Date(date[0], date[1]-1, date[2], date[3], date[4]).toLocaleString();
    }

    render() {
        if (this.props.data.status !== this.state.prescription.status) {
            this.loadPrescriptionData();
        }
        return (
            <Accordion>
                <Accordion.Item eventKey="0">
                    <Accordion.Header>
                        <Card.Body>
                            <Card.Title className="card-title">
                                Prescription #{this.state.prescription.appointmentId}
                            </Card.Title>
                            <Card.Subtitle className="mb-2 text-muted">
                                Patient: {this.state.prescription.patientFirstName + " " + this.state.prescription.patientLastName}
                            </Card.Subtitle>
                        </Card.Body>
                    </Accordion.Header>
                    <Accordion.Body>
                        <Notification show={this.state.notification.show} message={this.state.notification.message}
                                      type={this.state.notification.type}/>
                        <Card.Body id={this.state.prescription.appointmentId + "_collapsable"} className="collapse show"
                                   aria-labelledby="headingOne"
                                   data-parent={"#" + this.state.prescription.appointmentId + "_accordion"}>
                            <Card.Text>
                                <b>Doctor:</b> {this.state.prescription.doctorFirstName + " " + this.state.prescription.doctorLastName}
                            </Card.Text>
                            <Card.Text>
                                <b>Appointment date:</b> { (this.state.prescription.appointmentDate === null)? "-" :
                                (this.parseDateWithFormat(this.state.prescription.appointmentDate))}
                            </Card.Text>
                            <Card.Text>
                                <b>Medication:</b> {this.state.prescription.medication}
                            </Card.Text>
                            <Card.Text>
                                <b>Indications:</b> {this.state.prescription.indications}
                            </Card.Text>
                            <Button variant="success"
                                    onClick={this.onExportPrescription}>
                                Export as PDF</Button>
                        </Card.Body>
                    </Accordion.Body>
                </Accordion.Item>
            </Accordion>
        )
    }
}

export default PrescriptionItem;