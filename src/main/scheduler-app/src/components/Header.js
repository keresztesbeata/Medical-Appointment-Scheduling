import React from 'react';
import {Container, Nav, Navbar, NavDropdown} from 'react-bootstrap'
import {GetCurrentUser} from "../actions/UserActions";
import {AUTH_DOCTOR, AUTH_PATIENT, AUTH_RECEPTIONIST} from "../actions/Utils";

class Header extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            authenticated: false,
            authority: -1
        }
        this.getLinksBasedOnAuthority = this.getLinksBasedOnAuthority.bind(this);
    }

    componentDidMount() {
        GetCurrentUser()
            .then(currentUserData => {
                this.setState({
                    authenticated: true,
                    authority: currentUserData.authority
                })
            })
            .catch(e => {
                this.state = {
                    authenticated: false,
                    authority: null
                }
            });
    }

    getLinksBasedOnAuthority(authorityLevel) {
        switch (authorityLevel) {
            case AUTH_PATIENT:
                return [
                    <NavDropdown title="Appointments" key={1}>
                        <NavDropdown.Item href="/patientProfile/view_appointments" key={2}>My appointments</NavDropdown.Item>
                        <NavDropdown.Item href="/patientProfile/new_appointment" key={3}>New appointment</NavDropdown.Item>
                    </NavDropdown>,
                    <Nav.Link href="/patientProfile/prescriptions" key={4}>My prescriptions</Nav.Link>,
                ];
            case AUTH_RECEPTIONIST:
                return [
                    <NavDropdown title="Appointments" key={1}>
                        <NavDropdown.Item href="/receptionist/view_appointments" key={2}>View
                            appointments</NavDropdown.Item>
                        <NavDropdown.Item href="/receptionist/manage_appointments" key={3}>Manage
                            appointments</NavDropdown.Item>
                    </NavDropdown>,
                    <Nav.Link href="/receptionist/view_doctors" key={4}>View doctors</Nav.Link>,
                ];
            case AUTH_DOCTOR:
                return [
                    <Nav.Link href="/doctor/view_appointments" key={1}>View appointments</Nav.Link>,
                    <Nav.Link href="/doctor/view_patients" key={2}>View patients</Nav.Link>,
                    <Nav.Link href="/doctor/new_prescription" key={3}>New prescription</Nav.Link>,
                ];
            default:
                return [];
        }
    }

    render() {
        return (<div>
            <Navbar bg="dark" variant="dark">
                <Container>
                    {/*<img src={require("../images/logo.jpg")} alt="Medical Appointment Scheduling icon" width="5%"/>*/}
                    <Navbar.Brand href="/">MedPlan</Navbar.Brand>
                    {
                        (this.state.authenticated) ?
                            <Nav className="me-auto">
                                {this.getLinksBasedOnAuthority(this.state.authority)}
                                <Nav.Link href="/profile">My profile</Nav.Link>
                                <Nav.Link href="/logout">Logout</Nav.Link>
                            </Nav>
                            :
                            <Nav className="me-auto">
                                <Nav.Link href="/login">Log in</Nav.Link>
                                <Nav.Link href="/register">Register</Nav.Link>
                            </Nav>
                    }
                </Container>
            </Navbar>
        </div>);
    }
}

export default Header;
