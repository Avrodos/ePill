import axios from "axios";
import React from "react";

import {translate} from "react-i18next";
import {toast} from 'react-toastify';

import {withCookies} from "react-cookie";
import User from "../util/User";
import GoogleLogin from "react-google-login";
import Popup from "reactjs-popup";
import MissingDataPopup from "./missingDataPopup";

//TODO: Fehlermeldung falls login fehlschlägt aufgrund von 2 mal gleiche mail?
//INFO: Testing mail for google is: testmailforepill@gmail.com : thirdPartyAccountService
class googlePopup extends React.Component {
    _isMounted = false;
    _token = "";
    _levelOfDetail = 3;
    _preferredFontSize = 'defaultFontSize';

    constructor(props) {
        super(props);
        this.state = {
            sending : '',
            tpaId : '',
            service: '',
            open: false,
            gender: {id: 0},
            redGreenColorblind: false,
            dateOfBirth: '',
            email: ''
        };

        this.authenticateWithGoogle = this.authenticateWithGoogle.bind(this);
        this.updateUserData = this.updateUserData.bind(this);

        this.openModal = this.openModal.bind(this);
        this.closeModal = this.closeModal.bind(this);
        this.handleGenderChange = this.handleGenderChange.bind(this);
        this.handleRedGreenColorblind = this.handleRedGreenColorblind.bind(this);
        this.setFormattedDate = this.setFormattedDate.bind(this);

        this.options = toast.POSITION.BOTTOM_CENTER;
        this.cookies = this.props.cookies;
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

                        this._levelOfDetail = data.user.levelOfDetail;
                        this._preferredFontSize = data.user.preferredFontSize;
                        this.state.email = data.user.username;
                        this.setState({
                            email: this.state.email,
                            levelOfDetail: this._levelOfDetail,
                            preferredFontSize: this._preferredFontSize,
                            redGreenColorblind: this.state.redGreenColorblind,
                            error: undefined
                        });
                        this._token = data.token;
                        if (data.user.firstSignIn) {
                            //we need further data
                            this.openModal();
                        } else {
                            this.updateUserData();
                        }
                        break;
                    case 401:
                        this.setState({error: true});
                        toast.error(t('loginFailed'), options);
                        break;
                }
            });
    }

    updateUserData() {
        if (this.state.sending)
            return;

        // Store authentication values even after refresh.
        //Had to move this into this method, since otherwise react would unmount this comp too early, for some reason.
        this.cookies.set('auth', {
            token: this._token,
            user: User
        }, {path: '/'});

        //null handling on the backend makes sure we dont lose data
        this.state.sending = true;
        let newData;
        if (this.state.gender.id === 0) {
            //In this case, we would be losing our previously gained information.
            //Since the default is 0, we will always have the correct gender this way.

            //has to contain everything that cannot be handled by != null in UserService.java
            newData = {
                firstname: User.firstname,
                lastname: User.lastname,
                dateOfBirth: this.state.dateOfBirth,

                redGreenColorblind: this.state.redGreenColorblind,
                levelOfDetail: this._levelOfDetail,
                preferredFontSize: this._preferredFontSize,
                firstSignIn: false,
                email: this.state.email
            };
        } else {
            newData = {
                firstname: User.firstname,
                lastname: User.lastname,
                dateOfBirth: this.state.dateOfBirth,
                gender: this.state.gender,
                redGreenColorblind: this.state.redGreenColorblind,
                levelOfDetail: this._levelOfDetail,
                preferredFontSize: this._preferredFontSize,
                firstSignIn: false,
                email: this.state.email
            };
        }


        axios.post('/user/update', newData
        ).then(({dat, status}) => {
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
        const responseSuccess = (response) => {
            this.state.tpaId = response.getAuthResponse().id_token;
            this.setState(this.state);
            this.authenticateWithGoogle();
        };

        const responseFailure = (response) => {
            //empty function, in case the user cancels the sign in attempt.
        };

        let googleLoginButton =
            <div>
                <GoogleLogin
                    clientId="583900150012-agjlvgr8gjsj8cv5f8fkiv3fjl9keu1j.apps.googleusercontent.com"
                    buttonText="Login with Google"
                    onSuccess={responseSuccess}
                    onFailure={responseFailure}
                    cookiePolicy={'single_host_origin'}
                />
            </div>;

        let googleRegisterButton =
            <div>
                <GoogleLogin
                    clientId="583900150012-agjlvgr8gjsj8cv5f8fkiv3fjl9keu1j.apps.googleusercontent.com"
                    buttonText="Register with Google"
                    onSuccess={responseSuccess}
                    onFailure={responseFailure}
                    cookiePolicy={'single_host_origin'}
                />
            </div>;

        let missingDataPopup =
            <div>
                <Popup open={this.state.open}>
                    <MissingDataPopup gender={this.handleGenderChange} setFormattedDate={this.setFormattedDate}
                                      redGreenColorblind={this.handleRedGreenColorblind}
                                      update={this.updateUserData}/>
                </Popup>
            </div>;

        return (
            <div>
                {this.props.location.pathname === "/user/login" ? googleLoginButton : googleRegisterButton}
                {missingDataPopup}
            </div>);

    }
}

export default withCookies(translate()(googlePopup))