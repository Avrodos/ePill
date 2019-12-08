import React from "react";
import {sha256} from "js-sha256";
import axios from "axios";
import {toast} from "react-toastify";
import {translate} from "react-i18next";

class furtherMedicalDataPopup extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            sending: '',
            showConfirmButton: false,
            patientMail: '',
            showMailField: false
        };

        this.options = toast.POSITION.BOTTOM_CENTER;

        this.registerForService = this.registerForService.bind(this);
        this.sendRegisterToBackend = this.sendRegisterToBackend.bind(this);
        this.handlePatientMailChange = this.handlePatientMailChange.bind(this);
        this.handlePatientMailSubmit = this.handlePatientMailSubmit.bind(this);
        this.sendRegisterToMobile = this.sendRegisterToMobile.bind(this);
    }

    componentDidMount() {
        let element = document.getElementById("mailField");
        if (this.props.tpa == true) {
            element.style.display = 'none';
        } else {
            //if its not a tpa, the user has to enter his A7 mail
            element.style.display = '';
        }
    }

    handlePatientMailChange(event) {
        this.state.patientMail = event.target.value;
        this.setState(this.state);
    }

    handlePatientMailSubmit(event) {
        event.preventDefault();
        if (this.state.patientMail != "") {
            this.sendRegisterToMobile(this.state.patientMail);
            let element = document.getElementById("mailField");
            element.style.display = 'none';
        }
    }

    registerForService() {
        //its an a7 account and therefore the username is his mail
        const patientMail = this.props.username;
        this.sendRegisterToMobile(patientMail);
    }

    sendRegisterToMobile(mail) {
        const {t} = this.props;
        //This code is used to register a User to my service
        const deviceId = "21002c41-c430-4043-9511-a5c527869b2c";
        const connectorMail = "testmailforepill@gmail.com";
        const url = 'https://test-server.andaman7.com/public/v1/services/register';
        const apiKey = '0a24fa4b-9f38-47a4-ad99-5ba3a2c211c1';
        const hashedPw = sha256("thirdPartyAccountService");
        let credentials = connectorMail + ":" + hashedPw;
        let credString = 'Basic ' + window.btoa(credentials);

        //sending request to backend
        this.state.sending = true;
        this.setState(this.state);

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
                'firstName': this.props.firstName,
                'lastName': this.props.lastName,
                'email': mail,
                'language': 'EN',
                'externalUserId': mail,
                'serviceId': 'partner.com.epill',
                'scenarioId': 'partnerscenario.com.epill.poc'
            }
        }).then(({data}) => {
                this.state.sending = false;
                this.state.showConfirmButton = true;
                this.setState(this.state);
                toast.success(t('registerServiceInvitationSent'), this.options);
            },
            (error) => {
                this.state.sending = false;
                this.state.showConfirmButton = true;
                this.setState(this.state);
                console.log("something went wrong or he already received an invitation");
                console.log(error);
            }
        );
    }

    sendRegisterToBackend(event) {
        const {t} = this.props;
        //sending request to backend
        this.state.sending = true;
        this.setState(this.state);
        axios.post('/user/registerFurtherData/a7', this.state, {
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

                    toast.success(t('registerFurtherDataSuccess'), this.options);
                    this.props.handleFurtherMedicalDataSubmit();
                    break;
                case 401:
                    this.setState({error: true});
                    toast.error(t('transmissionError'), this.options);
                    break;
            }
        });
    }

    render() {
        const {t} = this.props;

        return (
            <div>
                <div>
                    {/* Popup content */}
                    <h2>{t("registerForFurtherData")}</h2>
                    <p> {t("registerServiceExplanation")}</p>
                    {}
                    {this.state.showConfirmButton ?
                        <button onClick={this.sendRegisterToBackend}> {t("confirmedConnection")} </button> :
                        <button disabled={!this.props.tpa}
                                onClick={this.registerForService}> {t("sendRequest")} </button>}
                </div>
                <fieldset id={"mailField"}>
                    <p> {t("enterA7Mail")} </p>
                    <div className="form-group col-lg-6 col-md-6">
                        <label htmlFor="email">{t('email')}</label>
                        <input type="text" name="email" id="email" className="form-control"
                               value={this.state.patientMail} onChange={this.handlePatientMailChange}/>
                    </div>
                    <button onClick={this.handlePatientMailSubmit}>{t('confirmMail')}</button>
                </fieldset>
            </div>)
    }
}

export default translate()(furtherMedicalDataPopup);