import axios from "axios";
import React from "react";

import {translate} from "react-i18next";
import {toast} from 'react-toastify';

import {withCookies} from "react-cookie";
import User from "../util/User";
import GoogleLogin from "react-google-login";
import Popup from "reactjs-popup";
import A7Popup from "./a7Popup";

class tpaAuthentication extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            sending : '',
            tpaId : '',
            service : ''
        };

        this.authenticateWithGoogle = this.authenticateWithGoogle.bind(this);

        this.options = toast.POSITION.BOTTOM_CENTER;
        this.cookies = this.props.cookies;
    }

    authenticateWithGoogle() {
        this.state.sending = true;
        this.state.service = "GOOGLE";
        this.setState(this.state);

        axios.post('/auth/tpaLogin', this.state, {
            // We allow a status code of 401 (unauthorized). Otherwise it is interpreted as an error and we can't
            // check the HTTP status code.
            validateStatus: (status) => {
                return (status >= 200 && status < 300) || status == 401
            }
        })
            .then(({data, status}) => {

                this.state.sending = false;
                this.setState(this.state);

                const {t} = this.props;

                const options = {
                    position: toast.POSITION.BOTTOM_CENTER
                };

                switch (status) {
                    case 200:
                        User.setCookieCredentials(data);

                        this.setState({error: undefined});

                        // Store authentication values even after refresh.
                        this.cookies.set('auth', {
                            token: data.token,
                            user: User
                        }, {path: '/'});

                        // Send event of updated login state.
                        this.props.updateNavigation();

                        // Redirect to front page.
                        this.props.history.push("/");
                        break;
                    case 401:
                        this.setState({error: true});
                        toast.error(t('loginFailed'), options);
                        break;
                }
            });
    }


    render() {
        //TODO: name der buttons sollte sich abhängig von register oder login ändern
        //TODO: Button Größe vereinheitlichen, position anpassen
        const responseGoogle = (response) => {
            //TODO Check if something went wrong.
            this.state.tpaId = response.getAuthResponse().id_token;

            this.setState(this.state);
            this.authenticateWithGoogle();
        };

        let googleLoginButton =
            <div>
                <GoogleLogin
                clientId="583900150012-agjlvgr8gjsj8cv5f8fkiv3fjl9keu1j.apps.googleusercontent.com"
                buttonText="Login with Google"
                onSuccess={responseGoogle}
                onFailure={responseGoogle}
                cookiePolicy={'single_host_origin'}
                />
            </div>;

        let googleRegisterButton =
            <div>
                <GoogleLogin
                clientId="583900150012-agjlvgr8gjsj8cv5f8fkiv3fjl9keu1j.apps.googleusercontent.com"
                buttonText="Register with Google"
                onSuccess={responseGoogle}
                onFailure={responseGoogle}
                cookiePolicy={'single_host_origin'}
                />
            </div>;

        let a7LoginPopup =
            <div>
                <Popup
                    trigger={<button> Login with Andaman7</button>}
                    position="right center"
                    modal>
                    <A7Popup {...this.props} updateNavigation={this.props.updateNavigation}  />
                </Popup>
            </div>;

        let a7RegisterPopup =
            <div>
                <Popup
                    trigger={<button> Register with Andaman7</button>}
                    position="right center"
                    modal>
                    <A7Popup {...this.props} updateNavigation={this.props.updateNavigation}  />
                </Popup>
            </div>;

        return (
            <div>
                {this.props.location.pathname === "/user/login" ? googleLoginButton : googleRegisterButton}
                {this.props.location.pathname === "/user/login" ? a7LoginPopup : a7RegisterPopup}
            </div>);

    }
}

export default withCookies(translate()(tpaAuthentication))