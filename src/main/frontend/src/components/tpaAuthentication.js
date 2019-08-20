import axios from "axios";
import React from "react";

import {translate} from "react-i18next";
import { toast } from 'react-toastify';

import {withCookies} from "react-cookie";
import User from "../util/User";
import GoogleLogin from "react-google-login";

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

        axios.post('/auth/googleLogin', this.state, {
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

        const responseGoogle = (response) => {
            //TODO Check if something went wrong.
            const id_token = response.getAuthResponse().id_token;
            this.state.tpaId = id_token;
            this.setState(this.state);
            this.authenticateWithGoogle();
        }

        return (
            <div>
            <GoogleLogin
                clientId="583900150012-agjlvgr8gjsj8cv5f8fkiv3fjl9keu1j.apps.googleusercontent.com"
                buttonText="Authenticate with Google"
                onSuccess={responseGoogle}
                onFailure={responseGoogle}
                cookiePolicy={'single_host_origin'}
            />
            </div>)
    }
}

export default withCookies(translate()(tpaAuthentication))