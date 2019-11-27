import axios from "axios";
import React from "react";

import {translate} from "react-i18next";
import {toast} from 'react-toastify';

import {withCookies} from "react-cookie";

import Popup from "reactjs-popup";

class DrugIntakePopup extends React.Component {
    _isMounted = false;
    _token = "";
    _levelOfDetail = 3;
    _preferredFontSize = 'defaultFontSize';

    constructor(props) {
        super(props);
        this.state = {
                drugId: this.props.drugId,
                drugName: this.props.drugName,
            sending : '',
            errorMessage: '',
            backendError: false,
            open: false,
            dateOfBirth: '',
            periodInDays: 1,
            intakeBreakfastTime: false,
            intakeSleepTime: false,
            intakeLunchTime: false,
            intakeDinnerTime: false,
            onSubmit: 0
        };

        this.handlePeriodInDaysChange = this.handlePeriodInDaysChange.bind(this);
        this.handleIntakeBreakfastChange = this.handleIntakeBreakfastChange.bind(this);
        this.handleIntakeLunchChange = this.handleIntakeLunchChange.bind(this);
        this.handleIntakeDinnerChange = this.handleIntakeDinnerChange.bind(this);
        this.handleIntakeSleepChange = this.handleIntakeSleepChange.bind(this);

        this.handleSubmit = this.handleSubmit.bind(this);

        this.options = toast.POSITION.BOTTOM_CENTER;
        this.cookies = this.props.cookies;

        this.openModal = this.openModal.bind(this);
    }

    componentWillReceiveProps(props) {
        console.log("...drugName=" + this.props.drugName + ", id=" + this.props.drugId);
        this.setState({ 
        	drugId: this.props.drugId, 
        	drugName: this.props.drugName, 
        	onSubmit: this.props.onSubmit});
    }
    
    setBackendError(backendError, errorMessage) {
        this.state.backendError = backendError;
        this.state.errorMessage = errorMessage;
        this.setState(this.state);
        console.log("backendError=" + backendError + ", error message = " + errorMessage);
    }

    getUserPrescriptionData() {
        this.state.loading = true;
        this.setState(this.state);
        axios.get("/drugplan/userprescription", {
                    params: {
                      drugid: this.state.drugId
                    }
                  },
                  {
                      validateStatus: (status) => {
                          console.log("status=" + status);
                          return (status >= 200 && status < 300) || status == 400 || status == 401
                      }
                  }
            ).then(({ data, status }) => {
                console.log("status=" + status);

                switch (status) {
                case 200:
                    console.log("case status 200, data = " + data);
                    if (data) {
                        this.state.periodInDays = data.periodInDays;
                        this.state.intakeBreakfastTime = data.intakeBreakfastTime;
                        this.state.intakeSleepTime = data.intakeSleepTime;
                        this.state.intakeLunchTime = data.intakeLunchTime;
                        this.state.intakeDinnerTime = data.intakeDinnerTime;
                    } else {
                        this.state.periodInDays = 1;
                        this.state.intakeBreakfastTime = false;
                        this.state.intakeSleepTime = false;
                        this.state.intakeLunchTime = false;
                        this.state.intakeDinnerTime = false;
                    }
                    this.state.loading = false;
                    this.setBackendError(false, '');
                    this.setState(this.state);
                    console.log("breakfast time = " + this.state.intakeBreakfastTime + ", periodInDays=" + this.state.periodInDays);
                    break;
                case 400:
                        this.setBackendError(true, 'backend error (400)');
                    break;
                case 401:
                        this.setBackendError(true, 'backend error (401)');
                   console.log(data, "not permitted");
                           break;
            }
        });
    }

    //saving user prescription - returns true if success
    postUserPrescription() {
        console.log("postUserPrescription");
        return axios.post('/drugplan/userprescription',
                        {
                           "drugId": this.state.drugId,
                                   "periodInDays": this.state.periodInDays,
                                   "intakeBreakfastTime": this.state.intakeBreakfastTime,
                                   "intakeSleepTime":   this.state.intakeSleepTime,
                                   "intakeLunchTime":   this.state.intakeLunchTime,
                                   "intakeDinnerTime":  this.state.intakeDinnerTime
                }, {
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
                                 console.log("postUserPrescription status 200");
                                 this.setBackendError(false, '');
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
                });
    }

    //add drug to taking list: returns true if succeeded
    addToTakingList() {
      return axios.post('/drug/taking/add', { id : this.props.drugId }, {
                validateStatus: (status) => {
                    return (status >= 200 && status < 300) || status == 400 || status == 401
                }
        })
         .then(({data, status}) => {
             const {t} = this.props;
             const options = {
                        position: toast.POSITION.BOTTOM_CENTER
             };

             console.log("addToTakingList, status = " + status);

             switch (status) {
                 case 200:
                    toast.success(t('addToTakingListSuccess'), options);
                    this.setState(this.state);
                    console.log("addToTakingList case 200");
                    return true;        //success
                     break;
                 case 400:
                    toast.error(t('addToTakingListFailed'), options);
                    break;
                 case 401:
                         toast.error(t('notPermitted'), options);
                    console.log(data, "not permitted");
                    break;
             }
             return false;
         });
    }

    componentDidMount() {
        //This should prevent operations after unmounting
        this._isMounted = true;
         console.log("did mount - breakfast time = " + this.state.intakeBreakfastTime + ", periodInDays=" + this.state.periodInDays);
    }

    componentWillUnmount() {
        this._isMounted = false;
    }

    openModal() {
        this.setState({open: true});
    }

    handlePeriodInDaysChange(event) {
        this.state.periodInDays = event.target.value;
        this.setState(this.state);
        console.log("period in days = " + this.state.periodInDays);
    }

    handleIntakeBreakfastChange(event) {
        this.state.intakeBreakfastTime = event.target.checked;
        this.setState(this.state);
        console.log("breakfast time = " + this.state.intakeBreakfastTime);
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
        event.preventDefault();
        this.state.sending = true;
        this.setState(this.state);
        console.log("add to taking list...");
        this.addToTakingList().then(() => {
            console.log("add to taking list succeeded...");
            this.postUserPrescription().then(() => {
                console.log("postUserPrescription succeeded...");
                //this.state.onSubmit();
            })
            .catch(err => {
                console.log("error postUserPrescription...${err}");
            });
        })
        .catch(err => {
            console.log("error add to taking list...${err}");
        });

        this.state.sending = false;
        this.setState(this.state);
        this.state.onSubmit();
    }

     render() {
        const {t} = this.props;

        return (
            <div className="popup-content">
                <div>
                    {/* Popup content */}
                    <h3 className="centered-title">{this.props.drugName}</h3>
                    <form onSubmit={this.handleSubmit} >

                            <fieldset>
                                    <div className="col-md-8">
                                        <label htmlFor="intakePattern">{t("intakePattern")}</label>
                                    </div>
                                    <div className="col-md-8">
                                        <select id="periodInDays" name="intakePattern" value={this.state.periodInDays}
                                                className="form-control" title="intake pattern"
                                                onChange={this.handlePeriodInDaysChange}>
                                            <option value="1">{t("daily")}</option>
                                            <option value="2">{t("everyOtherDay")}</option>
                                            <option value="7">{t("everyWeek")}</option>
                                            <option value="30">{t("everyMonth")}</option>
                                        </select>
                                    </div>
                            </fieldset>
                        <label>{t("intakeTimes")}</label>               
                                <fieldset>
                                    <div className="col-md-8">
                                <label htmlFor="intake-breakfast" className="intake-times">
                                    {t("morning")}
                                    <input type="checkbox" id="intake-breakfast"
                                           className="intakeTimes" checked={this.state.intakeBreakfastTime}
                                           onChange={this.handleIntakeBreakfastChange}/>
                                           <span className="checkmark"></span>
                                </label>
                                <label htmlFor="intake-lunch" className="intake-times">
                                    {t("noon")}
                                    <input type="checkbox" id="intake-lunch"
                                        className="intakeTimes" checked={this.state.intakeLunchTime}
                                           onChange={this.handleIntakeLunchChange}/>
                                           <span className="checkmark"></span>
                                </label>
                                    </div>
                                    <div className="col-md-8">
                                            <label htmlFor="intake-dinner" className="intake-times">
                                                {t("evening")}
                                                <input type="checkbox" value="3" id="intake-dinner"
                                                        className="intakeTimes" checked={this.state.intakeDinnerTime}
                                                       onChange={this.handleIntakeDinnerChange}/>
                                                       <span className="checkmark"></span>
                                            </label>
                                            <label htmlFor="intake-sleep" className="intake-times">
                                                {t("beforeSleep")}
                                                <input type="checkbox" value="4" id="intake-sleep"
                                                        className="intakeTimes" checked={this.state.intakeSleepTime}
                                                       onChange={this.handleIntakeSleepChange}/>
                                                       <span className="checkmark"></span>
                                            </label>
                                    </div>
                                </fieldset>
                                <fieldset>
                                <div>
                                        <button type="submit" className="btn btn-primary btn-next">{t("confirm")}</button>
                                        <button type="cancel" className="btn btn-primary btn-next">{t("cancel")}</button>
                                </div>
                                </fieldset>
                    </form>
                </div>

            </div>)
    }
}

export default withCookies(translate()(DrugIntakePopup))