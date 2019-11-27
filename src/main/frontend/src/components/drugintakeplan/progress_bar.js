import React from "react";

import {translate} from "react-i18next";

import User from "../../util/User";

class ProgressBar extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            percentage: this.props.percentage
        }
    }
    componentWillReceiveProps(props) {
		  this.setState({ percentage: props.percentage });  
	}
    
    componentWillMount() {

    }

    render() {
    	var progress_percentage = "" + this.props.percentage + "%"
    	if (this.props.percentage == 100) {
    		return (
    			<div className="progress">
            		<div className="progress-bar-dark" style={{ width: progress_percentage }}>{this.props.percentage}%</div>
        		</div> 
    		)
    	} else if ((this.props.percentage < 100) && (this.props.percentage > 50)) {
    		return (
        			<div className="progress">
                		<div className="progress-bar-lighter" style={{ width: progress_percentage }}>{this.props.percentage}%</div>
            		</div> 
        	)
    	} else if (this.props.percentage <= 50) {
    		return (
        			<div className="progress">
                		<div className="progress-bar-very-light" style={{ width: progress_percentage }}>{this.props.percentage}%</div>
            		</div> 
        	)
    	}
        
    }
}

export default translate()(ProgressBar);