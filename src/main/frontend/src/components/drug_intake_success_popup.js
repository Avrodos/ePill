import axios from "axios";
import React from "react";
import Popup from "reactjs-popup";
import {withCookies} from "react-cookie";
import {translate} from "react-i18next";

class DrugIntakeSuccessPopup extends React.Component {
	
	render () {
		return (
			<h2>Well done!</h2>
		);
	}
	
}
export default withCookies(translate()(DrugIntakeSuccessPopup));