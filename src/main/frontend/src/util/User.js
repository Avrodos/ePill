import axios from "axios";
import Cookies from "universal-cookie";

class User {
    constructor() {
        this.reset();
        const cookies = new Cookies();
        const auth = cookies.get('auth');
        
        if (auth) {
            this.setCookieCredentials(auth);
        }
    }

    setCookieCredentials(credentials) {
        axios.defaults.headers.common['Authorization'] = `Bearer ${credentials.token}`;
        this.set(credentials.user);
    }

    set(data) {
        this.id = data.id;

        this.firstname = data.firstname;
        this.lastname = data.lastname;
        this.email = data.email || '';
        this.dateOfBirth = data.dateOfBirth || '',
            this.gender = data.gender || {id: 0};
        this.username = data.username;
        this.redGreenColorblind = data.redGreenColorblind || false;
        this.weight = data.weight;
        this.levelOfDetail = data.levelOfDetail || 3;
        this.preferredFontSize = data.preferredFontSize || 'defaultFontSize';
        this.tpa = data.tpa || false;
        this.firstSignIn = data.firstSignIn || false;
        this.a7id = data.a7id;
        this.gid = data.gid;
        this.smoker = data.smoker || false;
        this.diabetes = data.diabetes || {id: 0};
        this.allergy = data.allergy || [];
        this.intolerance = data.intolerance || [];
        this.condition = data.condition || [];
    }

    reset() {
        this.id = -1;

        this.firstname = undefined;
        this.lastname = undefined;
        this.email = undefined;
        this.dateOfBirth = undefined;
        this.gender = 0;
        this.username = undefined;
        this.redGreenColorblind = false;
        this.weight = -1;
        this.levelOfDetail		= 3;
        this.preferredFontSize	= 100;
        this.tpa = null;
        this.firstSignIn = null;
        this.a7id = undefined;
        this.gid = undefined;
        this.smoker = false;
        this.diabetes = 0;
        this.allergy = undefined;
        this.intolerance = undefined;
        this.condition = undefined;

    }
    
    get() {
    		return {
    	        id			:	this.id,

                firstname: this.firstname,
                lastname: this.lastname,
                email: this.email,
                dateOfBirth: this.dateOfBirth,
                gender: this.gender,
                username: this.username,
                redGreenColorblind: this.redGreenColorblind,
                weight: this.weight,
    			levelOfDetail:	this.levelOfDetail,
    			preferredFontSize	:	this.preferredFontSize,
                tpa: this.tpa,
                firstSignIn: this.firstSignIn,
                a7id: this.a7id,
                gid: this.gid,
                smoker: this.smoker,
                diabetes: this.diabetes,
                allergy: this.allergy,
                intolerance: this.intolerance,
                condition: this.condition

    		}
    }
    
    isAuthenticated() {
        return this.username && this.id != -1;
    }

    isNotAuthenticated() {
        return !this.isAuthenticated();
    }
    
    
    setLevelOfDetail(value) {
        this.levelOfDetail = value;
    }
    
    setPreferredFontSize(value) {
        this.preferredFontSize = value;
    }
    
    setRedGreenColorblind(value) {
    		this.redGreenColorblind	= value;
    }

    isTPA() {
        return this.tpa;
    }

    isFirstSignIn() {
        return this.firstSignIn;
    }
}

// Singleton pattern in ES6.
export default (new User);