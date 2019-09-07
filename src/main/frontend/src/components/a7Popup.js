import axios from "axios";
import React from "react";

import {translate} from "react-i18next";
import {toast} from 'react-toastify';

import {withCookies} from "react-cookie";
import {sha256} from "js-sha256";
import User from "../util/User";
import Cookies from "universal-cookie";

class a7Popup extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            firstname: '',
            lastname: '',
            sending : '',
            tpaId : '',
            service: 'A7',
            password : '',
            email: '',
            error: ''
        };

        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.updateA7UserData = this.updateA7UserData.bind(this);

        this.options = toast.POSITION.BOTTOM_CENTER;
        this.cookies = this.props.cookies;
    }
    componentDidMount() {
        //TODO:
        this._isMounted = true;
    }

    handleUsernameChange(event) {
        this.setState({tpaId: event.target.value});
    }

    handlePasswordChange(event) {
        this.setState({password: event.target.value});
    }

    handleSubmit(event) {
        event.preventDefault();
        this.state.sending = true;
        this.setState(this.state);

        const url = 'https://sandbox.andaman7.com/public/v1/users/me';
        const apiKey = '0a24fa4b-9f38-47a4-ad99-5ba3a2c211c1';
        const hashedPw = sha256(this.state.password);
        let credentials = this.state.tpaId + ":" + hashedPw;
        let credString = 'Basic ' + window.btoa(credentials);

        axios({
            url: url,
            method: 'get',
            headers: {
                'api-key': apiKey,
                'Authorization': credString
            }
        })
            .then(({data}) => {
                //TODO: Fehlerbehandlung. get gender, rot-grün-schwäche...
                //TODO: Account creation itself should be on the backend, right?
                    this.state.sending = false;
                    this.state.tpaId = data.id;
                    const {lastName: lastName, firstName: firstName} = data.administrative;
                    this.state.firstname = firstName;
                    this.state.lastname = lastName;
                    this.state.email = data.mail;
                    this.setState(this.state);

                    //sending request to backend
                    this.state.sending = true;
                    this.setState(this.state);

                    axios.post('/auth/tpaLogin', this.state, {
                        // We allow a status code of 401 (unauthorized). Otherwise it is interpreted as an error and we can't
                        // check the HTTP status code.
                        validateStatus: (status) => {
                            return (status >= 200 && status < 300) || status == 401
                        }
                    }).then(({data, status}) => {

                        this.state.sending = false;
                        this.setState(this.state);

                        const {t} = this.props;

                        const options = {
                            position: toast.POSITION.BOTTOM_CENTER
                        };

                        switch (status) {
                            case 200:
                                //TODO: now we load all available data
                                User.setCookieCredentials(data);
                                let tempUser = User.get();
                                tempUser.firstname = this.state.firstname;
                                tempUser.lastname = this.state.lastname;
                                console.log(tempUser);
                                User.set(tempUser);

                                this.setState({error: undefined});
                                console.log("State after .setState {error: undefined}");
                                console.log(this.state);
                                this.cookies.set('auth', {
                                    token: data.token,
                                    user: User
                                }, {path: '/'});
                                this.updateA7UserData(User);

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

                },
                (error) => {
                    console.log(error);
                    const {t} = this.props;
                    this.setState({error: true});
                    toast.error(t('loginFailed'), this.options);
                }
            );
    }

    //TODO: Colorblind, adjust method for proper use in profile
    updateA7UserData(givenUser) {
        console.log("Entered update Method! Current state:");
        console.log(this.state);
        if(this.state.sending)
            return;
        this.state.sending = true;
        const email = this.state.email;
        //const username = this.state.tpaId;
        this.setState(this.state);
        axios.post('/user/updateA7',
            {
                firstname: givenUser.firstname,
                lastname: givenUser.lastname,
                //dateOfBirth			: date,
                //gender				: this.state.gender,
                email: email
                //redGreenColorblind   : this.state.redGrenColorblind,
            }).then(({dat, status}) => {
            this.state.sending = false;
            this.setState(this.state);

            const {t} = this.props;
            const options = {
                position: toast.POSITION.BOTTOM_CENTER
            };
            switch (status) {
                case 200:
                    //var data2 = this.state;
                    //data2.id = User.id;
                    //User.set(data2);
                    User.set(givenUser);
                    console.log("Success!");
                    console.log(User.get());
                    break;
                case 400:
                    console.log("case 400");
                    toast.error(t('loading failed'), options);
                    break;
                case 401:
                    console.log(data, "not permitted");
                    break;
            }
        });

    }

    render() {
        const {t} = this.props;

        return (
            <div>
                <div>
                    {/* Popup content */}
                    <h2>Login für Andaman7</h2>

                    <form onSubmit={this.handleSubmit} className="col-xs-12 col-sm-6 col-sm-offset-3 col-md-4 col-md-offset-0 col-lg-4 col-lg-offset-0 column">
                        <div className="form-group">
                            <label htmlFor="username">{t('username')}</label>
                            <input type="text" name="username" id="username" className="form-control" value={this.state.username} onChange={this.handleUsernameChange} />
                        </div>

                        <div className="form-group">
                            <label htmlFor="password">{t('password')}</label>
                            <input type="password" name="password" id="password" className="form-control" value={this.state.password} onChange={this.handlePasswordChange} />
                        </div>
                        {!this.state.sending ?
                            <button type="submit" className="btn btn-primary">Login</button>
                            : <button className="btn btn-default"><img src="data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA=="></img></button> }

                    </form>
                </div>
            </div>)
    }
}

export default withCookies(translate()(a7Popup))