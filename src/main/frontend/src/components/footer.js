import React from "react";

import {Link} from "react-router-dom";
import {translate} from "react-i18next";

import User from "./../util/User";


class Footer extends React.Component {
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
		  
		  return (
				  <footer className="footer">
					<div className="container">
						<div className="row bottom-rule">
							<div className="col-sm-4 footer-section">
								<Link to="/">
								 	<img src="/assets/images/logo_v.svg" className="logo" title="fratcher" alt="fratcher"></img>
								</Link>
							</div>
							<div className="col-sm-5 footer-section">
								<ul className="list-inline">
									<li className="text-uppercase">ePill:</li>
									<li><a href="http://www.aifb.kit.edu/web/Critical_Information_Infrastructures/en" target="blank">{t("team")}</a></li>
									<li><a href="http://www.aifb.kit.edu/web/Anton_Grube/en" target="blank">{t("articles")}</a></li>
									<li><a href="#/about">{t('whyRegister')}</a></li>
								</ul>
								<ul className="list-inline">
									<li className="text-uppercase">{t("help")}:</li>
									<li><a href="#/sitemap">{t("functions")}</a></li>
									<li><a href="#/privacy">{t("privacy")}</a></li>
								</ul>
							</div>
							<div className="col-sm-3">
								<address>
									<strong>Karlsruher Institut für Technologie</strong>
									<br /> KIT-Campus Süd - Kollegiengebäude am Kronenplatz (Geb. 05.20)
									<br /> Kaiserstrasse 89
									<br /> D-76133 Karlsruhe
									<br /> (+49) 7621 608-43679
									<br /><a href="mailto:anton.grube@kit.edu">{t("contactUs")}</a>
								</address>
							</div>
						</div>
						<div className="row bottom-rule">
							<div className="col-sm-12">
								<nav className="navbar navbar-default navbar-footer">
									<ul className="nav navbar-nav">
										<li><a href="#">{t("customerCare")}</a></li>
										<li><a href="#">{t("news")}</a></li>
										<li><a href="#/imprint">{t("imprint")}</a></li>
									</ul>
								</nav>
							</div>
						</div>
						<div className="row leg-room">
							<div className="col-md-12 text-center">
								<h3 className="text-uppercase">ePill</h3>
								<p className="monospaced">
									&copy;2019 ePill <span className="text-uppercase">All Rights Reserved</span>
								</p>
							</div>
						</div>
					</div>
				</footer>
		  );
	  }
}

export default translate()(Footer);