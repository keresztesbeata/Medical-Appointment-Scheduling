import React from 'react'
import {Alert} from 'react-bootstrap'

class Notification extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            show: props.show,
            message: props.message,
            type: props.type,
        };
        this.hideNotification = this.hideNotification.bind(this);
    }

    hideNotification() {
        this.setState({
            show: false
        });
    }

    render() {
        return (
            <Alert dismissible={true} onClose={this.hideNotification}
                   className={this.state.type} show={this.state.show}>
                {this.state.message}
            </Alert>
        )
    }
}

export default Notification;