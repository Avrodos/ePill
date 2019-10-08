import axios from "axios";
import React from "react";

import {translate} from "react-i18next";
import {toast} from 'react-toastify';

import {withCookies} from "react-cookie";

import Popup from "reactjs-popup";

class ChangingDrugIntakePopup extends React.Component {
    _isMounted = false;
    _token = "";
    _levelOfDetail = 3;
    _preferredFontSize = 'defaultFontSize';

    constructor(props) {
        super(props);
        this.state = {
                drugsToBeTaken: this.props.drugsToBeTaken,
                timeString: this.props.timeString,
            sending : '',
            errorMessage: '',
            backendError: false,
            open: false
        };

        this.handleSubmit = this.handleSubmit.bind(this);

        this.options = toast.POSITION.BOTTOM_CENTER;
        this.cookies = this.props.cookies;

        this.openModal = this.openModal.bind(this);
        this.closeModal = this.closeModal.bind(this);       
    }

    componentWillReceiveProps(props) {
        console.log("...drugsToBeTaken=" + this.props.drugsToBeTaken + ", stringTime=" + this.props.stringTime);
                this.setState({ stringTime: this.props.stringTime, drugsToBeTaken: this.props.drugsToBeTaken });
                this.getUserPrescriptionData();
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

    closeModal() {
        this.setState({open: false});
    }

    handleSubmit(event) {
        //event.preventDefault();
        this.state.sending = true;
        this.setState(this.state);
        console.log("add to taking list...");
        this.addToTakingList().then(() => {
            console.log("add to taking list succeeded...");
            this.postUserPrescription().then(() => {
                console.log("postUserPrescription succeeded...");
                this.closeModal();
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
    }
    
    handleDrugCheck () {
    	
    }
    
    renderDrugCheckboxes() {
    	for (var i = 0; i<this.status.drugsToBeTaken.length; i++) {
    		return (
    			<label htmlFor="intake-lunch" className="intake-times">
                    {this.status.drugsToBeTaken[i].name}
                    <input type="checkbox" id={this.status.drugsToBeTaken[i].name}
                        className="intakeTimes"
                           onChange={this.handleDrugCheck}/>
                           <span className="checkmark"></span>
                </label>
    		);
    	}
    }

     render() {
        const {t} = this.props;

        return (
            <div className="popup-content">
                <div>
                    {/* Popup content */}
                    <h3 className="centered-title">{t("WhatDrug")} ?</h3>
                    <form onSubmit={this.handleSubmit} >               
                                <fieldset>
                                    <div className="col-md-8">
                                    {this.renderDrugCheckboxes()}
                                
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

export default withCookies(translate()(ChangingDrugIntakePopup))