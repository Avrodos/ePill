import axios from "axios";
import React from "react";
import Popup from "reactjs-popup";
import {withCookies} from "react-cookie";
import {translate} from "react-i18next";
import CheckBox from "./checkbox";
import {toast} from 'react-toastify';

class DrugIntakeSuccessPopup extends React.Component {
	
	_isMounted = false;

	constructor(props) {
        super(props);
        this.state = {
            message: "",
            onSubmit: 0
        };
        this.handleSubmit = this.handleSubmit.bind(this);
    }
        
	componentWillReceiveProps(props) {
        console.log("... componentWillReceiveProps - message=" + this.props.message);
        this.setState({ message: this.props.message,
                onSubmit: this.props.onSubmit });
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

    closeModal() {
        this.setState({open: false});
    }
    
    handleSubmit(event) {
        event.preventDefault();
        console.log("handleSubmit");
        this.state.onSubmit();
    }

	render () {
        const {t} = this.props;

        return (
        <div className="popup-content">
            <div>
                {/* Popup content */}
                {this.state.message}
                <img src={"./../../assets/images/thumbs_up.jpg"} alt="success"></img>
                
                <form onSubmit={this.handleSubmit} >
                    <fieldset>
                    <div>
                        <button type="submit" className="btn btn-primary btn-next">{t("confirm")}</button>
                    </div>
                    </fieldset>
                </form>
            </div>
        </div>)
	}
}
export default withCookies(translate()(DrugIntakeSuccessPopup));