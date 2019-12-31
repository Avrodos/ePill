import axios from "axios";
import React from "react";
import Moment from 'moment';
import {toast} from 'react-toastify';
import {translate} from "react-i18next";
import Cookies from "universal-cookie";

import User from "./../../util/User";
import Popup from "reactjs-popup";
import NewPasswordPopup from "./../newPasswordPopup";
import ConnectA7Popup from "../connectA7Popup"
import MergeConflictPopup from "../mergeConflictPopup";
import FurtherMedicalDataPopup from "../furtherMedicalDataPopup";

// See https://facebook.github.io/react/docs/forms.html for documentation about
// forms.

class UserData extends React.Component {
    _isMounted = false;

    constructor(props) {
        super(props);
        
        this.state = {
            firstname: '',
            lastname: '',
            email: '',
            dateOfBirth: '',
            gender: {id: 0},
            username: '',
            redGreenColorblind: false,
            weight: '',
            levelOfDetail       : 3,
            preferredFontSize   : 'defaultFontSize',
			sending: false,
			tpa: false,
            firstSignIn: false,
            a7id: '',
            smoker: false,
            diabetes: {id: 0},
            allergy: [],
            enteredAllergy: '',
            intolerance: [],
            enteredIntolerance: '',
            condition: [],
            enteredCondition: '',
            gid: '',
            open: false,
            overwriteOnImport: true,
            mcopen: false,
            a7ropen: false,
            alreadyRegisteredForFurtherData: false,
            registeredForFurtherDataOpen: false
        };
        
        this.handleFirstnameChange		= this.handleFirstnameChange.bind(this);
        this.handleLastnameChange		= this.handleLastnameChange.bind(this);
        this.handleDateOfBirthChange     = this.handleDateOfBirthChange.bind(this);
        
        this.handleGenderChange			= this.handleGenderChange.bind(this);
        this.handleRedGreenColorblind    = this.handleRedGreenColorblind.bind(this);
		this.handleWeightChange		= this.handleWeightChange.bind(this);
        
        this.handleEmailChange			= this.handleEmailChange.bind(this);

        this.handleChangeLevelOfDetail		= this.handleChangeLevelOfDetail.bind(this);
        this.handleChangePreferredFontSize	= this.handleChangePreferredFontSize.bind(this);

        this.handleSmokerChange = this.handleSmokerChange.bind(this);
        this.handleDiabetesChange = this.handleDiabetesChange.bind(this);
        this.loadUser = this.loadUser.bind(this);

        this.handleSubmit				= this.handleSubmit.bind(this);


        this.cookies = this.props.cookies;

        this.importUserData = this.importUserData.bind(this);

        this.handleAddAllergy = this.handleAddAllergy.bind(this);
        this.onEnteredAllergyChange = this.onEnteredAllergyChange.bind(this);
        this.handleAddIntolerance = this.handleAddIntolerance.bind(this);
        this.onEnteredIntoleranceChange = this.onEnteredIntoleranceChange.bind(this);
        this.handleAddCondition = this.handleAddCondition.bind(this);
        this.onEnteredConditionChange = this.onEnteredConditionChange.bind(this);

        this.transformAccount = this.transformAccount.bind(this);
        this.openModal = this.openModal.bind(this);
        this.closeModal = this.closeModal.bind(this);
        this.handleKeepChange = this.handleKeepChange.bind(this);
        this.handleMCSubmit = this.handleMCSubmit.bind(this);
        this.openA7ConnectPopup = this.openA7ConnectPopup.bind(this);
        this.importA7Data = this.importA7Data.bind(this);
        this.importGoogleData = this.importGoogleData.bind(this);
        this.closeMCModal = this.closeMCModal.bind(this);
        this.openMCModal = this.openMCModal.bind(this);
        this.closeRegisterForFurtherMedicalDataModal = this.closeRegisterForFurtherMedicalDataModal.bind(this);
        this.openRegisterForFurtherMedicalDataModal = this.openRegisterForFurtherMedicalDataModal.bind(this);
        this.handleFurtherMedicalDataSubmit = this.handleFurtherMedicalDataSubmit.bind(this);
    }


    componentWillMount() {
        this.loadUser();
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

    openA7ConnectPopup() {
        this.state.a7ropen = true;
        this.setState(this.state);
    }

    closeModal() {
        this.state.open = false;
        this.state.mcopen = false;
        this.state.a7ropen = false;
        this.setState(this.state);
        this.loadUser(); //TODO: geht es auch "kleiner"?
    }

    openMCModal() {
        this.state.mcopen = true;
        this.setState(this.state);
    }

    closeMCModal() {
        this.state.mcopen = false;
        this.setState(this.state);
    }

    closeRegisterForFurtherMedicalDataModal() {
        this.state.registeredForFurtherDataOpen = false;
        this.setState(this.state);
    }

    openRegisterForFurtherMedicalDataModal() {
        this.state.registeredForFurtherDataOpen = true;
        this.setState(this.state);
    }

    loadUser() {
        if (!User.isAuthenticated()) {
            return;
        }

        axios.get(`/user/${User.id}`)
            .then(({data, status}) => {

                this.state.firstname = data.firstname || '';
                this.state.lastname = data.lastname || '';
                this.state.email = data.email || '';
                this.state.dateOfBirth = data.dateOfBirth || '',
                    this.state.gender = data.gender || {id: 0};
                this.state.username = data.username || '';
                this.state.redGreenColorblind = data.redGreenColorblind || false;
                this.state.weight = data.weight || '';
                this.state.levelOfDetail = data.levelOfDetail || 3;
                this.state.preferredFontSize = data.preferredFontSize || 'defaultFontSize';
                this.state.tpa = data.tpa || false;
                this.state.firstSignIn = data.firstSignIn || false;

                this.state.a7id = data.a7id || '';
                this.state.smoker = data.smoker || false;
                this.state.diabetes = data.diabetes || {id: 0};
                this.state.allergy = data.allergy || [];
                this.state.intolerance = data.intolerance || [];
                this.state.condition = data.condition || [];
                this.state.alreadyRegisteredForFurtherData = data.registeredForFurtherData || false;

                this.state.gid = data.gid || '';

                this.setState(this.state);

            });
    }
    
    handleFirstnameChange(event) {
	    this.state.firstname	= event.target.value;
	    	this.setState(this.state);
    }

    handleLastnameChange(event) {
	    this.state.lastname = event.target.value;
	    	this.setState(this.state);
    }
    
    handleGenderChange(event) {
	    this.state.gender = {id : event.target.value };
	    this.setState(this.state);
    }
    
    handleDateOfBirthChange(event) {
        this.state.dateOfBirth = event.target.value;
	    	this.setState(this.state);
    }
    
    handleUsernameChange(event) {
	    this.state.username = event.target.value;
	    	this.setState(this.state);
    }
    
    handleRedGreenColorblind(event) {
        this.state.redGreenColorblind = (event.target.value == 1) ? true : false;
        this.setState(this.state);
        
        User.setRedGreenColorblind(this.state.redGreenColorblind);
    }

	handleWeightChange(event) {
		this.state.weight	= event.target.value;
		this.setState(this.state);
	}

    handleChangeLevelOfDetail(event) {
	    this.state.levelOfDetail = event.target.value;
	    	this.setState(this.state);
	    	
	    User.setLevelOfDetail(this.state.levelOfDetail);
    }
    
    handleChangePreferredFontSize(event) {
	    this.state.preferredFontSize = event.target.value;
	    	this.setState(this.state);

		User.setPreferredFontSize(this.state.preferredFontSize);
		this.props.updateFontSize(this.state.preferredFontSize);
    }
    
    handleEmailChange(event) {
	    this.state.email = event.target.value;
    		this.setState(this.state);
    }

    handleSmokerChange(event) {
        this.state.smoker = (event.target.value == 1) ? true : false;
        this.setState(this.state);
    }

    handleDiabetesChange(event) {
        this.state.diabetes = {id: event.target.value};
        this.setState(this.state);
    }

    handleAddAllergy(event) {
        event.preventDefault();
        const {t} = this.props;
        const options = {
            position: toast.POSITION.BOTTOM_CENTER
        };

        this.state.sending = true;
        this.setState(this.state);
        axios.post('/allergy/save', this.state.enteredAllergy, {
            // We allow a status code of 401 (unauthorized). Otherwise it is interpreted as an error and we can't
            // check the HTTP status code.
            validateStatus: (status) => {
                return (status >= 200 && status < 300) || status == 401
            }
        }).then(({data, status}) => {
            this.state.sending = false;
            this.setState(this.state);

            switch (status) {
                case 200:
                    //const tempAll = {id: data.id, name: data.name, };
                    //this.state.allergy.push(tempAll);
                    this.state.enteredAllergy = "";
                    this.setState(this.state);
                    this.loadUser();
                    toast.success(t('addSuccessful'), options);

                    break;
                case 400:
                    toast.error(t('addFailed'), options);
                    break;
                case 401:
                    console.log(data, "not permitted");
                    break;
            }
        });
    }

    onEnteredAllergyChange(event) {
        this.state.enteredAllergy = event.target.value;
        this.setState(this.state);
    }

    handleAddIntolerance(event) {
        event.preventDefault();
        const {t} = this.props;
        const options = {
            position: toast.POSITION.BOTTOM_CENTER
        };

        this.state.sending = true;
        this.setState(this.state);
        axios.post('/intolerance/save', this.state.enteredIntolerance, {
            // We allow a status code of 401 (unauthorized). Otherwise it is interpreted as an error and we can't
            // check the HTTP status code.
            validateStatus: (status) => {
                return (status >= 200 && status < 300) || status == 401
            }
        }).then(({data, status}) => {
            this.state.sending = false;
            this.setState(this.state);

            switch (status) {
                case 200:
                    this.state.enteredIntolerance = "";
                    this.setState(this.state);
                    this.loadUser();
                    toast.success(t('addSuccessful'), options);

                    break;
                case 400:
                    toast.error(t('addFailed'), options);
                    break;
                case 401:
                    console.log(data, "not permitted");
                    break;
            }
        });
    }

    onEnteredIntoleranceChange(event) {
        this.state.enteredIntolerance = event.target.value;
        this.setState(this.state);
    }

    handleAddCondition(event) {
        event.preventDefault();
        const {t} = this.props;
        const options = {
            position: toast.POSITION.BOTTOM_CENTER
        };

        this.state.sending = true;
        this.setState(this.state);
        axios.post('/condition/save', this.state.enteredCondition, {
            // We allow a status code of 401 (unauthorized). Otherwise it is interpreted as an error and we can't
            // check the HTTP status code.
            validateStatus: (status) => {
                return (status >= 200 && status < 300) || status == 401
            }
        }).then(({data, status}) => {
            this.state.sending = false;
            this.setState(this.state);

            switch (status) {
                case 200:
                    this.state.enteredCondition = "";
                    this.setState(this.state);
                    this.loadUser();
                    toast.success(t('addSuccessful'), options);

                    break;
                case 400:
                    toast.error(t('addFailed'), options);
                    break;
                case 401:
                    console.log(data, "not permitted");
                    break;
            }
        });
    }

    onEnteredConditionChange(event) {
        this.state.enteredCondition = event.target.value;
        this.setState(this.state);
    }

    handleKeepChange(event) {
        this.state.overwriteOnImport = (event.target.value == 1) ? true : false;
        this.setState(this.state);
    }

    handleMCSubmit(event) {
        event.preventDefault();
        this.closeMCModal();
        if (this.state.a7id != '') {
            this.importA7Data();
        } else {
            //must be connected to google
            this.importGoogleData();
        }

    }

    handleFurtherMedicalDataSubmit(event) {
        //event.preventDefault();
        this.closeRegisterForFurtherMedicalDataModal();
        //this.state.registeredForFurtherDataOpen = false;
        //this.setState(this.state);
        if ((this.state.a7id != '' || this.state.gid != '') && !this.state.tpa) {
            this.openMCModal();
        } else {
            this.importA7Data();
        }
    }
    
    handleSubmit(event) {
        event.preventDefault();
        
        if(this.state.sending)
        		return;

		const {t} = this.props;
	    const options = {
	    	    position: toast.POSITION.BOTTOM_CENTER
	    };
        
        
        var date = null;
        
        if(this.state.dateOfBirth != '') {
            date = Moment(this.state.dateOfBirth);
            
            if(!date.isValid()) {
        	        	if(Moment(this.state.dateOfBirth, "DD.MM.YYYY").isValid()) {
        	    			date = Moment(this.state.dateOfBirth, "DD.MM.YYYY");
        	    		} else {
        	            toast.error(t('invalidDateFormat'), options);
        	    			return;
        	    		}
            }
            
            date = date.format("YYYY-MM-DD");
        }
        
        this.state.sending = true;
        this.setState(this.state);

        axios.post('/user/update',
               {
	           		firstname			: this.state.firstname,
	           		lastname				: this.state.lastname,
                   dateOfBirth: date,
                   gender: this.state.gender,
                   email: this.state.email,
                   redGreenColorblind: this.state.redGreenColorblind,
                   levelOfDetail: this.state.levelOfDetail,
                   preferredFontSize: this.state.preferredFontSize,
                   weight: this.state.weight,
                   smoker: this.state.smoker,
                   diabetes: this.state.diabetes
                })
                .then(({data, status}) => {
                     this.state.sending = false;
                     this.setState(this.state);
                     
                     const {t} = this.props;
                     const options = {
                     	    position: toast.POSITION.BOTTOM_CENTER
                     };
                     
                     switch (status) {
                         case 200:

                             var data = this.state;
                             data.id = User.id;
                             
                             User.set(data);
                             
                             const cookies = new Cookies();
                             const auth = cookies.get('auth');

                             auth["user"] = data;
                             cookies.set('auth', auth);
                             
                             toast.success(t('savingSuccessfull'), options);
                             
                             break;
                         case 400:
                          	toast.error(t('savingFailed'), options);
                             break;
                         case 401:
                         	console.log(data, "not permitted");
                            	break;
                     }
                });
    }

    importUserData(event) {
        event.preventDefault();

        const {t} = this.props;
        const options = {
            position: toast.POSITION.BOTTOM_CENTER
        };

        if (this.state.a7id == '' && this.state.tpa && this.state.gid != '') {
            //it is a google acc
            //toast.info(t('googleImport'), options);
            this.importGoogleData();

        } else if (this.state.a7id != '' && !this.state.tpa) {
            //it is a basic acc connected to a7
            if (!this.state.alreadyRegisteredForFurtherData) {
                this.openRegisterForFurtherMedicalDataModal();
            } else {
                this.openMCModal();
            }
        } else if (this.state.a7id != '' && this.state.tpa) {
            //this is a a7 tpa account
            this.state.sending = true;
            this.setState(this.state);
            if (!this.state.alreadyRegisteredForFurtherData) {
                this.openRegisterForFurtherMedicalDataModal();
            } else {
                this.importA7Data();
            }

        } else if (this.state.a7id == '' && !this.state.tpa && this.state.gid != '') {
            //its a basic acc connected to Google
            this.state.mcopen = true;
            this.setState(this.state);
        } else {
            //its a basic acc
            toast.info(t('basicAccImport'), options);
        }
    }

    importGoogleData() {
        const {t} = this.props;
        const options = {
            position: toast.POSITION.BOTTOM_CENTER
        };

        this.state.sending = true;
        this.setState(this.state);
        axios.post('/user/update/google', this.state, {
            // We allow a status code of 401 (unauthorized). Otherwise it is interpreted as an error and we can't
            // check the HTTP status code.
            validateStatus: (status) => {
                return (status >= 200 && status < 300) || status == 401
            }
        }).then(({data, status}) => {
            this.state.sending = false;
            this.setState(this.state);

            switch (status) {
                case 200:
                    toast.success(t('importSuccessful'), options);
                    break;
                case 400:
                    toast.error(t('importFailed'), options);
                    break;
                case 401:
                    console.log(data, "not permitted");
                    break;
            }
        });
    }

    importA7Data() {
        const {t} = this.props;
        const options = {
            position: toast.POSITION.BOTTOM_CENTER
        };

        axios.post('/data/import', this.state, {
            // We allow a status code of 401 (unauthorized). Otherwise it is interpreted as an error and we can't
            // check the HTTP status code.
            validateStatus: (status) => {
                return (status >= 200 && status < 300) || status == 401
            }
        }).then(({data, status}) => {
            this.state.sending = false;
            this.setState(this.state);

            switch (status) {
                case 200:

                    var data = this.state;
                    const cookies = new Cookies();
                    const auth = cookies.get('auth');

                    auth["user"] = data;
                    cookies.set('auth', auth);

                    this.loadUser();

                    toast.success(t('importSuccessful'), options);

                    break;
                case 400:
                    toast.error(t('importFailed'), options);
                    break;
                case 401:
                    console.log(data, "not permitted");
                    break;
            }
        });
    }

    sendTransformAccountToBackend() {
        axios.post('/user/transform', this.state, {
            // We allow a status code of 401 (unauthorized). Otherwise it is interpreted as an error and we can't
            // check the HTTP status code.
            validateStatus: (status) => {
                return (status >= 200 && status < 300) || status == 401
            }
        }).then(({data, status}) => {
            this.state.sending = false;
            this.setState(this.state);

            const {t} = this.props;
            const options = {
                position: toast.POSITION.BOTTOM_CENTER
            };

            switch (status) {
                case 200:
                    break;
                case 400:
                    toast.error(t('transformFailed'), options);
                    break;
                case 401:
                    console.log(data, "not permitted");
                    break;
            }
        });
    }

    transformAccount(event) {
        event.preventDefault();
        this.state.tpa = false;
        this.state.sending = true;
        this.setState(this.state);
        //he has to choose a new password
        if (this._isMounted) {
            this.openModal();
        }
        this.sendTransformAccountToBackend();
    }

    render() {
        const {t} 		= this.props;
        const firstname 	= this.state.firstname;
        const lastname 	= this.state.lastname;

        let choosePasswordPopup =
            <div>
                <Popup open={this.state.open}>
                    <NewPasswordPopup
                        id={this.state.id}
                        username={this.state.username}
                        closeModal={this.closeModal}
                    />
                </Popup>
            </div>;

        let a7RegisterPopup =
            <div>
                <Popup
                    open={this.state.a7ropen}
                    position="right center"
                    modal>
                    <ConnectA7Popup
                        closeModal={this.closeModal}
                        firstName={this.state.firstname}
                        lastName={this.state.lastname}
                    />
                </Popup>
            </div>;

        let mergeConflictPopup =
            <div>
                <Popup
                    open={this.state.mcopen}>
                    <MergeConflictPopup
                        overwriteOnImport={this.state.overwriteOnImport}
                        keepChange={this.handleKeepChange}
                        handleMCSubmit={this.handleMCSubmit}
                    />
                </Popup>
            </div>;

        let furtherMedicalDataPopup =
            <div>
                <Popup
                    open={this.state.registeredForFurtherDataOpen}>
                    <FurtherMedicalDataPopup
                        handleFurtherMedicalDataSubmit={this.handleFurtherMedicalDataSubmit}
                        tpa={this.state.tpa}
                        username={this.state.username}
                        firstName={this.state.firstname}
                        lastName={this.state.lastname}
                    />
                </Popup>
            </div>;

        return (
	       <div className="container marketing no-banner">
	        	<div className='page-header'>
	        	      <h3>{t("userData")}</h3>
	        	</div>
	     	
	        	{User.levelOfDetail > 1 &&
			    <div className="alert alert-info">
                    <span className="glyphicon glyphicon-info-sign" aria-hidden="true"></span>
                    <span className="sr-only">Info:</span>&nbsp;
	        			{t("userCockpitDescr").replace("%User.firstname%", firstname).replace("%User.lastname%", lastname)}
				</div>
                }
	        	   <form onSubmit={this.handleSubmit} className="row">
                       <p>
                           <button disabled={this.state.a7id == '' && this.state.gid == ''}
                                   onClick={this.importUserData}>Import user data
                           </button>
                       </p>
                       <p>
                           <button disabled={!this.state.tpa} onClick={this.transformAccount}> Transform account to a
                               connected basic account
                           </button>
                       </p>
                       <p>
                           <button disabled={this.state.a7id != '' || this.state.gid != ''}
                                   onClick={this.openA7ConnectPopup}> Connect to Andaman7
                           </button>
                       </p>
                       {choosePasswordPopup}
                       {mergeConflictPopup}
                       {furtherMedicalDataPopup}
                       <fieldset>
						   <fieldset disabled={this.state.tpa}>
				            <div className="form-group col-md-6 col-lg-6">
				               <label htmlFor="firstname">{t('firstname')}</label>
				               <input type="text" name="firstname" id="firstname" className="form-control" value={this.state.firstname} onChange={this.handleFirstnameChange} />
				            </div> 
				            <div className="form-group col-md-6 col-lg-6">
				               <label htmlFor="lastname">{t('lastname')}</label>
								<input type="text" name="lastname" id="lastname" className="form-control"
									   value={this.state.lastname} onChange={this.handleLastnameChange}/>
				            </div>
                            <div className="form-group col-md-6 col-lg-6">
                               <label htmlFor="gender">{t('gender')}</label>
                               <select id="gender" value="0" name="gender" className="form-control" title={t('gender')} value={this.state.gender.id} onChange={this.handleGenderChange}>
                                     <option value="0" disabled>{t('noInfo')}</option>
                                     <option value="2">{t('female')}</option>
                                     <option value="1">{t('male')}</option>
                                </select>
                           </div>
                           <div className="form-group col-md-6 col-lg-6">
                             <label htmlFor="dateOfBirth">{t('dateOfBirth')}</label>
                             <input type="text" name="dateOfBirth" id="dateOfBirth" className="form-control" value={this.state.dateOfBirth} onChange={this.handleDateOfBirthChange} />
                          </div> 
					</fieldset>

                           <fieldset disabled={this.state.tpa}>
					      <div className="form-group col-lg-6 col-md-6">
					         <label htmlFor="email">{t('email')}</label>
					         <input type="text" name="email" id="email" className="form-control" value={this.state.email} onChange={this.handleEmailChange} />
					      </div> 
					    </fieldset>
                           <fieldset disabled={this.state.tpa}>
                               <div className="form-group col-lg-6 col-md-6">
                                   <label htmlFor="weight">{t('weight')}</label>
                                   <input type="text" name="weight" id="weight" className="form-control" value={this.state.weight} onChange={this.handleWeightChange} />
                               </div>
                           </fieldset>
                           <fieldset disabled={this.state.tpa}>
                          <div className="form-group col-lg-6 col-md-6">
                            <p><b>{t("redGreenColorblind")}</b></p>
                            <ul className="list-inline">
                                <li className="col-lg-4 col-md-4 col-xs-4 list-group-item">
                                    <label htmlFor="red-green-colorblind-yes" className="radio-inline">
                                        <input type="radio" value="1" id="red-green-colorblind-yes" name="redGreenColorblind" checked={this.state.redGreenColorblind == true} onChange={this.handleRedGreenColorblind} /> 
                                         {t('yes')}
                                     </label>
                                </li>
                                <li className="col-lg-4 col-md-4 col-xs-4 list-group-item">
                                    <label htmlFor="red-green-colorblind-no" className="radio-inline">
                                        <input type="radio" value="0" id="red-green-colorblind-no" name="redGreenColorblind" checked={this.state.redGreenColorblind == false} onChange={this.handleRedGreenColorblind} /> 
                                        {t('no')}
                                    </label>
                                </li>
                            </ul>
                        </div>
                               <div className="form-group col-lg-6 col-md-6">
                                   <p><b>{t("smoker")}</b></p>
                                   <ul className="list-inline">
                                       <li className="col-lg-4 col-md-4 col-xs-4 list-group-item">
                                           <label htmlFor="smoker-yes" className="radio-inline">
                                               <input type="radio" value="1" id="smoker-yes" name="smoker"
                                                      checked={this.state.smoker == true}
                                                      onChange={this.handleSmokerChange}/>
                                               {t('yes')}
                                           </label>
                                       </li>
                                       <li className="col-lg-4 col-md-4 col-xs-4 list-group-item">
                                           <label htmlFor="smoker-no" className="radio-inline">
                                               <input type="radio" value="0" id="smoker-no" name="smoker"
                                                      checked={this.state.smoker == false}
                                                      onChange={this.handleSmokerChange}/>
                                               {t('no')}
                                           </label>
                                       </li>
                                   </ul>
                               </div>
                               <div className="form-group col-md-6 col-lg-6">
                                   <label htmlFor="diabetes">{t('Diabetes')}</label>
                                   <select id="diabetes" value="0" name="diabetes" className="form-control"
                                           title={t('diabetes')} value={this.state.diabetes.id}
                                           onChange={this.handleDiabetesChange}>
                                       <option value="0" disabled>{t('noInfo')}</option>
                                       <option value="1">{t('type1')}</option>
                                       <option value="2">{t('type2')}</option>
                                       <option value="3">{t('none')}</option>
                                   </select>
                               </div>

                           </fieldset>
                           <fieldset disabled={this.state.tpa}>
                               <div className="form-group col-md-6 col-lg-6">
                                   <label htmlFor="allergy">{t('Allergies')}</label>
                                   <ul>
                                       {this.state.allergy.map(item => (
                                           <li key={item.id}>{item.name}</li>
                                       ))}
                                   </ul>
                                   <input
                                       type="text"
                                       value={this.state.enteredAllergy}
                                       onChange={this.onEnteredAllergyChange}
                                   />
                                   <button
                                       type="button"
                                       onClick={this.handleAddAllergy}
                                       disabled={!this.state.enteredAllergy}
                                   >
                                       Add Allergy
                                   </button>
                               </div>
                               <div className="form-group col-md-6 col-lg-6">
                                   <label htmlFor="intolerance">{t('Intolerances')}</label>
                                   <ul>
                                       {this.state.intolerance.map(item => (
                                           <li key={item.id}>{item.name}</li>
                                       ))}
                                   </ul>
                                   <input
                                       type="text"
                                       value={this.state.enteredIntolerance}
                                       onChange={this.onEnteredIntoleranceChange}
                                   />
                                   <button
                                       type="button"
                                       onClick={this.handleAddIntolerance}
                                       disabled={!this.state.enteredIntolerance}
                                   >
                                       Add Intolerance
                                   </button>
                               </div>
                               <div className="form-group col-md-6 col-lg-6">
                                   <label htmlFor="condition">{t('Conditions')}</label>
                                   <ul>
                                       {this.state.condition.map(item => (
                                           <li key={item.id}>{item.name}</li>
                                       ))}
                                   </ul>
                                   <input
                                       type="text"
                                       value={this.state.enteredCondition}
                                       onChange={this.onEnteredConditionChange}
                                   />
                                   <button
                                       type="button"
                                       onClick={this.handleAddCondition}
                                       disabled={!this.state.enteredCondition}
                                   >
                                       Add Condition
                                   </button>
                               </div>
                           </fieldset>
					<fieldset>
					    <div className="form-group col-lg-9 col-md-9">
        						<p><b>{t("helpenabled")}</b></p>
        						<ul className="list-inline degree-of-detail-list">
        							<li className="col-lg-4 col-md-4 col-xs-4 list-group-item">
        								<label htmlFor="settings-detail-max" className="radio-inline">
        									<input type="radio" value="3" id="settings-detail-max" name="levelOfDetail" checked={this.state.levelOfDetail == 3} onChange={this.handleChangeLevelOfDetail} />
        									{t('yes')}
        								</label>
        							</li>
        							<li className="col-lg-4 col-md-4 col-xs-4 list-group-item">
        								<label htmlFor="settings-detail-min" className="radio-inline">
        									<input type="radio" value="1" id="settings-detail-min" name="levelOfDetail" checked={this.state.levelOfDetail == 1} onChange={this.handleChangeLevelOfDetail} />
        									{t('no')}
        								</label>
        							</li>
        						</ul>
						</div>
					</fieldset>

                           <fieldset>
                        <div className="form-group col-lg-9 col-md-9">
            					<p><b>{t("preferredFontSize")}</b></p>
            					<ul className="list-inline font-size-list">
            						<li className="col-lg-4 col-md-4 col-xs-4 list-group-item">
            							<label htmlFor="settings-preferred-font-size-min" className="radio-inline">
            								<input type="radio" value="minFontSize" id="settings-preferred-font-size-min" name="preferredFontSize" checked={this.state.preferredFontSize == 'minFontSize'} onChange={this.handleChangePreferredFontSize} />
            								<span className="small-text">AAA</span>
            							</label>
            						</li>
            						<li className="col-lg-4 col-md-4 col-xs-4 list-group-item">
            							<label htmlFor="settings-preferred-font-size-default" className="radio-inline">
            								<input type="radio" value="defaultFontSize" id="settings-preferred-font-size-default" name="preferredFontSize" checked={this.state.preferredFontSize == 'defaultFontSize'} onChange={this.handleChangePreferredFontSize} />
            								<span className="medium-text">AAA</span>
            							</label>
            						</li>
            						<li className="col-lg-4 col-md-4 col-xs-4 list-group-item">
            							<label htmlFor="settings-preferred-font-size-max" className="radio-inline">
            								<input type="radio" value="maxFontSize" id="settings-preferred-font-size-max" name="preferredFontSize" checked={this.state.preferredFontSize == 'maxFontSize'} onChange={this.handleChangePreferredFontSize} />
            								<span className="big-text">AAA</span>
            							</label>
            						</li>
            					</ul>
        					</div>
				</fieldset>
					   </fieldset>
						<div className="form-actions container">
			            {!this.state.sending ?
	            				<button type="submit" className="btn btn-primary">{t('save')}</button>
	            				: <button className="btn btn-default"><img src="data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA=="></img></button> }


                        </div>
	        	    </form>

               {a7RegisterPopup}

           </div>
        );
    }

}

export default translate()(UserData);