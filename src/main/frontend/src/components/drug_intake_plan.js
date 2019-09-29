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

// See https://facebook.github.io/react/docs/forms.html for documentation about forms.
class DrugIntakePlan extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            drugsplanned: [],
            interactions        : [],
            date : new Date(),
            percentage : 100,
            expandedRows: []
        };
        this.output = this.handleTakenChange.bind(this)
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

    // for html conversion
    createMarkup(text) {
        return { __html: text };
    }

    changeDate (incrementBy) {
        this.state.date.setTime(this.state.date.getTime() + incrementBy * 86400000);
        this.setState(this.state);
        this.getData();
    }

    handleRowClick(rowId) {

        const currentExpandedRows = this.state.expandedRows;
        const isRowCurrentlyExpanded = currentExpandedRows.includes(rowId);

        const newExpandedRows = isRowCurrentlyExpanded
          ? currentExpandedRows.filter(id => id !== rowId)
          : currentExpandedRows.concat(rowId);

        this.setState({ expandedRows: newExpandedRows });
      }

    handleTakenChange(isChecked, userDrugPlanItemId) {
        // parent class change handler is always called with field name and value
        //this.setState({[field]: value});
        console.log("isChecked=" + isChecked + ", userDrugPlanItemId="+ userDrugPlanItemId);
        var isDrugTaken = !isChecked;  //toggle
        this.setDrugTaken(isDrugTaken, userDrugPlanItemId);
    }

    renderCheckBox(drugplanned) {
        //if (!drugplanned.intermediateStep && drugplanned.drugTaken)   {
                        return (
                                <CheckBox id={drugplanned.userDrugPlanItemId} checked={drugplanned.drugTaken}
                                                onChange={this.handleTakenChange.bind(this)}/>
                        )
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
                            {this.renderCheckBox(drugplanned)}
                        </td>
                        <td className="td-style">{drugplanned.timeString}</td>
                        <td className="td-style">
                                <b>{this.renderDrugName(drugplanned)}</b>
                        </td>
                        <td className="td-style">
                                {this.renderExpandableButton(drugplanned)}
                      </td>
                </tr>                   
            ];
                if (drugplanned.drugNamesSameTime !== null) {
                        if(this.state.expandedRows.includes(drugplanned)) {
                    plannedRow.push(
                        <tr key={"row-expanded-" + drugplanned}>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>
                            <p>{(t("usedWhen"))+": "+drugplanned.drugDiseases}</p> 
                            	{this.renderDrugIntakeIndications(drugplanned)}
                                {drugplanned.personalizedInformation && <section className="minimum-summary" dangerouslySetInnerHTML={this.createMarkup(drugplanned.personalizedInformation)} />}
                            </td>
                            <td></td>
                        </tr>
                    );
                }
                }
                return plannedRow;
        });
    }
    
    renderDrugIntakeIndications(drugplanned) {
    	const { t } = this.props;
    	if (drugplanned.takeOnEmptyStomach) {
    		 return (
    				 <p className="information"><span class="glyphicon glyphicon-info-sign"></span> {t("takeOnAnEmptyStomach")}</p> 
    		 ) 
    	}
    	if (drugplanned.takeOnFullStomach) {
    		return (
    				<p className="information"><span class="glyphicon glyphicon-info-sign"></span> {t("takeOnAFullStomach")}</p>
    		)
    	}
    	if (drugplanned.takeToMeals) {
    		return (
    				<p className="information"><span class="glyphicon glyphicon-info-sign"></span> {t("takeToMeals")}</p>
    		)
    	}
    }
    
    renderProgressBar(drugplanned) {
        var drugplanned_percentage = drugplanned.percentage;
                return (
                        <ProgressBar percentage={drugplanned_percentage} />     
                );
    }

    renderDrugName(drugplanned) {
        if (drugplanned.drugNamesSameTime !== undefined) {
                return drugplanned.drugNamesSameTime;
        } else if (drugplanned.drugName !== undefined) {
                return drugplanned.drugName;
        } else {
                return "";
        };
    }

    renderExpandableButton(drugplanned) {
        const clickCallback = () => this.handleRowClick(drugplanned);

        if (drugplanned.drugNamesSameTime !== null) {
                return (
                                <button type="button" className="btn btn-sm btn add" onClick={clickCallback}>
                    <span className={"glyphicon "+ ( this.state.expandedRows.includes(drugplanned) ? 'glyphicon-minus' : 'glyphicon-plus')}></span>
                    </button>
                );
        }

        return "";
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
                    {drugsplanned.length > 1 && User.isAuthenticated() && (
                        <div>
                            <p>you have {drugsplanned.length} planned drugs</p>
                        </div>
                    )}

                    <div>
                        {this.state.loading && <Loading />}
                        {!this.state.loading && drugsplanned && drugsplanned.length == 0 && (
                            <EmptyList />
                        )}
                        {!this.state.loading && drugsplanned && drugsplanned.length > 0 && (
                                <table id="drugsplanned" className="table-style">
                                        <thead>
                                                <tr>
                                                        <th className="th-style">half-time-period</th>
                                                <th className="th-style"></th>
                                                <th className="th-style">time</th>
                                                <th className="th-style">name</th>
                                                <th className="th-style"></th>
                                        </tr>
                                </thead>
                                <tbody>
                                        {this.renderDrugsPlanned(drugsplanned)}
                                </tbody>
                            </table>
                        )}
                    </div>
                </div>

            </div>
        );
    }
}

export default translate()(DrugIntakePlan);
