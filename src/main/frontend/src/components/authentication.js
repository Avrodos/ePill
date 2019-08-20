import axios from "axios";
import React from "react";

import {Link} from "react-router-dom";
import {translate} from "react-i18next";
import {toast} from 'react-toastify';
import {withCookies} from "react-cookie";

import User from "../util/User";
import TpaAuthentication from "./tpaAuthentication";

class Authentication extends React.Component {
    constructor(props) {
        super(props);
        
        this.state = {
            username: '',
            password: '',
            error	: undefined,
            sending	: false
        };
        
        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleLogout = this.handleLogout.bind(this);
        this.cookies = this.props.cookies;
    }
	
	updateAuthentication() {
        this.forceUpdate();
    }
	
	
    handleUsernameChange(event) {
        this.setState({username: event.target.value});
    }

    handlePasswordChange(event) {
        this.setState({password: event.target.value});
    }

    //TODO: Prevent TPAs from logging in directly.
    handleSubmit(event) {
        event.preventDefault();

        this.state.sending = true;
        this.setState(this.state);
        
        axios.post('/auth/login', this.state, {
	            // We allow a status code of 401 (unauthorized). Otherwise it is interpreted as an error and we can't
	            // check the HTTP status code.
	            validateStatus: (status) => {
	                return (status >= 200 && status < 300) || status == 401
	            }
        		})
            .then(({data, status}) => {

                this.state.sending = false;
                this.setState(this.state);
                
                const {t} = this.props;
                
                const options = {
                	    position: toast.POSITION.BOTTOM_CENTER
                	};
                
                switch (status) {
                    case 200:
                        User.setCookieCredentials(data);
                        
                        this.setState({error: undefined});

                        // Store authentication values even after refresh.
                        this.cookies.set('auth', {
                            token: data.token,
                            user: User
                        }, {path: '/'});

                        // Send event of updated login state.
                        this.props.updateNavigation();
                        
                        // Redirect to front page.
                        this.props.history.push("/");
                        break;
                    case 401:
                    		this.setState({error: true});
                    		toast.error(t('loginFailed'), options);
                        	break;
                }
            });
    }

    handleLogout() {
        axios.defaults.headers.common['Authorization'] = undefined;
        User.reset();
        this.cookies.remove('auth');
        this.forceUpdate();
        this.props.updateNavigation();
    }


    render() {
        const {t} = this.props;
        let component = null;
        if (User.isNotAuthenticated()) {
            component =
                <form onSubmit={this.handleSubmit} className="col-xs-12 col-sm-6 col-sm-offset-3 col-md-4 col-md-offset-0 col-lg-4 col-lg-offset-0 column">
		            <div className="form-group">
		                <label htmlFor="username">{t('username')}</label>
		                <input type="text" name="username" id="username" className="form-control" value={this.state.username} onChange={this.handleUsernameChange} />
		            </div>
		            	
		            <div className="form-group">
		                <label htmlFor="password">{t('password')}</label>
		                <input type="password" name="password" id="password" className="form-control" value={this.state.password} onChange={this.handlePasswordChange} />
		            </div>
		            <div className="form-actions">
			            {!this.state.sending ?
	            				<button type="submit" className="btn btn-primary">Login</button>
	            				: <button className="btn btn-default"><img src="data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA=="></img></button> }
			            <Link to="/user/register"><button type="button" className="btn btn-default">{t('register')}</button></Link>
			        </div>
                    <TpaAuthentication {...this.props} updateNavigation={this.props.updateNavigation}  />
                </form>
        } else {
            component = <div className="container">
        						Current user: {User.username || 'not logged in'}<br />
            					<button onClick={this.handleLogout} className="btn btn-danger">Logout</button>

            </div>

        }

        return (
	        	<div className="container no-banner">
	        		<div className="page-header">
	        			<h2>{t('login')}</h2>
	        		</div>
	        		<div className="row">
	                	{component}
	                		
	                	{User.isNotAuthenticated() &&
	                		<div className="hidden-xs hidden-sm col-md-6 col-lg-4 container">
		             	
		             		<h4>Hast du noch keinen Account?</h4>
		             		<Link to="/user/register">{t('register')}</Link>
		    		        </div>
		             }
		            </div>
                </div>
        );
    }
}


export default withCookies(translate()(Authentication));