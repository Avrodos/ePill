import axios from "axios";
import React from "react";
import Popup from "reactjs-popup";
import {withCookies} from "react-cookie";
import {translate} from "react-i18next";
import CheckBox from "./checkbox";
import {toast} from 'react-toastify';

class ChangingDrugIntakePopup extends React.Component {
	
	_isMounted = false;

	constructor(props) {
        super(props);
        this.state = {
            drugsNotTaken: [],
            intakeHour: 0,
            sending : '',
            open : false,
            errorMessage: '',
            backendError: false,
            selectedItems: [],
            callbackParent: 0
        };
        this.handleTakenChange = this.handleTakenChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }
        
	componentWillReceiveProps(props) {
        console.log("... componentWillReceiveProps - drugsNotTaken=" + this.props.drugsNotTaken +
                        ", intakeHour" + this.props.intakeHour + ", callback=" + this.props.callbackParent );
        this.setState({ drugsNotTaken: this.props.drugsNotTaken,
                intakeHour: this.props.intakeHour,
                callbackParent: this.props.callbackParent });
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
    
    handleSubmit(event) {
        event.preventDefault();
        console.log("handleSubmit");
    }

    postDrugTaken(isDrugTaken, userDrugPlanItemId, intakeHour) {
        const options = {
                position: toast.POSITION.BOTTOM_CENTER
            };
        console.log("postDrugTaken(userDrugPlanItemId=" + userDrugPlanItemId + ")");
        axios.post('/drugplan/drug/taken', { "drugTaken" : isDrugTaken, "userDrugPlanItemId" : userDrugPlanItemId, "intakeHour":  intakeHour} , {
                        validateStatus: (status) => {
                            console.log("status=" + status);
                            return (status >= 200 && status < 300) || status == 400 || status == 401
                        }
                            })
                  .then(({data, status}) => {
                     console.log("postDrugTaken status=" + status);
                     const {t} = this.props;

                     switch (status) {
                         case 200:
                             console.log("postDrugTaken status 200");
                             if (isDrugTaken) {
                                 this.setDrugTaken(isDrugTaken, userDrugPlanItemId);
                                 toast.success(t('Well done!'), options);
                                 this.state.callbackParent(isDrugTaken, userDrugPlanItemId);   
                             } else {
                                 toast.error(t('Remember to take it soon!'), options);
                             }
                             break;
                         case 400:
                             break;
                         case 401:
                            console.log(data, "not permitted");
                                    break;
                     }
            });
    }



    renderDrugName(drugsNotTaken) {
        if (drugsNotTaken.drugsPlannedSameTime.length > 0) {     
        	return drugsNotTaken.drugsPlannedSameTime.map(drug => {
        		return ( <p key="p{drug.userDrugPlanItemId}">{drug.name}</p>	);
        	});
        } else {
        	return "";
        };
    }
    
    renderCheckBox(drug) {
		return (
				<CheckBox id={drug.userDrugPlanItemId} key={drug.userDrugPlanItemId} checked={this.isDrugTaken(drug)}
                			onChange={this.handleTakenChange.bind(this)}/>	
		)
    }
    
    isDrugTaken(drug) {
        if (drug.drugsPlannedSameTime.length == 1) {     
        	return drug.drugsPlannedSameTime[0].drugTaken;
        } else {
        	return false;
        };
    }

    setDrugTaken(isChecked, userDrugPlanItemId) {
    	console.log("setDrugTaken " + isChecked + ", " + userDrugPlanItemId);
    	var i;
    	var indexFound=-1;
    	for (i=0; i < this.state.drugsNotTaken.length; i++) {
    		if (this.state.drugsNotTaken[i].drugsPlannedSameTime[0].userDrugPlanItemId == userDrugPlanItemId) {
    			console.log("index found = " + i);
    			indexFound = i;
    			//this.state.drugsNotTaken[i].drugsPlannedSameTime[0].drugTaken = isChecked;
    			this.setState(this.state);
    		}
    	}
    	if (indexFound >= 0) {
    		console.log("removing drug taken from list");
    		this.state.drugsNotTaken.splice(indexFound, 1);
    		this.setState(this.state);
    		console.log("removed new length = " + this.state.drugsNotTaken.length);
    	}
    }
    
    handleTakenChange(isChecked, userDrugPlanItemId) {
        console.log("isChecked=" + isChecked + ", userDrugPlanItemId="+ userDrugPlanItemId);
    	//update drug taken
        var isDrugTaken = !isChecked;  //toggle
        this.postDrugTaken(isDrugTaken, userDrugPlanItemId, this.state.intakeHour);
    }
	
    renderDrugsNotTaken(drugsNotTaken) {
    	  const { t } = this.props;
          return drugsNotTaken.map(drugNotTaken => {

                  const row = [
                  <tr key="row{drugNotTaken.userDrugPlanItemId}" className="table-line-hover">
                          <td className="td-style">
                              <div>{this.renderCheckBox(drugNotTaken)}</div>
                          </td>
                          <td className="td-style">{drugNotTaken.timeString}</td> 
                          <td className="td-style">{this.formatTime(this.props.intakeHour)}</td>
                          <td className="td-style">
                                  <div className="tab"><b>{this.renderDrugName(drugNotTaken)}</b></div>
                          </td>
                  </tr>                   
              ];
              return row;
          });
    }
    
    renderIntakeHour() {
    	console.log("renderIntakeHour...");
    	var intakeHourString = "Intake time " + this.formatTime(this.props.intakeHour);
    	console.log(intakeHourString);
    	return (
    			<h3 className="centered-title">{intakeHourString}</h3>
    			);
    }
    
    //i.e.:  10 -> "10:00"
    formatTime(hour) {
      var timeString;
  	  if (hour < 10) {
  		  timeString = "0" + hour;
  	  } else {
  		timeString = hour;
  	  }
  	  timeString = timeString + ":00";
  	  return timeString;
    }
    
	render () {
        const {t} = this.props;

        return (
        <div className="popup-content">
            <div>
                {/* Popup content */}
                {this.renderIntakeHour()}
                <form onSubmit={this.handleSubmit} >
                <table id="drugsnotplanned" className="table-style">
                	<thead>
                        <tr>
                        <th className="th-style">drug taken</th>
                        <th className="th-style">planned time</th>
                        <th className="th-style">taken time</th>
                        <th className="th-style">drug</th>
                        </tr>
                    </thead>
	                <tbody>
	                        {this.renderDrugsNotTaken(this.state.drugsNotTaken)}
	                </tbody>
        		</table>
                
                    <fieldset>
                    <div>
                        <button type="submit" className="btn btn-primary btn-next">{t("confirm")}</button>
                    </div>
                    </fieldset>
                </form>
            </div>
        </div>)
	}
}
export default withCookies(translate()(ChangingDrugIntakePopup));