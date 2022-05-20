import React from 'react'
import {GetCurrentUser} from "../actions/UserActions";
import Notification from "../components/Notification";

class Home extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            authority: -1,
            authenticated: false,
            username: ""
        }
    }

    componentDidMount() {
        GetCurrentUser()
            .then(currentUserData => {
                this.setState({
                    username: currentUserData.username,
                    authority: currentUserData.authority,
                    authenticated: true,
                })
            })
            .catch(e => {
                this.state = {
                    authority: -1,
                    authenticated: false,
                    username: ""
                }
            });
    }

    render() {
        return (
            <div
                className="background-container-home bg-image d-flex justify-content-center align-items-center">
                <div className="transparent-background">
                    {(this.state.username !== "") ?
                        <h1 className="text-white">Welcome {this.state.username}!</h1>
                        :
                        <h1 className="text-white">Welcome stranger!</h1>
                    }
                </div>
            </div>
        )
    }
}
export default Home;