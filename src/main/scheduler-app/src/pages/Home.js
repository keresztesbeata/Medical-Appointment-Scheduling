import React from 'react'
import {GetCurrentUser} from "../actions/UserActions";

class Home extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            authority: -1,
            authenticated: false,
            profile: {}
        }
    }

    componentDidMount() {
        GetCurrentUser()
            .then(currentUserData => {
                this.setState({
                    profile: currentUserData.profile,
                    authority: currentUserData.authority,
                    authenticated: true,
                })
            })
            .catch(e => {
                this.state = {
                    authority: -1,
                    authenticated: false,
                    profile: {}
                }
            });
    }

    render() {
        return (
            <div
                className="background-container-home bg-image d-flex justify-content-center align-items-center">
                <div className="transparent-background">
                    {(this.state.currentUser !== null) ?
                        <h1 className="text-white">Welcome {this.state.currentUser.username}!</h1>
                        :
                        <h1 className="text-white">Welcome stranger!</h1>
                    }
                </div>
            </div>
        )
    }
}
export default Home;