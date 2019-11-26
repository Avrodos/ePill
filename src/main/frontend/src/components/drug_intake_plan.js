import axios from "axios";
import React from "react";

import {Link} from "react-router-dom";

import {translate} from "react-i18next";
import Loading from "./loading";
import User from "./../util/User";
import moment from 'moment';
import EmptyList from "./empty_list";
import ProgressBar from "./progress_bar";
import Popup from "reactjs-popup";
import ChangingDrugIntakePopup from "./changing_drug_intake_popup";
import CheckBox from "./checkbox";
import {toast} from 'react-toastify';
import Clock from "./clock";
import PercentageCalculator from "./../util/PercentageCalculator";
import DrugIntakeSuccessPopup from "./drug_intake_success_popup";

// See https://facebook.github.io/react/docs/forms.html for documentation about forms.
class DrugIntakePlan extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            drugplanitems: [],
            interactions        : [],
            date : new Date(),
            percentage : 100,
            expandedRows: [],
            showProgressBar: false,
            errorMessage: '',
            backendError: false,
            showDrugIntakePopup: false,
            showDrugIntakeSuccessPopup: false,
            showDrugsNotTakenBeforeHour: 0
        };
        this.output = this.handleTakenChange.bind(this);
        this.handleShowProgressBar = this.handleShowProgressBar.bind(this);
        this.callbackDrugIsTaken = this.callbackDrugIsTaken.bind(this);
        this.closeChangingDrugIntakePopup = this.closeChangingDrugIntakePopup.bind(this);
        this.closeDrugIntakeSuccessPopup = this.closeDrugIntakeSuccessPopup.bind(this);
    }
    
    setBackendError(backendError, errorMessage) {
        this.state.backendError = backendError;
        this.state.errorMessage = errorMessage;
        this.setState(this.state);
        console.log("backendError=" + backendError + ", error message = " + errorMessage);
    }

    // This function is called before render() to initialize its state.
    componentWillMount() {
        this.getData();
    }

    getData() {
        this.state.loading = true;
        this.state.percentage = 100;
        this.setState(this.state);
        axios.get("/drugplan/intake/date", {
                        params: {
                          date: this.state.date
                        }
                      }).then(({ data }) => {
         this.state.drugplanitems = data;
         this.state.loading = false;
         this.setState(this.state);
        });
        axios.get("drug/interactions/taking").then(({data}) => {
                this.state.interactions = data.value;
                this.setState(this.state);
        });
    }
    
 getDrugsNotTakenBefore(beforeHour) {
    	
        console.log("getDrugsNotTakenBefore hour: " + beforeHour);
    	
        this.state.loading = true;
        this.setState(this.state);
        return axios.get("/drugplan/nottaken/date", {
                        params: {
                          date: this.state.date, 
                          hour: beforeHour
                        }
                      }, {
                          validateStatus: (status) => {
                              console.log("status=" + status);
                              return (status >= 200 && status < 300) || status == 400 || status == 401
                          }
                      }).then(({ data, status }) => {
                    	  console.log("status=" + status);
                          const {t} = this.props;

                          switch (status) {
                              case 200:
                                  console.log("getDrugsNotTakenBefore status 200");
                                  this.setBackendError(false, '');
                                  this.state.drugsNotTaken = data;
                                  this.state.loading = false;
                                  this.setState(this.state);
                                  return true;  //success
                                  break;
                              case 400:
                                 this.setBackendError(true, 'backend error (400)');
                                  break;
                              case 401:
                                 this.setBackendError(true, 'backend error (401)');
                                 break;
                          }
                          return false;
                      }
        );
    }
    
    // for html conversion
    createMarkup(text) {
        return { __html: text };
    }

    changeDate (incrementBy) {
        this.state.date.setTime(this.state.date.getTime() + incrementBy * 86400000);
        this.setState(this.state);
        this.getData();
    }

    handleDrugTabClick(rowId, drugplanitem) {
    	
        const currentExpandedRows = this.state.expandedRows;
        const isRowCurrentlyExpanded = currentExpandedRows.includes(rowId);
        var drugSelected;
        
        var newExpandedRows = isRowCurrentlyExpanded
          ? currentExpandedRows.filter(id => id !== rowId)
          : currentExpandedRows.concat(rowId);
        for (var i = 0; i < drugplanitem.drugsPlannedSameTime.length; i++) {
        	if (newExpandedRows.includes(drugplanitem.drugsPlannedSameTime[i]) && newExpandedRows.includes(rowId)) {
        		if (drugplanitem.drugsPlannedSameTime[i] != rowId){
        			newExpandedRows = newExpandedRows.filter(id => id !== drugplanitem.drugsPlannedSameTime[i]);
        		} else {
        			drugSelected = drugplanitem.drugsPlannedSameTime[i];
        			console.log("handleDrugTabClick, drug = " + drugSelected.name);
        	        this.setPercentageFollowingRows(drugplanitem, drugSelected);
        		}
            }
        }

        console.log('newExpandedRows,size = ' + newExpandedRows.length);
        
        this.setState({ expandedRows: newExpandedRows });
      }

    setPercentageFollowingRows(drugplanitem, drugSelected) {
    	if(drugplanitem && drugSelected ) {
    		console.log("halftimePeriod=" + drugSelected.halfTimePeriod);
        	const percentageCalculator = new PercentageCalculator();
        	percentageCalculator.setPercentagesForDrugFromDrugPlanItem(this.state.drugplanitems, drugplanitem, drugSelected);
    	}
    }
    
    handleTakenChange(isChecked, userDrugPlanItemId) {
        console.log("isChecked=" + isChecked + ", userDrugPlanItemId="+ userDrugPlanItemId);
        if (userDrugPlanItemId < 0) {
        	//intermediate step: find items before time
        	var beforeHour = - userDrugPlanItemId;
        	this.getDrugsNotTakenBefore(beforeHour).then(() => {
                console.log("getDrugsNotTakenBefore succeeded...");
                this.state.showDrugIntakePopup = true;
                this.state.showDrugsNotTakenBeforeHour = beforeHour;
                this.setState(this.state);
            })
            .catch(err => {
                console.log("error getDrugsNotTakenBefore...${err}");
            });
        } else {
        	//update drug taken
            var isDrugTaken = !isChecked;  //toggle
            this.setDrugTaken(isDrugTaken, userDrugPlanItemId);
        }
    }
    
    handleShowProgressBar() {
    	console.log("showProgressBar " + this.state.showProgressBar);
    	this.setState({ showProgressBar: !this.state.showProgressBar});
    }
    
    renderTimeString(timeString) {
    	var currentDate = new Date();
    	if (currentDate.getHours() == parseInt(timeString)) {
    		return ( <Clock showSeconds={true} />);
    	} else {
    		return timeString;
    	}
    }

    renderDrugPlanItems(drugplanitems) {
        const { t } = this.props;
        return drugplanitems.map(drugplanitem => {

                const plannedRow = [
                <tr key={drugplanitem} className="table-line-hover">
                        <td className="td-style">
                                {this.renderProgressBar(drugplanitem)}
                        </td>
                        <td className="td-style">
                            <div>{this.renderCheckBox(drugplanitem)}</div>
                        </td>
                        <td className="td-style">
                        	{this.renderTimeString(drugplanitem.timeString)}
                        </td>
                        <td className="td-style">
                                <div className="tab"><b>{this.renderDrugName(drugplanitem)}</b></div>
                        </td>
                        <td className="td-style">
                        	{this.renderMealSleepTime(drugplanitem)}
                        </td>
                </tr>                   
            ];
                if (drugplanitem.drugsPlannedSameTime.length > 0) {
                   for (var i = 0; i < drugplanitem.drugsPlannedSameTime.length; i++) {
                	   if(this.state.expandedRows.includes(drugplanitem.drugsPlannedSameTime[i])) {
                           plannedRow.push(
                               <tr key={"row-expanded-" + drugplanitem}>
                                   <td></td>
                                   <td></td>
                                   <td></td>
                                   <td>
                                   <p><b>{(t("usedWhen")) +": "}</b>{this.renderDiseases(drugplanitem.drugsPlannedSameTime[i])}</p>
                                       	<div>
                                       		{this.renderDrugIntakeIndications(drugplanitem.drugsPlannedSameTime[i])}
                                       		{this.renderInstructions(drugplanitem.drugsPlannedSameTime[i])}
                                       	</div>
                                       <div>
                                       {this.renderInteractions(drugplanitem.drugsPlannedSameTime[i])}
                                       <Link to={`/drug/${drugplanitem.drugsPlannedSameTime[i].link}`}>
                                               <h4>{t("forMoreInformation")}</h4>
                                       </Link>
                                       </div>
                                   </td>
                               </tr>
                             );
                           }
                   }
                	
                }
                return plannedRow;
        });
    }
    
    renderMealSleepTime(drugplanned) {
    	if (drugplanned.mealTime){
    		return (
                    <span className="glyphicon glyphicon-cutlery"></span>
    		)
    	} else if (drugplanned.sleep){
    		return (
                    <span className="glyphicon glyphicon-eye-close"></span>
    		)
    	} else {
    		return "";
    	}
    }
    
    renderInteractions(drugplanned) {
    	const { t } = this.props;
    	if (drugplanned.interactions === ""){
    		return "";
    	} else {
    		return (
    				<div className={"alert alert-dismissable" + (User.redGreenColorblind ? " danger-red-green-colorblind" : " alert-danger") }>
                    	<h5>{t("interaction")}</h5>
                    	<span dangerouslySetInnerHTML={this.createMarkup(drugplanned.interactions)} />
                    </div>	
    		);
    	}
    }

    renderDiseases(drug) {
        if (drug.diseases.length > 0) {
                return this.getDiseases(drug);
        } else {
                return "";
        };
    }

    getDiseases(drug) {
        var diseases = "";
                for (var j = 0; j <  drug.diseases.length; j++) {
                	if (j == 0) {
                		diseases = diseases + " " +  drug.diseases[j];
                	} else {
                		diseases = diseases + ", " +  drug.diseases[j];
                	}
                }
        return diseases;
    }

    renderDrugIntakeIndications(drugplanned) {
        const { t } = this.props;
        if (drugplanned.takeOnEmptyStomach) {
                 return (
                                 <p className="information"><span className="glyphicon glyphicon-info-sign"></span> {t("takeOnAnEmptyStomach")}</p>
                 )
        }
        if (drugplanned.takeOnFullStomach) {
                return (
                                <p className="information"><span className="glyphicon glyphicon-info-sign"></span> {t("takeOnAFullStomach")}</p>
                )
        }
        if (drugplanned.takeToMeals) {
                return (
                                <p className="information"><span className="glyphicon glyphicon-info-sign"></span> {t("takeToMeals")}</p>
                )
        }
        
    }
    
    renderInstructions(drugplanned) {
    	const { t } = this.props;
    	if (drugplanned.instructions.length != 0) {
        	return drugplanned.instructions.map(instructionName => {
            	const instruction = [
            		<p className="information" key={instructionName}><span className="glyphicon glyphicon-info-sign"></span> {instructionName}</p>
            	];
            	return instruction;
            	});
        } else {
        	return "";
        }
    }
    
    
    renderProgressBar(drugplanned) {
    	var drugplanned_percentage = drugplanned.percentage;
        const progressBar = this.state.showProgressBar ? <ProgressBar percentage={drugplanned_percentage}/> : null;
    	return progressBar;        
    }
    
    renderDrugName(drugplanned) {
        if (drugplanned.drugsPlannedSameTime.length > 0) {     
        	return drugplanned.drugsPlannedSameTime.map(drug => {
        	const clickCallback = () => this.handleDrugTabClick(drug, drugplanned);
        	const drugNameButtons = [
        		<button key={drug.userDrugPlanItemId} className={"tablinks drug-names " + (this.state.expandedRows.includes(drug) ? 'btn-drug-names-active' : 'btn-drug-names-inactive')} onClick={clickCallback}>{drug.name}</button>
        	];
        	return drugNameButtons;
        	});
        } else {
        	return "";
        };
    }
    
    renderCheckBox(drugplanned) {
        //if (!drugplanned.intermediateStep && drugplanned.drugTaken)   {
    	if (drugplanned.drugsPlannedSameTime.length > 0) {     
        	return drugplanned.drugsPlannedSameTime.map(drug => {
        			const drugCheckboxes = [
                        <CheckBox id={drug.userDrugPlanItemId} key={drug.userDrugPlanItemId} checked={drug.drugTaken}
                                        onChange={this.handleTakenChange.bind(this)}/>
                        	];
        		
        		
        		return drugCheckboxes;
        	});
    	} else {
    		return (
    				<CheckBox id={drugplanned.userDrugPlanItemId} key={drugplanned.userDrugPlanItemId} checked={false}
                    onChange={this.handleTakenChange.bind(this)}/>	
    		)
    	}
    }
    
    callbackDrugIsTaken(isDrugTaken, userDrugPlanItemId) {
        console.log('callbackDrugIsTaken --- ' + isDrugTaken + ", " + userDrugPlanItemId);
        this.getData();
    }
    
    closeChangingDrugIntakePopup() {
        console.log('closeChangingDrugIntakePopup...');
        this.state.showDrugIntakePopup = false;
        this.setState(this.state);
    }
    
    closeDrugIntakeSuccessPopup() {
    	console.log('closeDrugIntakeSuccessPopup...');
        this.state.showDrugIntakeSuccessPopup = false;
        this.setState(this.state);
    }

    recalculatePlan() {
                axios.post('/drugplan/calculate/date', { date: moment(this.state.date).format("DD.MM.YYYY")}, {
            validateStatus: (status) => {
                console.log("status=" + status);
                return (status >= 200 && status < 300) || status == 400 || status == 401
            }
                })
     .then(({data, status}) => {
         console.log("status=" + status);
         const {t} = this.props;

         switch (status) {
             case 200:
                 console.log("case status 200");
                 this.getData();
                 break;
             case 400:
                 break;
             case 401:
                console.log(data, "not permitted");
                        break;
         }
        });
    }

    setDrugTaken(isDrugTaken, userDrugPlanItemId) {
    	
    	const options = {
                position: toast.POSITION.BOTTOM_CENTER
            };
        console.log("setDrugTaken(userDrugPlanItemId=" + userDrugPlanItemId + ")");
        axios.post('/drugplan/drug/taken', { "drugTaken" : isDrugTaken, "userDrugPlanItemId" : userDrugPlanItemId } , {
                        validateStatus: (status) => {
                            console.log("status=" + status);
                            return (status >= 200 && status < 300) || status == 400 || status == 401
                        }
                            })
                  .then(({data, status}) => {
                     console.log("status=" + status);
                     const {t} = this.props;

                     switch (status) {
                         case 200:
                             console.log("case status 200");
                             if (isDrugTaken) {
                            	 this.state.showDrugIntakeSuccessPopup = true;
                             } else {
                                 toast.error(t('Remember to take it soon!'), options);
                             }

                             this.getData();
                             break;
                         case 400:
                             break;
                         case 401:
                            console.log(data, "not permitted");
                                    break;
                     }
            });
    }


    render() {
        const { t } = this.props;
        const drugplanitems = this.state.drugplanitems;
        var formatted_date = moment(this.state.date).format("DD.MM.YYYY");
        const firstname = User.firstname;
        const lastname = User.lastname;
        const description = t('drugIntakePlanDescriptionText');

        return (
            <div className="container no-banner">
                <div className="page-header">
                        <div className="text-date-change">
                            <button type="button" className="btn btn-sm btn-date-change" onClick={this.changeDate.bind(this, -1)}>
                            <span className="glyphicon glyphicon-triangle-left"></span>
                            </button>
                            <div className="mp-title">
                                <h3>{t("drugIntakePlan")}</h3>
                                <p>{" " + (t("for")) + " " + formatted_date}</p>
                            </div>
                            <button type="button" className="btn btn-sm btn-date-change" onClick={this.changeDate.bind(this, 1)}>
                            <span className="glyphicon glyphicon-triangle-right"></span>
                            </button>
                            <Popup trigger={<button type="button" className="btn btn-sm btn-like btn-add-drug">
                            <Link className="link-white" to="/drug/list"><span className="glyphicon glyphicon-white glyphicon-plus"></span>
                            </Link></button>} position="bottom center" on="hover">
                        	{t("addDrugsToMedicationPlan")}
                        	</Popup>
                            <Popup trigger={<button type="button" className="btn btn-sm btn-recalculate" onClick={() => this.recalculatePlan()}>
                            <span className="glyphicon glyphicon-white glyphicon-refresh"></span>
                           </button>} position="bottom center" on="hover">
                            {t("recalculatePlan")}
                        	</Popup>
                            
                        </div>
                </div>
                <div>
                	{User.isAuthenticated() && User.levelOfDetail > 1 &&
                		<div className="alert alert-info">
                                        <span className="glyphicon glyphicon-info-sign" aria-hidden="true"></span>
                                        <span className="sr-only">Info:</span>&nbsp;
                                        {description.replace("%User.firstname%", firstname).replace("%User.lastname%", lastname)}
                        </div>
                     }
                        {this.state.loading && <Loading />}
                        {!this.state.loading && drugplanitems && drugplanitems.length == 0 && (
                            <EmptyList />
                        )}
                        {this.state.showDrugIntakePopup && (
                                <Popup
                                    open={this.state.showDrugIntakePopup}
                                    position="right center" modal
                                trigger={<button type="button">Drugs Not Taken</button>}>
                                    {close =>
                                            <div>
                                               <a className="close" onClick={close}>&times;</a>
                                               <ChangingDrugIntakePopup drugsNotTaken={this.state.drugsNotTaken}
                                                        intakeHour={this.state.showDrugsNotTakenBeforeHour}
                                               			drugIsTakenCallback={this.callbackDrugIsTaken}
                                               			onSubmit={this.closeChangingDrugIntakePopup}
                                                    	updateNavigation={this.props.updateNavigation}/>
                                            </div>
                                    }
                                    </Popup>
                                )
                        }
                        {this.state.showDrugIntakeSuccessPopup && (
                                <Popup 
                                    open={this.state.showDrugIntakeSuccessPopup}
                                    position="right center" modal
                                    trigger={<button type="button" className="invisible-button">Success</button>}>
                                    {close =>
                                            <div>
                                               <a className="close" onClick={close}>&times;</a>
                                               <DrugIntakeSuccessPopup 
                                               	        message = {t("Well done!")}
                                               			onSubmit={this.closeDrugIntakeSuccessPopup}
                                                    	updateNavigation={this.props.updateNavigation}/>
                                            </div>
                                    }
                                    </Popup>
                                )
                        }
                        {!this.state.loading && drugplanitems && drugplanitems.length > 0 && (
                                <table id="drugplanitems" className="table-style">
                                        <thead>
                                                <tr>
                                                <th className="th-style"><Popup trigger={<button type="button" className="btn-half-time-period" onClick={() => this.handleShowProgressBar()}>{t("halfTimePeriod")}</button>} position="bottom center" on="hover">
                                                {t("showHideHalfTimePeriod")}
                                                </Popup></th>
                                                <th className="th-style"></th>
                                                <th className="th-style th-time">{t("time")}</th>
                                                <th className="th-style th-name">{t("name")}</th>
                                                <th className="th-style"></th>
                                                </tr>
                                        </thead>
                                <tbody>
                                        {this.renderDrugPlanItems(drugplanitems)}
                                </tbody>
                            </table>
                        )}
                 </div>
            </div>
        );
    }
}

export default translate()(DrugIntakePlan);
