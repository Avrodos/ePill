import React from "react";

import {translate} from "react-i18next";

import User from "../util/User";

class ProgressBar extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            percentage: this.props.percentage
        }
        console.log("progressBar, percentage=" + this.props.percentage);
    }
    componentWillReceiveProps(props) {
		  this.setState({ percentage: props.percentage });  
	}
    
    componentWillMount() {

    }

    render() {
    	var progress_percentage = "" + this.props.percentage + "%"
        return (
        	<div className="progress">
        		<div className="progress-bar" style={{ width: progress_percentage }}>{this.props.percentage}%</div>
    		</div>    	
        );
    }
}

export default translate()(ProgressBar);