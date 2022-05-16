import React from 'react'
import {Card} from "react-bootstrap";

class Error extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        const errorMessage = new URLSearchParams(window.location.search).get('message');
        return (
            <div
                className="background-container-error d-flex justify-content-center align-items-center">
                <Card className="error-background">
                    <Card.Title>
                        Error!
                    </Card.Title>
                    <Card.Body>
                        {errorMessage}
                    </Card.Body>
                </Card>
            </div>
        )
    }
}
export default Error;