import React from 'react'
import {Alert} from 'react-bootstrap'

class Notification extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            showNotification: true,
        }
        this.hideNotification = this.hideNotification.bind(this);
    }

    hideNotification() {
        this.setState({
            showNotification: false
        });
    }

    render() {
        return (
            <Alert dismissible={true} onClose={this.hideNotification}
                   className={this.props.type} show={this.props.show && this.state.showNotification}>
                {this.props.message}
            </Alert>
        )
    }
}

export default Notification;