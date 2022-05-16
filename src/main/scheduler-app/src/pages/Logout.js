import React from 'react'
import {LogoutUser} from "../actions/UserActions";
import {ERROR} from "../actions/Utils";
import {Alert} from "react-bootstrap";

class Logout extends React.Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            notification: {
                show: false,
                message: "",
                type: ERROR
            }}
    }

    componentDidMount() {
        LogoutUser()
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
            })
    }

    render() {
        return (
            <div  className="background-container-home bg-image justify-content-center ">
                {
                    (this.state.notification.show) ?
                        <Alert dismissible={true} onClose={this.hideNotification}
                               className={this.state.notification.type}>
                            {this.state.notification.message}
                        </Alert>
                        :
                        <p>Logging out...</p>
                }
            </div>
        )
    }
}

export default Logout;