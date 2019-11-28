import React from "react";
import {translate} from "react-i18next";
import {toast} from "react-toastify";

//Popup asking for required data that is missing after first Sign in (TPAs)
class missingDataPopup extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            redGreenColorblind: false
        };
        this.handleRedGreenColorblind = this.handleRedGreenColorblind.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleRedGreenColorblind(event) {
        this.state.redGreenColorblind = (event.target.value == 1) ? true : false;
        this.setState(this.state);
        this.props.redGreenColorblind(event);
    }

    handleSubmit(event) {
        event.preventDefault();
        const {t} = this.props;
        const options = {
            position: toast.POSITION.BOTTOM_CENTER
        };

        this.props.update(event);
    }

    render() {
        const {t} = this.props;

        return (
            <div>
                <h3>{t("missingData")}</h3>
                <form onSubmit={this.handleSubmit} className="info">
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