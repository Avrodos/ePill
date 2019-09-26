import axios from "axios";
import React from "react";
import Popup from "reactjs-popup";
import {translate} from "react-i18next";

class AddingDrugPopup extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            open: this.props.open
        };


    }
    
    componentWillReceiveProps(props) {
		  this.setState({ open: props.open });  
	}
    
    componentDidMount() {
        //This should prevent operations after unmounting
        this._isMounted = true;
    }

    componentWillUnmount() {
        this._isMounted = false;
    }

    
    handleIntakePatternChange(event) {
        
    }
    
    handleIntakeBreakfast(event) {
        
    }
    
    handleIntakeLunch(event) {
        
    }
    
    handleIntakeDinner(event) {
        
    }

    handleIntakeSleep(event) {
        
    }


	render() {

        let missingDataPopup =
                <div>
                <h3>Drugname</h3>
                <form className="info">
                    <fieldset>
                        <div className="form-group col-md-6 col-lg-6">
                            <label htmlFor="intakePattern">{t('intake pattern')}</label>
                            <select id="intakePattern" value="0" name="intakePattern" className="form-control" title={t('intake pattern')}
                                    onChange={this.handleIntakePatternChange}>
                                <option value="0" disabled>{t('noInfo')}</option>
                                <option value="2">{t('daily')}</option>
                                <option value="1">{t('every other day')}</option>
                            </select>
                        </div>
                    </fieldset>
                    <fieldset>
                        <div className="form-group col-lg-6 col-md-6">
                            <p><b>{t("intake times")}</b></p>
                            <ul className="list-inline">
                                <li className="col-lg-4 col-md-4 col-xs-4 list-group-item">
                                    <label htmlFor="intake-breakfast" className="radio-inline">
                                        <input type="radio" value="1" id="intake-breakfast"
                                               name="intakeTimes" 
                                               onChange={this.handleIntakeBreakfast}/>
                                        {t('breakfast time')}
                                    </label>
                                </li>
                                <li className="col-lg-4 col-md-4 col-xs-4 list-group-item">
                                    <label htmlFor="intake-lunch" className="radio-inline">
                                        <input type="radio" value="2" id="intake-lunch"
                                               name="intakeTimes"
                                               onChange={this.handleIntakeLunch}/>
                                        {t('lunch time')}
                                    </label>
                                </li>
                                <li className="col-lg-4 col-md-4 col-xs-4 list-group-item">
                                <label htmlFor="intake-dinner" className="radio-inline">
                                    <input type="radio" value="3" id="intake-dinner"
                                           name="intakeTimes"
                                           onChange={this.handleIntakeDinner}/>
                                    {t('dinner time')}
                                </label>
                                </li>
                                <li className="col-lg-4 col-md-4 col-xs-4 list-group-item">
                                <label htmlFor="intake-sleep" className="radio-inline">
                                    <input type="radio" value="4" id="intake-sleep"
                                           name="intakeTimes"
                                           onChange={this.handleIntakeSleep}/>
                                    {t('sleep time')}
                                </label>
                                </li>
                            </ul>
                        </div>
                    </fieldset>
                    <div className="form-actions container">
                        <button type="submit" className="btn btn-primary">{t('confirm')}</button>
                    </div>
                    <div className="form-actions container">
                    <button type="cancel" className="btn btn-primary">{t('cancel')}</button>
                    </div>
                </form>
            </div>

		return (
			<Popup trigger={this.state.open == true}>
                {missingDataPopup}
            </Popup>	
		);
	}
}
export default translate() (AddingDrugPopup);