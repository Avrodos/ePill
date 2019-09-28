import axios from "axios";
import React from "react";
import Popup from "reactjs-popup";
import {translate} from "react-i18next";

class AddingDrugPopup extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            open: true,
            periodInDays: 1,
            intakeBreakfastTime: false,
            intakeSleepTime: false,
            intakeLunchTime: false,
            intakeDinnerTime: false
        };
        this.openModal = this.openModal.bind(this);
        this.closeModal = this.closeModal.bind(this);

        this.handlePeriodInDaysChange = this.handlePeriodInDaysChange.bind(this);
        this.handleIntakeBreakfastChange = this.handleIntakeBreakfastChange.bind(this);
        this.handleIntakeLunchChange = this.handleIntakeLunchChange.bind(this);
        this.handleIntakeDinnerChange = this.handleIntakeDinnerChange.bind(this);
        this.handleIntakeSleepChange = this.handleIntakeSleepChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    openModal() {
        this.setState({ open: true });
      }

    closeModal() {
        this.setState({ open: false });
      }
    
    
    handlePeriodInDaysChange(event) {
    	this.state.periodInDays = event.target.value;
        this.setState(this.state);
    }
    
    handleIntakeBreakfastChange(event) {
    	this.state.intakeBreakfastTime = event.target.checked;
        this.setState(this.state);
    }
    
    handleIntakeLunchChange(event) {
    	this.state.intakeLunchTime = event.target.checked;
        this.setState(this.state);
    }
    
    handleIntakeDinnerChange(event) {
    	this.state.intakeDinnerTime = event.target.checked;
        this.setState(this.state);
    }

    handleIntakeSleepChange(event) {
    	this.state.intakeSleepTime = event.target.checked;
        this.setState(this.state);
    }

    handleSubmit(event) {
        //event.preventDefault();
    }
    
	render() {
		return (
           <div>
            <h3 className="centered-title">Drugname</h3>
            <form onSubmit={this.handleSubmit()} className="info">
                <fieldset>
                    <div className="form-group col-md-6 col-lg-6">
                        <label htmlFor="intakePattern">intake pattern</label>
                        <select id="periodInDays" name="intakePattern" value={this.state.periodInDays} 
                        	className="form-control" title="intake pattern"
                                onChange={this.handlePeriodInDaysChange}>
                            <option value="1">daily</option>
                            <option value="2">every other day</option>
                            <option value="7">every week</option>
                            <option value="30">every month</option>
                        </select>
                    </div>
                </fieldset>
                
                <fieldset>
                    <div className="form-group col-lg-6 col-md-6">
                        <p><b>intake times</b></p>
                                <label htmlFor="intake-breakfast" className="intake-times">
                                    breakfast time
                                    <input type="checkbox" id="intake-breakfast"
                                           name="intakeTimes" 
                                           onChange={this.handleIntakeBreakfastChange}/>
                                           <span className="checkmark"></span>
                                </label>
                                <label htmlFor="intake-lunch" className="intake-times">
                                    lunch time
                                    <input type="checkbox" id="intake-lunch"
                                           name="intakeTimes"
                                           onChange={this.handleIntakeLunchChange}/>
                                           <span className="checkmark"></span>
                                </label>
                            <label htmlFor="intake-dinner" className="intake-times">
                                dinner time
                                <input type="checkbox" value="3" id="intake-dinner"
                                       name="intakeTimes"
                                       onChange={this.handleIntakeDinnerChange}/>
                                       <span className="checkmark"></span>
                            </label>
                            <label htmlFor="intake-sleep" className="intake-times">
                                sleep time
                                <input type="checkbox" value="4" id="intake-sleep"
                                       name="intakeTimes"
                                       onChange={this.handleIntakeSleepChange}/>
                                       <span className="checkmark"></span>
                            </label>
                    </div>
                </fieldset>
                <fieldset>
            	<div>
                	<button type="submit" className="btn btn-primary btn-next">confirm</button>
                	<button type="cancel" className="btn btn-primary btn-next">cancel</button>
                </div>
                </fieldset>
            </form>
           </div>
		);
	}
}

export default translate() (AddingDrugPopup);