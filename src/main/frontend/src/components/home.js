import React from "react";

import {translate} from "react-i18next";

import Carousel from "./carousel";
import MostVisitedItems from "./most_visited_items";

import User from "./../util/User";

// See https://facebook.github.io/react/docs/forms.html for documentation about forms.
class Home extends React.Component {
    constructor(props) {
        super(props);
        this.state = {

        };
    }

    render() {
        const {t} = this.props;

        return (
        		<div>
        		<Carousel />

        		<div className="container marketing">
        		
        			{User.isAuthenticated() &&
        				<MostVisitedItems />
        			}
        		
        			<div className="row">
        				<div className="col-lg-4">
        					<img className="img-circle" src="/assets/images/logo_s.svg" alt="Generic placeholder image" width="140" height="140"></img>
        					<h2>{t("projectName")}</h2>
        					<p>Bei dieser Webanwendung handelt es sich um einen Forschungsprototypen des KIT. Mit dieser Webseite können Sie sich Informationen, die normalerweise in Beipackzetteln
								von Medikamenten stehen, online ansehen.
								Suchen Sie einfach nach einem Medikament und lassen Sie sich die Informationen zu diesem
								anzeigen. Außerdem werden noch einige Zusatzfunktionen angeboten.</p>
        					<p>
        						<a className="btn btn-default" href="#/about" role="button">{t('viewDetails')} »</a>
        					</p>
        				</div>
        				<div className="col-lg-4">
        					<img className="img-circle" src="/assets/images/prof_sunyaev.jpg" alt="Generic placeholder image" width="140" height="140"></img>
        					<h2>{t("Team")}</h2>
        					<p>Wir erforschen zuverlässige, sichere und zweckorientierte Software- und Informationssysteme mit Bezug zu kritischen Informationsinfrastrukturen, innovativen Health-IT Anwendungen / Digital Health, Cloud und Edge Computing Diensten, Blockchain Technologien, und der Auditierung/Zertifizierung von IT.</p>
        					<p>
        						<a className="btn btn-default" href="http://www.aifb.kit.edu/web/Critical_Information_Infrastructures/en" role="button" target="blank">{t('viewDetails')} »</a>
        					</p>
        				</div>

        				<div className="col-lg-4">
        					<img className="img-circle" src="/assets/images/articles.jpg" alt="Generic placeholder image" width="140" height="140"></img>
        					<h2>{t("articles")}</h2>
        					<p>Hier finden Sie unsere aktuellen Publikationen, Projekte und weitere Informationen über die Forschungsgruppe Critical Informatiuon Infrastructures am KIT.</p>
        					<p>
        						<a className="btn btn-default" href="http://www.aifb.kit.edu/web/Critical_Information_Infrastructures/en" role="button" target="blank">{t('viewDetails')} »</a>
        					</p>
        				</div>
        			</div>
        		</div>
        	</div>     
        );
    }
}

export default translate()(Home);