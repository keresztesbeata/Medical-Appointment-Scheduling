import React from 'react'
import {Container, ListGroup, ListGroupItem} from "react-bootstrap";
import {ERROR, INFO} from "../actions/Utils";
import {LoadAllPrescriptionsOfPatient} from "../actions/PrescriptionActions";
import PrescriptionItem from "../components/PrescriptionItem";
import Notification from "../components/Notification";

class PatientViewPrescriptions extends React.Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            prescriptions: [],
            notification: {
                show: false,
                message: "",
                type: ERROR,
            }
        };
    }

    componentDidMount() {
        LoadAllPrescriptionsOfPatient()
            .then(data => {
                console.log(data)
                this.setState({
                    ...this.state,
                    prescriptions: data,
                });
                if (data.length === 0) {
                    this.setState({
                        notification: {
                            show: true,
                            message: "You have no previous prescriptions!",
                            type: INFO
                        }
                    })
                }
            })
            .catch(error => {
                console.log(error)
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
            <div className="background-container-order bg-image justify-content-center ">
                <Container>
                    <div className="text-center transparent-background">
                        <h1 className="text-white">
                            My prescriptions
                        </h1>
                    </div>
                    <Notification show={this.state.notification.show} message={this.state.notification.message}
                                  type={this.state.notification.type}/>
                    <div className="flex justify-content-center">
                        <Container className="fluid">
                            <ListGroup variant="flush">
                                {this.state.prescriptions.map(item =>
                                    <ListGroupItem key={item.appointmentId}>
                                        <PrescriptionItem data={item}/>
                                    </ListGroupItem>
                                )}
                            </ListGroup>
                        </Container>
                    </div>
                </Container>
            </div>
        );
    }
}

export default PatientViewPrescriptions;