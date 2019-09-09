import React from "react";
import {translate} from "react-i18next";
import Moment from "moment";
import {toast} from "react-toastify";

//TODO: dynamisch überprüfen ob Namen/Mail fehlen?
//TODO: only on first sign in, get User to actually fill in the data?
//TODO: User shouldnt be able to submit without entering birthday?
class missingDataPopup extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            dateOfBirth: '',
            gender: {id: 0},
            redGreenColorblind: false
        };

        this.handleDateOfBirthChange = this.handleDateOfBirthChange.bind(this);
        this.handleGenderChange = this.handleGenderChange.bind(this);
        this.handleRedGreenColorblind = this.handleRedGreenColorblind.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleGenderChange(event) {
        this.state.gender = event.target.value;
        this.setState(this.state);
        this.props.gender(event);
    }

    handleDateOfBirthChange(event) {
        this.state.dateOfBirth = event.target.value;
        this.setState(this.state);
    }

    handleRedGreenColorblind(event) {
        this.state.redGreenColorblind = (event.target.value == 1) ? true : false;
        this.setState(this.state);
        this.props.redGreenColorblind(event);
    }

    handleSubmit(event) {
        event.preventDefault();
        const options = {
            position: toast.POSITION.BOTTOM_CENTER
        };

        //test for valid dates:
        var date = null;

        if (this.state.dateOfBirth != '') {
            date = Moment(this.state.dateOfBirth);
            if (!date.isValid()) {
                if (Moment(this.state.dateOfBirth, "DD.MM.YYYY").isValid()) {
                    date = Moment(this.state.dateOfBirth, "DD.MM.YYYY");
                } else {
                    toast.error(t('invalidDateFormat'), options);
                    return;
                }
            }

            date = date.format("YYYY-MM-DD");

            this.props.setFormattedDate(date);
            this.props.update(event);
        } else {
            toast.error(t('please enter a date'), options);
        }
    }

    render() {
        const {t} = this.props;

        return (
            <div>
                <h3> Leider fehlen noch ein paar wichtige Daten. Bitte ergänzen Sie diese: </h3>
                <form onSubmit={this.handleSubmit} className="info">
                    <fieldset>
                        <div className="form-group col-md-6 col-lg-6">
                            <label htmlFor="gender">{t('gender')}</label>
                            <select id="gender" value="0" name="gender" className="form-control" title={t('gender')}
                                    value={this.state.gender.id} onChange={this.handleGenderChange}>
                                <option value="0" disabled>{t('noInfo')}</option>
                                <option value="2">{t('female')}</option>
                                <option value="1">{t('male')}</option>
                            </select>
                        </div>
                        <div className="form-group col-md-6 col-lg-6">
                            <label htmlFor="dateOfBirth">{t('dateOfBirth')}</label>
                            <input type="text" name="dateOfBirth" id="dateOfBirth" className="form-control"
                                   value={this.state.dateOfBirth} onChange={this.handleDateOfBirthChange}/>
                        </div>
                    </fieldset>
                    <fieldset>
                        <div className="form-group col-lg-6 col-md-6">
                            <p><b>{t("redGreenColorblind")}</b></p>
                            <ul className="list-inline">
                                <li className="col-lg-4 col-md-4 col-xs-4 list-group-item">
                                    <label htmlFor="red-green-colorblind-yes" className="radio-inline">
                                        <input type="radio" value="1" id="red-green-colorblind-yes"
                                               name="redGreenColorblind" checked={this.state.redGreenColorblind == true}
                                               onChange={this.handleRedGreenColorblind}/>
                                        {t('yes')}
                                    </label>
                                </li>
                                <li className="col-lg-4 col-md-4 col-xs-4 list-group-item">
                                    <label htmlFor="red-green-colorblind-no" className="radio-inline">
                                        <input type="radio" value="0" id="red-green-colorblind-no"
                                               name="redGreenColorblind"
                                               checked={this.state.redGreenColorblind == false}
                                               onChange={this.handleRedGreenColorblind}/>
                                        {t('no')}
                                    </label>
                                </li>
                            </ul>
                        </div>
                    </fieldset>
                    <div className="form-actions container">
                        <button type="submit" className="btn btn-primary">{t('confirm')}</button>
                    </div>
                </form>
            </div>
        );
    }
}

export default translate()(missingDataPopup);