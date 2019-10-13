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

// See https://facebook.github.io/react/docs/forms.html for documentation about forms.
class DrugIntakePlan extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            drugsplanned: [],
            interactions        : [],
            date : new Date(),
            percentage : 100,
            expandedRows: [],
            showProgressBar: false,
            errorMessage: '',
            backendError: false,
            showDrugIntakePopup: false,
            showDrugsNotTakenBeforeHour: 0
        };
        this.output = this.handleTakenChange.bind(this);
        this.handleShowProgressBar = this.handleShowProgressBar.bind(this);
        this.callbackDrugIntakePopup = this.callbackDrugIntakePopup.bind(this);
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
         this.state.drugsplanned = data;
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

    handleRowClick(rowId, drugsplanned) {
    	
        const currentExpandedRows = this.state.expandedRows;
        const isRowCurrentlyExpanded = currentExpandedRows.includes(rowId);
        
        var newExpandedRows = isRowCurrentlyExpanded
          ? currentExpandedRows.filter(id => id !== rowId)
          : currentExpandedRows.concat(rowId);
        for (var i = 0; i < drugsplanned.drugsPlannedSameTime.length; i++) {
        	if (newExpandedRows.includes(drugsplanned.drugsPlannedSameTime[i]) && newExpandedRows.includes(rowId)) {
        		if (drugsplanned.drugsPlannedSameTime[i] != rowId){
        			newExpandedRows = newExpandedRows.filter(id => id !== drugsplanned.drugsPlannedSameTime[i])
        		}
            }
        }

        this.setState({ expandedRows: newExpandedRows });
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

    renderDrugsPlanned(drugsplanned) {
        const { t } = this.props;
        return drugsplanned.map(drugplanned => {

                const plannedRow = [
                <tr key={drugplanned} className="table-line-hover">
                        <td className="td-style">
                                {this.renderProgressBar(drugplanned)}
                        </td>
                        <td className="td-style">
                            <div>{this.renderCheckBox(drugplanned)}</div>
                        </td>
                        <td className="td-style">
                        	{this.renderTimeString(drugplanned.timeString)}
                        </td>
                        <td className="td-style">
                                <div className="tab"><b>{this.renderDrugName(drugplanned)}</b></div>
                        </td>
                </tr>                   
            ];
                if (drugplanned.drugsPlannedSameTime.length > 0) {
                   for (var i = 0; i < drugplanned.drugsPlannedSameTime.length; i++) {
                	   if(this.state.expandedRows.includes(drugplanned.drugsPlannedSameTime[i])) {
                           plannedRow.push(
                               <tr key={"row-expanded-" + drugplanned}>
                                   <td></td>
                                   <td></td>
                                   <td></td>
                                   <td>
                                   <p><b>{(t("usedWhen")) +": "}</b>{this.renderDiseases(drugplanned.drugsPlannedSameTime[i])}</p>
                                       	<div>
                                       		{this.renderDrugIntakeIndications(drugplanned.drugsPlannedSameTime[i])}
                                       		{this.renderFoodtoAvoid(drugplanned.drugsPlannedSameTime[i])}
                                       	</div>
                                       <div>
                                       {this.renderInteractions(drugplanned)}
                                       <Link to={`/drug/${drugplanned.drugsPlannedSameTime[i].link}`}>
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
    
    renderInteractions(drugplanned) {
    	const { t } = this.props;
    	if (drugplanned.drugsPlannedSameTime.length > 1) {
    		return (
    				<div className={"alert alert-dismissable" + (User.redGreenColorblind ? " danger-red-green-colorblind" : " alert-danger") }>
                    	<h5>{t("interaction")}</h5>
                    	<span dangerouslySetInnerHTML={this.createMarkup(drugplanned.interaction)} />
                    </div>	
    		);
    	} else {
    		return "";
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
    
    renderFoodtoAvoid(drugplanned) {
    	const { t } = this.props;
    	if (drugplanned.food.length != 0) {
        	return drugplanned.food.map(foodName => {
            	const drugFoodToAvoid = [
            		<p className="information" key={foodName}><span className="glyphicon glyphicon-info-sign"></span> {t("tryToAvoid")+ " " +foodName}</p>
            	];
            	return drugFoodToAvoid;
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
        	const clickCallback = () => this.handleRowClick(drug, drugplanned);
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
    
    callbackDrugIntakePopup(isDrugTaken, userDrugPlanItemId) {
        console.log('callbackDrugIntakePopup --- ' + isDrugTaken + ", " + userDrugPlanItemId);
        this.getData();
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
                                 toast.success(t('Well done!'), options);
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
        const drugsplanned = this.state.drugsplanned;
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
                            <button type="button" className="btn btn-sm btn-add btn-add-drug"><Link to="/drug/list">{t("addDrugsToMedicationPlan")}</Link></button>
                            <button type="button" className="btn btn-sm btn-recalculate" onClick={() => this.recalculatePlan()}>{t("recalculatePlan")}
                                <span className="glyphicon glyphicon-white glyphicon-refresh"></span>
                                </button>
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
                        {!this.state.loading && drugsplanned && drugsplanned.length == 0 && (
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
                                                    callbackParent={this.callbackDrugIntakePopup}
                                                    updateNavigation={this.props.updateNavigation}/>
                                            </div>
                                    }
                                    </Popup>
                                )
                        }
                        {!this.state.loading && drugsplanned && drugsplanned.length > 0 && (
                                <table id="drugsplanned" className="table-style">
                                        <thead>
                                                <tr>
                                                <th className="th-style"><Popup trigger={<button type="button" className="btn-half-time-period" onClick={() => this.handleShowProgressBar()}>{t("halfTimePeriod")}</button>} position="bottom center" on="hover">
                                                {t("showHideHalfTimePeriod")}
                                                </Popup></th>
                                                <th className="th-style"></th>
                                                <th className="th-style th-time">{t("time")}</th>
                                                <th className="th-style th-name">{t("name")}</th>
                                                </tr>
                                        </thead>
                                <tbody>
                                        {this.renderDrugsPlanned(drugsplanned)}
                                </tbody>
                            </table>
                        )}
                 </div>
            </div>
        );
    }
}

export default translate()(DrugIntakePlan);
