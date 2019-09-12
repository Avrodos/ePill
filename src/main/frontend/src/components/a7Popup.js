import axios from "axios";
import React from "react";

import {translate} from "react-i18next";
import {toast} from 'react-toastify';

import {withCookies} from "react-cookie";
import {sha256} from "js-sha256";
import User from "../util/User";
import Popup from "reactjs-popup";
import MissingDataPopup from "./missingDataPopup";

//TODO: If I start the popup through "Login" the component unmounts before the update.
//TODO: If I create gacc first, I cant register with a7?
//TODO: toast at wrong pos when entering wrong password
class a7Popup extends React.Component {
    _isMounted = false;
    _token = "";
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
            error: '',
            open: false,
            gender: {id: 0},
            redGreenColorblind: false,
            dateOfBirth: '',
        };

        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.updateA7UserData = this.updateA7UserData.bind(this);

        this.options = toast.POSITION.BOTTOM_CENTER;
        this.cookies = this.props.cookies;


        this.openModal = this.openModal.bind(this);
        this.closeModal = this.closeModal.bind(this);
        this.handleGenderChange = this.handleGenderChange.bind(this);
        this.handleRedGreenColorblind = this.handleRedGreenColorblind.bind(this);
        this.setFormattedDate = this.setFormattedDate.bind(this);
    }

    componentDidMount() {
        //This should prevent operations after unmounting
        this._isMounted = true;
    }

    componentWillUnmount() {
        this._isMounted = false;
    }

    openModal() {
        this.setState({open: true});
    }

    closeModal() {
        this.setState({open: false});
    }

    handleUsernameChange(event) {
        this.setState({tpaId: event.target.value});
    }

    handlePasswordChange(event) {
        this.setState({password: event.target.value});
    }


    handleGenderChange(event) {
        this.state.gender = {id: event.target.value};
        this.setState(this.state);
    }

    handleRedGreenColorblind(event) {
        this.state.redGreenColorblind = (event.target.value == 1) ? true : false;
        this.setState(this.state);
        User.setRedGreenColorblind(this.state.redGreenColorblind);
    }

    setFormattedDate(date) {
        this.state.dateOfBirth = date;
        this.setState(this.state);
    }

    //TODO: Falsches passwort "hängt" das fenster auf
    handleSubmit(event) {
        event.preventDefault();
        this.state.sending = true;
        this.setState(this.state);
        const {t} = this.props;

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

                        switch (status) {
                            case 200:
                                //First we are only loading the absolutely necessary data.
                                User.setCookieCredentials(data);
                                let tempUser = User.get();
                                //We need the names, because the update call is asynchronous, and the user might see the HP before the update is finished.
                                tempUser.firstname = this.state.firstname;
                                tempUser.lastname = this.state.lastname;
                                User.set(tempUser);
                                this.setState({error: undefined});
                                this._token = data.token;
                                //TODO: is this the first sign in?
                                //we need further data
                                this.openModal();
                                break;
                            case 401:
                                this.setState({error: true});
                                toast.error(t('loginFailed'), this.options);
                                break;
                        }
                    });

                },
                (error) => {
                    this.setState({sending: false, error: true});
                    toast.error(t('loginFailed'), this.options);
                }
            );
    }

    //TODO: adjust method for proper use in profile
    updateA7UserData() {
        if (this.state.sending)
            return;

        // Store authentication values even after refresh.
        //Had to move this into this method, since otherwise react would unmount this comp too early, for some reason.
        this.cookies.set('auth', {
            token: this._token,
            user: User
        }, {path: '/'});

        this.state.sending = true;
        //TODO: If second log in, are doB, gender and rgcb still correct?
        axios.post('/user/update',
            {
                firstname: User.firstname,
                lastname: User.lastname,
                dateOfBirth: this.state.dateOfBirth,
                gender: this.state.gender,
                email: this.state.email,
                redGreenColorblind: this.state.redGreenColorblind
            }).then(({dat, status}) => {
            this.state.sending = false;
            const {t} = this.props;
            switch (status) {
                case 200:
                    if (this._isMounted) {
                        //we dont need to do this, if the comp is unmounted anyway
                        this.setState(this.state);
                        this.closeModal();
                    }
                    // Send event of updated login state.
                    this.props.updateNavigation();
                    // Redirect to front page.
                    this.props.history.push("/");
                    break;
                case 400:
                    toast.error(t('loadingFailed'), this.options);
                    break;
                case 401:
                    toast.error(t('noPermission'), this.options);
                    break;
                default:
                    toast.error(t('loginFailed'), this.options);
            }
        });
    }

    render() {
        const {t} = this.props;

        //TODO: Proper error handling if the user clicks outside of modal before entering data.
        let missingDataPopup =
            <div>
                <Popup open={this.state.open}>
                    <MissingDataPopup gender={this.handleGenderChange} setFormattedDate={this.setFormattedDate}
                                      redGreenColorblind={this.handleRedGreenColorblind}
                                      update={this.updateA7UserData}/>
                </Popup>
            </div>;

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
                {missingDataPopup}
            </div>)
    }
}

export default withCookies(translate()(a7Popup))