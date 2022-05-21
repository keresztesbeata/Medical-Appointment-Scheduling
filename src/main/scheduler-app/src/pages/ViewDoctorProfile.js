import React from 'react'
import {Button, Card, FormControl, InputGroup} from 'react-bootstrap'
import "react-datepicker/dist/react-datepicker.css";
import {GetCurrentUserProfile} from "../actions/UserActions";
import {ERROR, WARNING} from "../actions/Utils";
import Notification from "../components/Notification";
import {Link} from "react-router-dom";

class ViewDoctorProfile extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            profile: {
                firstName: "",
                lastName: "",
                specialty: "",
                startTime: "",
                finishTime: ""
            },
            notification: {
                show: false,
                message: "",
                type: ERROR
            }
        };
    }

    componentDidMount() {
        GetCurrentUserProfile()
            .then(profileData => {
                this.setState({
                    ...this.state,
                    profile: profileData,
                    notification: {
                        show: false
                    }
                })
            })
            .catch(error => {
                console.log(error)
                this.setState({
                    ...this.state,
                    notification: {
                        show: true,
                        message: error.message,
                        type: WARNING
                    }
                });
            });
    }

    render() {
        return (
            <div className="background-container-register bg-image d-flex justify-content-center align-items-center">
                <div className="card col-sm-6 border-dark text-left">
                    <Card className="card-body">
                        <h3 className="card-title text-center">My profile</h3>
                        {
                            (this.state.notification.show) ?
                                    <Card.Body>
                                        <Notification show={this.state.notification.show} message={this.state.notification.message}
                                                      type={this.state.notification.type}/>
                                        <Button variant="outline-info" id="doctor_setup_profile_btn">
                                            <Link to="/doctor/setup_profile" className="text-decoration-none">
                                                Setup profile
                                            </Link>
                                        </Button>
                                    </Card.Body>
                                :
                                <Card.Body>
                                    <InputGroup className="mb-3">
                                        <InputGroup.Text>First Name</InputGroup.Text>
                                        <FormControl type="text" placeholder="Name" disabled
                                                     value={this.state.profile.firstName}/>
                                    </InputGroup>
                                    <InputGroup className="mb-3">
                                        <InputGroup.Text>Last Name</InputGroup.Text>
                                        <FormControl type="text"
                                                     value={this.state.profile.lastName}
                                                     disabled/>
                                    </InputGroup>
                                    <InputGroup className="mb-3">
                                        <InputGroup.Text>Specialty</InputGroup.Text>
                                        <FormControl type="text"
                                                     value={this.state.profile.specialty}
                                                     disabled/>
                                    </InputGroup>
                                    <InputGroup className="mb-3">
                                        <InputGroup.Text>Start time</InputGroup.Text>
                                        <FormControl type="text"
                                                     value={this.state.profile.startTime.toString().replaceAll(',',':')}
                                                     disabled/>
                                    </InputGroup>
                                    <InputGroup className="mb-3">
                                        <InputGroup.Text>Finish time</InputGroup.Text>
                                        <FormControl type="text"
                                                     value={this.state.profile.finishTime.toString().replaceAll(',',':')}
                                                     disabled/>
                                    </InputGroup>
                                </Card.Body>
                        }
                    </Card>
                </div>
            </div>
        )
    }
}

export default ViewDoctorProfile;