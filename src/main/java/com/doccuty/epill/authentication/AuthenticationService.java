package com.doccuty.epill.authentication;

import com.doccuty.epill.user.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doccuty.epill.model.LoginAttempt;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.util.Collections;




@Service
public class AuthenticationService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    private SimpleUserRepository<SimpleUser> repository;

    @Autowired
    private LoginAttemptRepository loginAttemptRepository;

    @Autowired
    private UserService service;
    
    private final String JWTSecret = "geheim";

    /**
     * Create a JWT token and additional user information if the user's credentails are valid.
     *
     * @param username username
     * @param password password
     * @return a UserToken or null if the credentials are not valid
     */
    public UserToken login(String username, String password) {

        SimpleUser user = repository.findByUsername(username);

        if(user == null) {
        		return null;
        }

        LoginAttempt loginAttempt = new LoginAttempt();
        loginAttempt.withUserId(user.getId());
        
        String hashedPassword = hashPassword(user.getSalt(), password);

        if (!hashedPassword.equals(user.getPassword())) {
        		loginAttempt.withSuccess(false);
        		loginAttemptRepository.save(loginAttempt);
            LOG.info("User unable to login. user={}", username);
            return null;
        }
        
        String token = Jwts.builder()
                .setSubject(username)
                .setId(""+user.getId())
                .signWith(SignatureAlgorithm.HS512, JWTSecret)
                .compact();
        
        
        UserToken userToken = new UserToken();
        userToken.setUser(user);
        userToken.setToken(token);

    		loginAttempt.withSuccess(true);
    		loginAttemptRepository.save(loginAttempt);
    	
        return userToken;
    }

    /**
     * @param tpaID
     * @return
     */
    public UserToken tpaLogin(String tpaID, TpaService tpaService) {
        SimpleUser user = new User();
        if (tpaService == TpaService.GOOGLE) {
            String userID = "";
            Payload payload = null;
            //Verification of the received token
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                    // Specify the CLIENT_ID of the app that accesses the backend:
                    .setAudience(Collections.singletonList("583900150012-agjlvgr8gjsj8cv5f8fkiv3fjl9keu1j.apps.googleusercontent.com"))
                    // Or, if multiple clients access the backend:
                    //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                    .build();
            try {
                GoogleIdToken idToken = verifier.verify(tpaID);
                if (idToken != null) {
                    payload = idToken.getPayload();

                    // This is our desired ID. Unique and constant for the whole account lifetime.
                    userID = payload.getSubject();
                }

            } catch (Exception e){
                //we have an error
                //TODO: Do I need further error handling?
                return null;
            }
            if (userID.equals("") || payload == null) {
                return null;
            }

            //this means we successfully verified the user
            user = repository.findByGID(userID);
            if (user == null) {
                //no user account in the database -> we have to create one.
                String email = payload.getEmail();
                String firstName = (String) payload.get("given_name");
                String familyName = (String) payload.get("family_name");

                user = new User();
                user.setFirstname(firstName);
                user.setLastname(familyName);
                user.setEmail(email);
                user.setGid(userID);
                user.setUsername(userID); //TODO: perhaps this should be mail
                user.setPassword("thirdPartyAccountService");
                user.setTPA(true);
                User childUser = (User) user;
                if(service.saveUser(childUser) == null) {
                    return null;
                }

                return login(userID, "thirdPartyAccountService");
            } else {
                // we have to just log him into this account
                return login(user.getUsername(), "thirdPartyAccountService");
            }



        } else if (tpaService == TpaService.A7) {
            if (tpaID.equals("")) {
                return null;
            }
            //lets see whether or not he already has an account.
            user = repository.findByA7ID(tpaID);

            if(user == null) {
                user = new User();
                user.setA7id(tpaID);
                user.setUsername(tpaID);
                user.setPassword("thirdPartyAccountService");
                user.setTPA(true);
                User childUser = (User) user;
                if(service.saveUser(childUser) == null) {
                    return null;
                }
                return login(tpaID, "thirdPartyAccountService");
            } else {
                return login(user.getUsername(), "thirdPartyAccountService");
            }

        } else {
            //something went wrong with the transmission of states.
            return null;
        }
    }




    /**
     * Validate that a token is valid and returns its body.
     *
     * Throws a SignatureException if the token is not valid.
     * @param jwtToken JWT token
     * @return JWT body
     */
    public Object parseToken(String jwtToken) {
        LOG.debug("Parsing JWT token. JWTtoken={}", jwtToken);
        return Jwts.parser()
                .setSigningKey(JWTSecret)
                .parse(jwtToken)
                .getBody();
    }


    /**
     * Return (salt + password) hashed with SHA-512.
     *
     * The salt is configured in the property authenticationService.salt.
     *
     * @param password plain text password
     * @return hashed password
     */
    public String hashPassword(String salt, String password) {
        return DigestUtils.sha512Hex(salt + password);
    }


}
