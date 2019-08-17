import axios from "axios";
import React from "react";

import {Link} from "react-router-dom";

import {translate} from "react-i18next";

// See https://facebook.github.io/react/docs/forms.html for documentation about forms.
class Privacy extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
        		
        };

    }

	// for html conversion
	createMarkup(text) { return {__html: text}; };
	    
    render() {
        const {t} = this.props;

        return (
        	<div className="container marketing no-banner">
        		<div className='page-header'>
        	    		<div className='btn-toolbar pull-right'>
        	            <div className='btn-group'></div>
                        </div>
                    <h3>{t("privacy")}</h3>
                </div>

        	    
        		<div className="col-md-12 no-padding" dangerouslySetInnerHTML={this.createMarkup(t('privacyPolicy'))} />

        	</div>
        );
    }
}

export default translate()(Privacy);
