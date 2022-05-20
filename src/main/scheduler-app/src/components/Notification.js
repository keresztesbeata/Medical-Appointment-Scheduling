import React from 'react'
import {Alert} from 'react-bootstrap'

class Notification extends React.Component {
    constructor(props) {
        super(props);
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
                   className={this.props.type} show={this.props.show}>
                {this.props.message}
            </Alert>
        )
    }
}

export default Notification;