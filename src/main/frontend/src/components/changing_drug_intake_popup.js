import axios from "axios";
import React from "react";
import Popup from "reactjs-popup";
import {withCookies} from "react-cookie";
import {translate} from "react-i18next";

class ChangingDrugIntakePopup extends React.Component {
	
	render () {
		return (
				<h2>What did you take?</h2>
		);
	}
}
export default withCookies(translate()(ChangingDrugIntakePopup));