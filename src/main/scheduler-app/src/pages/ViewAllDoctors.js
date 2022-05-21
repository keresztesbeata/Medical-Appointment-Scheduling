import React from 'react'
import {Button, Card, Container, Form, FormControl, ListGroup, Navbar} from "react-bootstrap";
import {ERROR} from "../actions/Utils";
import {FilterDoctorsByMedicalService, FindDoctorByName, LoadMedicalServices} from "../actions/AppointmentActions";
import Notification from "../components/Notification";

class ViewAllDoctors extends React.Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            doctors: [],
            doctorFirstName: "",
            doctorLastName: "",
            medicalServices: [],
            selectedMedicalService: "",
            notification: {
                show: false,
                message: "",
                type: ERROR,
            }
        };

        this.handleInputChange = this.handleInputChange.bind(this);
        this.filterDoctors = this.filterDoctors.bind(this);
        this.searchDoctorByName = this.searchDoctorByName.bind(this);
    }

    componentDidMount() {
        LoadMedicalServices()
            .then(data => {
                this.setState({
                    ...this.state,
                    medicalServices: data,
                });
            })
            .catch(error => {
                this.setState({
                    notification: {
                        show: true,
                        message: error.message,
                        type: ERROR,
                    }
                });
            });
    }

    searchDoctorByName() {
        FindDoctorByName(this.state.doctorFirstName, this.state.doctorLastName)
            .then(data => {
                this.setState({
                    ...this.state,
                    doctors: data,
                    notification: {
                        show: false,
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
                })
            });
    }

    filterDoctors() {
        FilterDoctorsByMedicalService(this.state.selectedMedicalService)
            .then(data => {
                this.setState({
                    ...this.state,
                    doctors: data,
                    notification: {
                        show: false,
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
                })
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

    render() {
        return (
            <div className="background-container-menu justify-content-center ">
                <Container>
                    <Notification show={this.state.notification.show} message={this.state.notification.message}
                                  type={this.state.notification.type}/>
                    <Navbar className="justify-content-center">
                        <Form className="d-flex">
                            <FormControl
                                type="search"
                                placeholder="Doctor first name..."
                                className="me-2"
                                aria-label="Search"
                                name="doctorFirstName"
                                onChange={this.handleInputChange}
                            />
                            <FormControl
                                type="search"
                                placeholder="Doctor last name..."
                                className="me-2"
                                aria-label="Search"
                                name="doctorLastName"
                                onChange={this.handleInputChange}
                            />
                            <Button variant="success" onClick={this.searchDoctorByName}>Search</Button>
                        </Form>
                    </Navbar>
                    <div className="flex justify-content-center">
                        <Navbar className="justify-content-center">
                            <Form className="d-flex">
                                <Form.Label>Medical service</Form.Label>
                                <Form.Select aria-label="Medical services" className="me-2"
                                             name="selectedMedicalService"
                                             onChange={this.handleInputChange}>
                                    <option value="" key="All">All</option>
                                    {
                                        this.state.medicalServices.map(service =>
                                            <option value={service} key={service}>{service}</option>
                                        )
                                    }
                                </Form.Select>
                                <Button variant="success" onClick={this.filterDoctors}>Filter</Button>
                            </Form>
                        </Navbar>
                        <Container className="fluid">
                            <ListGroup variant="flush" >
                                {this.state.doctors.map(item =>
                                    <ListGroup.Item key={item.firstName+"_"+item.lastName} >
                                        <Card key={"card_" + item.firstName+"_"+item.lastName} className="align-items-center justify-content-center" className="info-box">
                                            <Card.Title className="card-title">{item.firstName + " " + item.lastName}</Card.Title>
                                            <Card.Subtitle className="mb-2 text-muted">{item.specialty}</Card.Subtitle>
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

export default ViewAllDoctors;