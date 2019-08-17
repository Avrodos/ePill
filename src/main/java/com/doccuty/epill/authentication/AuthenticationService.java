package com.doccuty.epill.authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doccuty.epill.model.LoginAttempt;
import com.doccuty.epill.user.SimpleUser;
import com.doccuty.epill.user.SimpleUserRepository;
import com.doccuty.epill.user.UserToken;
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
     *
     * @param tpaID
     * @return
     */
    public UserToken tpaLogin(String tpaID, TpaService service) {
        SimpleUser user = new SimpleUser();
        if (service == TpaService.GOOGLE) {
            //TODO: I should probably verify the integrity of the google token around here
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                    // Specify the CLIENT_ID of the app that accesses the backend:
                    .setAudience(Collections.singletonList("583900150012-agjlvgr8gjsj8cv5f8fkiv3fjl9keu1j.apps.googleusercontent.com"))
                    // Or, if multiple clients access the backend:
                    //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                    .build();
            try {
                GoogleIdToken idToken = verifier.verify(tpaID);
                if (idToken != null) {
                    Payload payload = idToken.getPayload();

                    // Print user identifier
                    String userId = payload.getSubject();
                    System.out.println("User ID: " + userId);
                }

            } catch (Exception e){
                //we have an error
            }

                user = repository.findByGid(tpaID);
        } else {
            //just assume here its looking for a7 or other services
        }

        if(user == null) {
            //this means, we create an account before we log in.
            return null;
        }

        String username = user.getUsername();

        //now we login with the found username
        UserToken userToken = login(username, "");
        return userToken;
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
