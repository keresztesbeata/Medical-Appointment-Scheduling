import React from 'react'
import {Button, FormControl, FormGroup, FormLabel, FormSelect} from 'react-bootstrap'
import {RegisterUser} from "../actions/UserActions";
import {ERROR, SUCCESS} from "../actions/Utils";
import Notification from "../components/Notification";

class Register extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: "",
            password: "",
            accountType: "",
            notification: {
                show: false,
                message: "",
                type: ERROR,
            }
        };

        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleInputChange(event) {
        // prevent page from reloading
        event.preventDefault();
        const target = event.target;
        this.setState({
            ...this.state,
            [target.name]: target.value,
            notification: {
                show: false
            }
        });
    }

    handleSubmit(event) {
        // prevent page from reloading
        event.preventDefault();
        RegisterUser(this.state.username, this.state.password, this.state.accountType)
            .then(() => {
                this.setState({
                    notification: {
                        show: true,
                        message: "You have successfully registered! Next, log in to access the application and complete the setup process!",
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

        document.getElementById("registration-form").reset();
    }

    render() {
        return (
            <div className="background-container-register bg-image d-flex justify-content-center align-items-center">
                <div className="card col-sm-3 border-dark text-left">
                    <form onSubmit={this.handleSubmit} className="card-body" id="registration-form">
                        <h3 className="card-title">Register</h3>
                        <Notification show={this.state.notification.show} message={this.state.notification.message}
                                      type={this.state.notification.type}/>
                        <FormGroup className="mb-3" controlId="formBasicText">
                            <FormLabel>Username</FormLabel>
                            <FormControl type="text" required placeholder="Enter username" name="username"
                                         onChange={this.handleInputChange}/>
                        </FormGroup>
                        <FormGroup className="mb-3" controlId="formBasicPassword">
                            <FormLabel>Password</FormLabel>
                            <FormControl type="password" required placeholder="Enter Password" name="password"
                                         onChange={this.handleInputChange}/>
                        </FormGroup>
                        <FormGroup className="mb-3">
                            <FormLabel>Account type</FormLabel>
                            <FormSelect name="accountType" onChange={this.handleInputChange} required>
                                <option value="" key={-1}>Select the account type ...</option>
                                <option value="PATIENT" key={0}>Patient</option>
                                <option value="RECEPTIONIST" key={1}>Receptionist</option>
                                <option value="DOCTOR" key={2}>Doctor</option>
                            </FormSelect>
                        </FormGroup>
                        <div className="text-center">
                            <Button variant="secondary" type="submit">
                                Register
                            </Button>
                        </div>
                    </form>
                </div>
            </div>
        )
    }
}

export default Register;