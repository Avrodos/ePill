import React from "react";

import {Link} from "react-router-dom";
import {translate} from "react-i18next";

import User from "./../util/User";

class UserMenue extends React.Component {
	  constructor(props) {
	    super(props);
	    this.status = {
	    		show		: false
	    }

        this.toggleShow = this.toggleShow.bind(this);
	  }

	  toggleShow(event) {
		  this.status.show = !this.status.show;
		  this.setState(this.status);
	  }

	  render() {
	      const {t} = this.props;
	      const show = this.status.show;
	      
		  let menue = null;
		  if(User.isAuthenticated()) {
				menue = <ul className="nav navbar-nav navbar-right" onClick={this.toggleShow}>
							<li className="dropdown open">
							<a className="dropdown-toggle">{User.firstname} {User.lastname}</a>
								{show &&
									<ul className="dropdown-menu">
										<li><Link to="/drug/taking">{t('userDrugs')}</Link></li>
										<li><Link to="/drug/remember">{t('rememberedDrugs')}</Link></li>
										<li><Link to="/drugintakeplan">{t('drugIntakePlan')}</Link></li>
										<li><Link to="/user/data">{t('userData')}</Link></li>
										<li><Link to="/user/login">{t('logout')}</Link></li>
									</ul> }
							</li>
					</ul>;
				
		  } else {
			  menue = <ul className="nav navbar-nav navbar-right" onClick={this.toggleShow}>
						<li className="dropdown open">
						<a className="dropdown-toggle">{t('login')}/{t('register')}</a>
						{show &&
							<ul className="dropdown-menu">
								<li><Link to="/user/login">{t('login')}</Link></li>
								<li><Link to="/user/register">{t('register')}</Link></li>
							</ul> }
						</li>
					</ul>;
		  }
		
	    return (
	    		menue
	    );
	  }
	}

export default translate()(UserMenue);