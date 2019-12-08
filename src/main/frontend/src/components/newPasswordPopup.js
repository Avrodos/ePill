import React from "react";
import {toast} from "react-toastify";
import axios from "axios";
import {translate} from "react-i18next";

class newPasswordPopup extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            id: props.id,
            username: props.username,
            password: '',
            passwordRepeat: '',
        };

        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handlePasswordRepeatChange = this.handlePasswordRepeatChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
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

        if (this.state.password != this.state.passwordRepeat) {
            toast.error(t('passwordsDifferent'), options);
            return;
        }
        this.state.sending = true;
        this.setState(this.state);

        axios.post('/user/update/password', this.state)
            .then(({data, status}) => {

                this.state.sending = false;
                this.setState(this.state);
                switch (status) {
                    case 200:
                        toast.success(t('passwordChangeSuccess'), options);
                        this.props.closeModal();
                        break;
                    default:
                        toast.error(t('errorOccurred'), options);
                        break;
                }
            });
    }

    render() {
        const {t} = this.props;

        return (
            <div>
                <div className="page-header">
                    <h2>{t('transformRequiresPasswordChange')}</h2>
                    <h2>{t('choosePassword')}</h2>
                    <p>{t('noteOnlyFormLogin')}</p>
                </div>
                <div className="form-group">
                    <label htmlFor="password">{t('password')}</label>
                    <input type="password" name="password" id="password" className="form-control"
                           value={this.state.password} onChange={this.handlePasswordChange}/>
                </div>

                <div className="form-group">
                    <label htmlFor="password_rep">{t('passwordRepeat')}</label>
                    <input type="password" name="password_rep" id="password_rep" className="form-control"
                           value={this.state.password_repeat} onChange={this.handlePasswordRepeatChange}/>
                </div>

                <button type="submit" className="btn btn-primary" onClick={this.handleSubmit}>{t('confirm')} </button>

            </div>
        )
    }

}

export default translate()(newPasswordPopup);
