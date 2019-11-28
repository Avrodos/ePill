import axios from "axios";
import React from "react";
import Moment from 'moment';
import {toast} from 'react-toastify';
import {translate} from "react-i18next";
import Cookies from "universal-cookie";

import User from "./../../util/User";

// See https://facebook.github.io/react/docs/forms.html for documentation about
// forms.

//TODO: Find proper solution for a7id
class UserData extends React.Component {
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
            weight: 0,
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
            gid: ''
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

        this.testDataTransfer = this.testDataTransfer.bind(this);

        this.handleAddAllergy = this.handleAddAllergy.bind(this);
        this.onEnteredAllergyChange = this.onEnteredAllergyChange.bind(this);
        this.handleAddIntolerance = this.handleAddIntolerance.bind(this);
        this.onEnteredIntoleranceChange = this.onEnteredIntoleranceChange.bind(this);
        this.handleAddCondition = this.handleAddCondition.bind(this);
        this.onEnteredConditionChange = this.onEnteredConditionChange.bind(this);
    }


    componentWillMount() {
        this.loadUser();
    }

    loadUser() {
        if (!User.isAuthenticated()) {
            return;
        }

        axios.get(`/user/${User.id}`)
            .then(({data, status}) => {

                this.state.firstname = data.firstname;
                this.state.lastname = data.lastname;
                this.state.email = data.email || '';
                this.state.dateOfBirth = data.dateOfBirth || '',
                    this.state.gender = data.gender || {id: 0};
                this.state.username = data.username;
                this.state.redGreenColorblind = data.redGreenColorblind || false;
                this.state.weight = data.weight;
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

                this.state.gid = data.gid || '';
                console.log(data);

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
	    this.state.dateOfBirth = event.target.value
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
        this.state.smoker = event.target.value;
        this.setState(this.state);
    }

    handleDiabetesChange(event) {
        this.state.diabetes = {id: event.target.value};
        this.setState(this.state);
    }

    handleAddAllergy() {
        this.state.allergy = this.state.allergy.concat(this.state.enteredAllergy);
        this.state.enteredAllergy = "";
        this.setState(this.state);
        //TODO: Save it (repo), so the repository gives me back a unique id.
    }

    onEnteredAllergyChange(event) {
        this.state.enteredAllergy = event.target.value;
        this.setState(this.state);
    }

    handleAddIntolerance() {
        this.state.intolerance = this.state.intolerance.concat(this.state.enteredIntolerance);
        this.state.enteredIntolerance = "";
        this.setState(this.state);
        //TODO: Save it (repo), so the repository gives me back a unique id.
    }

    onEnteredIntoleranceChange(event) {
        this.state.enteredIntolerance = event.target.value;
        this.setState(this.state);
    }

    handleAddCondition() {
        this.state.condition = this.state.condition.concat(this.state.enteredCondition);
        this.state.enteredCondition = "";
        this.setState(this.state);
        //TODO: Save it (repo), so the repository gives me back a unique id.
    }

    onEnteredConditionChange(event) {
        this.state.enteredCondition = event.target.value;
        this.setState(this.state);
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

    testDataTransfer(event) {
        //TODO: adjust for use, when basic user is connected to a TPS or when GAcc
        event.preventDefault();

        const {t} = this.props;
        const options = {
            position: toast.POSITION.BOTTOM_CENTER
        };

        if (this.state.a7id == '' && this.state.tpa && this.state.gid != '') {
            //it is a google acc
            toast.info(t('googleImport'), options);
        } else if (this.state.a7id != '' && !this.state.tpa) {
            //TODO: its a basic acc connected to a7
        } else if (this.state.a7id != '' && this.state.tpa) {
            this.state.sending = true;
            this.setState(this.state);

            /* This code is used to register a User to my service
            const deviceId = "21002c41-c430-4043-9511-a5c527869b2c";

            const connectorMail = "testmailforepill@gmail.com";
            const url = 'https://test-server.andaman7.com/public/v1/services/register';
            const apiKey = '0a24fa4b-9f38-47a4-ad99-5ba3a2c211c1';
            const hashedPw = sha256("thirdPartyAccountService");
            let credentials = connectorMail + ":" + hashedPw;
            let credString = 'Basic ' + window.btoa(credentials);

            axios({
                url: url,
                method: 'POST',
                headers: {
                    'api-key': apiKey,
                    'Authorization': credString,
                    'Content-Type': 'application/json',
                    'device-id': deviceId
                },
                data: {	//the body of the request
                    'firstName': this.state.firstname,
                    'lastName': this.state.lastname,
                    'email': this.state.email,
                    'language': 'EN',
                    'externalUserId': this.state.email,
                    'serviceId': 'partner.com.epill',
                    'scenarioId': 'partnerscenario.com.epill.poc'
                }
            }).then(({data}) => {
                    console.log(data);
                },
                (error) => {
                    console.log("something went wrong");
                    console.log(error);
                }
            );
             */
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

                        //TODO: messages updaten.
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
        } else if (this.state.a7id == '' && !this.state.tpa && this.state.gid != '') {
            //its a basic acc connected to Google
            toast.info(t('googleImport'), options);
        } else {
            //its a basic acc
            toast.info(t('basicAccImport'), options);
        }

        //TODO: ERROR handling
        console.log("done");
    }

    //TODO: Die gruppierung der fieldsets anpassen
    //TODO: die labels in die übersetzung übertragen
    //TODO: Entscheiden ob einmal alles eingetragen werden muss oder es optional ist (-> Diabetes)
    render() {
        const {t} 		= this.props;
        const firstname 	= this.state.firstname;
        const lastname 	= this.state.lastname;

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
                           <button onClick={this.testDataTransfer}>Import user data</button>
                       </p>
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
                                   <label htmlFor="allergy">{t('Allergy')}</label>
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
                                   <label htmlFor="intolerance">{t('Intolerance')}</label>
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
                                   <label htmlFor="condition">{t('Condition')}</label>
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

           </div>
        );
    }

}

export default translate()(UserData);