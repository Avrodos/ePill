import React from "react";
import ReactDOM from "react-dom";

import {CookiesProvider} from "react-cookie";
import {I18nextProvider} from "react-i18next";
import {HashRouter as Router, Link, Route, Switch} from "react-router-dom";
import { ToastContainer } from 'react-toastify';

import i18n from "./i18n";

import User from "./util/User";

import Navigation from "./components/navigation";
import Footer from "./components/footer";

import Home from "./components/home";
import About from "./components/about";
import Imprint from "./components/imprint";
import Privacy from "./components/privacy";
import Authentication from "./components/authentication";
import Register from "./components/register";
import DrugList from "./components/drug_list";
import DrugDetail from "./components/drug_detail";
import UserData from "./components/user/data";


class Root extends React.Component {
	constructor(props) {
        super(props);
        
        var fontSize = 'defaultFontSize';
        // Force initialization of the object.
        if(User.isAuthenticated()){
        		fontSize = User.preferredFontSize;
        }
        
        this.state = {
        		fontSize		: fontSize
        }
        
        this.updateNavigation 	= this.updateNavigation.bind(this);
        this.updateFontSize		= this.updateFontSize.bind(this);
    }

    // This is called whenever the authentication state of a user is changed by a component
    updateNavigation() {
		this.nav.updateNavigation();
    }    

    updateFontSize(fontSize) {
		this.state.fontSize = fontSize;
		this.setState(this.state);
    }

    render() {
        return (
            <div className={this.state.fontSize}>
	            <Navigation {...this.props} ref={(component) => { this.nav = component; }} />
                
	            <Switch>
	                {/* Authentication */}
	                <Route path="/user/login" render={(props) => (<Authentication {...props} updateNavigation={this.updateNavigation}/> )}/>
	                
	                <Route path="/user/register" component={Register}/>
	
	                {/* Drug handling */}
	                <Route path="/drug/list" component={DrugList}/>
	                <Route path="/drug/taking" component={DrugList}/>
	                <Route path="/drug/remember" component={DrugList} />
	                <Route path="/drug/search" component={DrugList} />
	                <Route path="/drug/:id" component={DrugDetail}/>
	
	                {/* User sites */}
	                <Route path="/user/rememberedDrugs" component={DrugList}/>
	                <Route path="/user/takenDrugs" component={DrugList}/>
	                <Route path="/user/data"  render={(props) => (<UserData {...props} updateFontSize={this.updateFontSize} updateNavigation={this.updateNavigation} /> )}/>
	                
	                {/* Information sites */}
	                <Route path="/about" component={About} />
	                <Route path="/imprint" component={Imprint} />
                    <Route path="/privacy" component={Privacy} />
	                
	                {/* Default route */}
	                <Route path="/" component={Home} />
	            </Switch>
	                
	            <ToastContainer />
	            <Footer />
            </div>
        );
    }
}

ReactDOM.render(
    <CookiesProvider>
        <I18nextProvider i18n={i18n}>
            <Router>
                <Root />
            </Router>
        </I18nextProvider>
    </CookiesProvider>
    , document.getElementById('root'));