import React from 'react'
import {Alert, Button, FormControl, FormGroup, FormLabel} from 'react-bootstrap'
import {LoginUser} from "../actions/UserActions";
import {ERROR} from "../actions/Utils";

class Login extends React.Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            username: "",
            password: "",
            notification: {
                show: false,
                message: "",
                type: ERROR,
            }
        };

        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.hideNotification = this.hideNotification.bind(this);
    }

    handleInputChange(event) {
        // prevent page from reloading
        event.preventDefault();
        const target = event.target
        this.setState({
            [target.name]: target.value,
            notification: {
                show: false,
            }
        });
    }

    handleSubmit(event) {
        // prevent page from reloading
        event.preventDefault();
        LoginUser(this.state.username, this.state.password)
            .then(() => {
                window.location.href = "/"
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

    hideNotification() {
        this.setState({
            notification: {
                show: false
            }
        });
    }

    render() {
        return (
            <div className="background-container-login bg-image d-flex justify-content-center align-items-center">
                <div className="card col-sm-3 border-dark text-left">
                    <form onSubmit={this.handleSubmit} className="card-body">
                        <h3 className="card-title">Log in</h3>
                        {
                            (this.state.notification.show) ?
                                <Alert dismissible={true} onClose={this.hideNotification}
                                       className={this.state.notification.type}>
                                    {this.state.notification.message}
                                </Alert>
                                :
                                <div/>
                        }
                        <FormGroup className="mb-3" controlId="formBasicText">
                            <FormLabel>Username</FormLabel>
                            <FormControl type="text" placeholder="Enter username" name="username"
                                         onChange={this.handleInputChange}/>
                        </FormGroup>
                        <FormGroup className="mb-3" controlId="formBasicPassword">
                            <FormLabel>Password</FormLabel>
                            <FormControl type="password" placeholder="Enter Password" name="password"
                                         onChange={this.handleInputChange}/>
                        </FormGroup>
                        <div className="text-center">
                            <Button variant="secondary" type="submit">
                                Log in
                            </Button>
                        </div>
                    </form>
                </div>
            </div>
        )
    }
}

export default Login;