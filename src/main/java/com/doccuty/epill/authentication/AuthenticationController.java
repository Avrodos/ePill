package com.doccuty.epill.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.doccuty.epill.user.UserToken;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	
    public static class UserLogin {
        public String username;
        public String password;
    }

    public static class TpaLogin {
        public String tpaId;
        public TpaService service;
    }

    @Autowired
    private AuthenticationService authenticationService;
    
    /*
     * authentication of a user using JWT token 
     */

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity<UserToken> login(@RequestBody UserLogin userLogin) {
        UserToken token = authenticationService.login(userLogin.username, userLogin.password);

        if(token == null) {
        		return new ResponseEntity<>(token, HttpStatus.UNAUTHORIZED);
        }
        
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @RequestMapping(value = "googleLogin", method = RequestMethod.POST)
    public ResponseEntity<UserToken> tpalogin(@RequestBody TpaLogin userLogin) {
        userLogin.service = TpaService.GOOGLE;
        UserToken token = authenticationService.tpaLogin(userLogin.tpaId, userLogin.service);

        if(token == null) {
            return new ResponseEntity<>(token, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
