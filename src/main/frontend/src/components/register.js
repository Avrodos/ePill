import axios from "axios";
import React from "react";

import {Link} from "react-router-dom";
import {translate} from "react-i18next";
import { toast } from 'react-toastify';

import GoogleLogin from 'react-google-login';
import User from "../util/User";
import {withCookies} from "react-cookie";

// See https://facebook.github.io/react/docs/forms.html for documentation about forms.
class Register extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            firstname	: '',
            lastname		: '',
            username		: '',
            gender		: {id : 0},
            redGreenColorblind    : false,
            password		: '',
            passwordRepeat: '',
            sending		: false,
            tpaId : '',
            service : ''
        };

        this.handleFirstnameChange	= this.handleFirstnameChange.bind(this);
        this.handleLastnameChange	= this.handleLastnameChange.bind(this);
        this.handleGenderChange		= this.handleGenderChange.bind(this);
        this.handleRedGreenColorblind = this.handleRedGreenColorblind.bind(this);

        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handlePasswordRepeatChange = this.handlePasswordRepeatChange.bind(this);

        this.handleSubmit = this.handleSubmit.bind(this);

        this.registeredByGoogle = this.registeredByGoogle.bind(this);

        this.options = toast.POSITION.BOTTOM_CENTER;
        this.cookies = this.props.cookies; //TODO: unsure
    }


    handleFirstnameChange(event) {
        this.state.firstname = event.target.value;
        this.setState(this.state);
    }

    handleLastnameChange(event) {
        this.state.lastname = event.target.value;
        this.setState(this.state);
    }

    handleGenderChange(event) {
        this.state.gender.id = event.target.value;
        this.setState(this.state);
    }

    handleRedGreenColorblind(event) {
        this.state.redGreenColorblind = (event.target.value == 1) ? true : false;
        this.setState(this.state);
    }

    handleUsernameChange(event) {
        this.setState({username: event.target.value});
        this.state.username = event.target.value;
        this.setState(this.state);
    }

    handlePasswordChange(event) {
        this.state.password = event.target.value;
        this.setState(this.state);
    }

    handlePasswordRepeatChange(event) {
        this.state.passwordRepeat = event.target.value;
        this.setState(this.state);
    }

    handleSubmit(event) {
        event.preventDefault();

        const {t} = this.props;
        const options = {
            position: toast.POSITION.BOTTOM_CENTER
        };

        if(this.state.firstname.length == 0 || this.state.lastname.length == 0 || this.state.username.length == 0 || this.state.password == 0) {
            return;
        }

        if(this.state.password != this.state.passwordRepeat) {
            toast.error(t('passwordsDifferent'), this.options);
            return;
        }
        this.state.sending = true;
        this.setState(this.state);

        axios.post('/user/save',
            {
                firstname       : this.state.firstname,
                lastname        : this.state.lastname,
                gender          : this.state.gender,
                redGreenColorblind    : this.state.redGreenColorblind,
                username        : this.state.username,
                password        : this.state.password
            })
            .then(({data, status}) => {

                this.state.sending = false;
                this.setState(this.state);

                switch (status) {
                    case 200:
                        toast.success(t('registrationSuccess'), this.options);
                        this.props.history.push("/user/login");
                        break;
                    case 409:
                        toast.error(t('usernameUsed'), this.options);
                        break;
                    default:
                        toast.error(t('errorOccurred'), this.options);
                        break;
                }
            });
    }

    /*
    To prevent overfilling the render method, I extracted the pushing and sending process for Google sign up to a separate method
     */
    registeredByGoogle() {

        this.state.sending = true;
        this.state.service = "GOOGLE";
        this.setState(this.state);
        console.log("sending");

        axios.post('/auth/googleLogin', this.state, {
            // We allow a status code of 401 (unauthorized). Otherwise it is interpreted as an error and we can't
            // check the HTTP status code.
            validateStatus: (status) => {
                console.log(status);
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
        const {t} = this.props;

        const responseGoogle = (response) => {
            console.log(response);
            //TODO Check if something went wrong.
            const profile = response.getBasicProfile();
            const id_token = response.getAuthResponse().id_token;

            this.state.firstname = profile.getGivenName();
            this.state.lastname = profile.getFamilyName();
            //this.state.username = profile.getEmail(); //for testing
            //this.state.password = "apfel"; //for testing
            this.state.tpaId = id_token;
            console.log("Now registerProcess should be running.");
            this.registeredByGoogle();
        }

        return (
            <div className="container no-banner">
                <div className="page-header">
                    <h2>{t('register')}</h2>
                </div>
                <div className="row">
                    <form onSubmit={this.handleSubmit} className="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-0 col-lg-4 col-lg-offset-0 column">
                        <div className="form-group">
                            <label htmlFor="firstname">{t('firstname')}</label>
                            <input type="text" name="firstname" id="firstname" className="form-control" value={this.state.firstname} onChange={this.handleFirstnameChange} />
                        </div>

                        <div className="form-group">
                            <label htmlFor="lastname">{t('lastname')}</label>
                            <input type="text" name="lastname" id="lastname" className="form-control" value={this.state.lastname} onChange={this.handleLastnameChange} />
                        </div>

                        <div className="form-group">
                            <label htmlFor="gender">{t('gender')}</label>
                            <select id="gender" value="0" name="gender" className="form-control" title={t('gender')} value={this.state.gender.id} onChange={this.handleGenderChange}>
                                <option value="0" disabled>{t('noInfo')}</option>
                                <option value="2">{t('female')}</option>
                                <option value="1">{t('male')}</option>
                            </select>
                        </div>
                        <div className="form-group">
                            <label htmlFor="red-green-colorblind">{t('redGreenColorblind')}</label>
                            <ul id="red-green-colorblind" className="inline">
                                <li className="col-lg-6 col-md-6 col-xs-6">
                                    <label htmlFor="red-green-colorblind-yes" className="radio-inline">
                                        <input type="radio" value="1" id="red-green-colorblind-yes" name="redGreenColorblind" checked={this.state.redGreenColorblind == true} onChange={this.handleRedGreenColorblind} />
                                        {t('yes')}
                                    </label>
                                </li>
                                <li className="col-lg-6 col-md-6 col-xs-6">
                                    <label htmlFor="red-green-colorblind-no" className="radio-inline">
                                        <input type="radio" value="0" id="red-green-colorblind-no" name="redGreenColorblind" checked={this.state.redGreenColorblind == false} onChange={this.handleRedGreenColorblind} />
                                        {t('no')}
                                    </label>
                                </li>
                            </ul>
                        </div>
                        <div className="form-group">
                            <label htmlFor="username">{t('username')}</label>
                            <input type="text" name="username" id="username" className="form-control" value={this.state.username} onChange={this.handleUsernameChange} />
                        </div>

                        <div className="form-group">
                            <label htmlFor="password">{t('password')}</label>
                            <input type="password" name="password" id="password" className="form-control" value={this.state.password} onChange={this.handlePasswordChange} />
                        </div>

                        <div className="form-group">
                            <label htmlFor="password_rep">{t('passwordRepeat')}</label>
                            <input type="password" name="password_rep" id="password_rep" className="form-control" value={this.state.password_repeat} onChange={this.handlePasswordRepeatChange} />
                        </div>

                        <div className="form-actions">
                            {!this.state.sending ?
                                <button type="submit" className="btn btn-primary">{t('register')}</button>
                                :
                                <button className="btn btn-default">
                                    <img src="data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA=="></img>
                                </button>
                            }
                            <Link to="/user/login"><button type="button" className="btn btn-default">{t('login')}</button></Link>
                        </div>
                    </form>
                    <div className="hidden-xs hidden-sm col-md-6 col-lg-4 container">
                        <h4>Warum du dich registrieren kannst?</h4>
                        <p>Es gibt viele tolle Features, die dir dabei helfen können,<br />
                            dein Verständnis für Medikamente zu verbessern.</p>
                        <p>Beispielsweise können wir dir genau die Informationen...</p>
                    </div>

                    <GoogleLogin
                        clientId="583900150012-agjlvgr8gjsj8cv5f8fkiv3fjl9keu1j.apps.googleusercontent.com"
                        buttonText="Sign Up with Google"
                        onSuccess={responseGoogle}
                        onFailure={responseGoogle}
                        cookiePolicy={'single_host_origin'}
                    />

                </div>
            </div>

        );
    }
}

export default withCookies(translate()(Register));