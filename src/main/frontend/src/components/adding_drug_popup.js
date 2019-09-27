import axios from "axios";
import React from "react";
import Popup from "reactjs-popup";
import {translate} from "react-i18next";

class AddingDrugPopup extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            open: true
        };
        this.openModal = this.openModal.bind(this);
        this.closeModal = this.closeModal.bind(this);

    }

    openModal() {
        this.setState({ open: true });
      }

    closeModal() {
        this.setState({ open: false });
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
		return (
           <div>
            <h3 className="centered-title">Drugname</h3>
            <form className="info">
                <fieldset>
                    <div className="form-group col-md-6 col-lg-6">
                        <label htmlFor="intakePattern">intake pattern</label>
                        <select id="intakePattern" value="0" name="intakePattern" className="form-control" title="intake pattern"
                                onChange={this.handleIntakePatternChange}>
                            <option value="2">daily</option>
                            <option value="1">every other day</option>
                        </select>
                    </div>
                </fieldset>
                <fieldset>
                    <div className="form-group col-lg-6 col-md-6">
                        <p><b>intake times</b></p>
                                <label htmlFor="intake-breakfast" className="intake-times">
                                    breakfast time
                                    <input type="checkbox" value="1" id="intake-breakfast"
                                           name="intakeTimes" 
                                           onChange={this.handleIntakeBreakfast}/>
                                           <span className="checkmark"></span>
                                </label>
                                <label htmlFor="intake-lunch" className="intake-times">
                                    lunch time
                                    <input type="checkbox" value="2" id="intake-lunch"
                                           name="intakeTimes"
                                           onChange={this.handleIntakeLunch}/>
                                           <span className="checkmark"></span>
                                </label>
                            <label htmlFor="intake-dinner" className="intake-times">
                                dinner time
                                <input type="checkbox" value="3" id="intake-dinner"
                                       name="intakeTimes"
                                       onChange={this.handleIntakeDinner}/>
                                       <span className="checkmark"></span>
                            </label>
                            <label htmlFor="intake-sleep" className="intake-times">
                                sleep time
                                <input type="checkbox" value="4" id="intake-sleep"
                                       name="intakeTimes"
                                       onChange={this.handleIntakeSleep}/>
                                       <span className="checkmark"></span>
                            </label>
                    </div>
                </fieldset>
                <fieldset>
                	<div className="form-actions container">
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