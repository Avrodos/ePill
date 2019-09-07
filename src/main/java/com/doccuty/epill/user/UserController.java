package com.doccuty.epill.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.doccuty.epill.model.util.UserCreator;

import de.uniks.networkparser.Deep;
import de.uniks.networkparser.Filter;
import de.uniks.networkparser.IdMap;
import de.uniks.networkparser.json.JsonArray;
import de.uniks.networkparser.json.JsonObject;

@RestController
@RequestMapping("/user")
public class UserController {
	
    public static class UserLogin {
        public String username;
        public String password;
    }

    @Autowired
    private UserService service;

    /**
     * get user profiles of all users
     */
    
    @RequestMapping(value="/all", method = RequestMethod.GET)
    public ResponseEntity<JsonObject> getAllUsers() {
    	// A pragmatic approach to security which does not use much framework-specific magic. While other approaches
        // with annotations, etc. are possible they are much more complex while this is quite easy to understand and
        // extend.
        if (service.isAnonymous()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
    	IdMap map = UserCreator.createIdMap("");
		map.withFilter(Filter.regard(Deep.create(2)));
    	
    	List<User> set = service.getAllUsers();
    	
    	JsonObject json = new JsonObject();
    	JsonArray userArray = new JsonArray();

    	for(User user : set) {
    		userArray.add(map.toJsonObject(user));
    	}
    	
		json.add("value", userArray);
			
		return new ResponseEntity<>(json, HttpStatus.OK);
    }
    
    /**
     * get a complete user profile by id
     */
    
    @RequestMapping(value={"/{id}"}, method = RequestMethod.GET)
    public ResponseEntity<JsonObject> getUserById(@PathVariable(value = "id") long id) {
    	// A pragmatic approach to security which does not use much framework-specific magic. While other approaches
        // with annotations, etc. are possible they are much more complex while this is quite easy to understand and
        // extend.
        if (service.isAnonymous()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        User user = service.getUserById(id);
    	
    		IdMap map = UserCreator.createIdMap("");
		map.withFilter(Filter.regard(Deep.create(5)));
    	
		JsonObject json = map.toJsonObject(user);

		return new ResponseEntity<>(json, HttpStatus.OK);
    }
    
    /**
     * save complete user profile
     * @param user
     * @return
     */
    
    @RequestMapping(value="/save", method = RequestMethod.POST)
    public ResponseEntity<Object> saveUser(@RequestBody User user) {
		
    		if(service.saveUser(user) == null) {
    			return new ResponseEntity<>(HttpStatus.CONFLICT);
    		}

		return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**
     * update all parameters from settings section including
     * preferredFontSize, preferredDrugFeatures, preferredPackagingTopics,
     * levelOfDetail
     * 
     * @param user
     * @return
     */
    
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public ResponseEntity<JsonObject> updateUserData(@RequestBody User user) {
    	// A pragmatic approach to security which does not use much framework-specific magic. While other approaches
        // with annotations, etc. are possible they are much more complex while this is quite easy to understand and
        // extend.
        if (service.isAnonymous()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        service.updateUserData(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**
     * update all parameters from settings section including
     * preferredFontSize, preferredDrugFeatures, preferredPackagingTopics,
     * levelOfDetail
     * @param user
     * @return
     */
    
    @RequestMapping(value="/update/settings", method = RequestMethod.POST)
    public ResponseEntity<JsonObject> updateUserSettings(@RequestBody User user) {
    	// A pragmatic approach to security which does not use much framework-specific magic. While other approaches
        // with annotations, etc. are possible they are much more complex while this is quite easy to understand and
        // extend.
        if (service.isAnonymous()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        user = service.updateUserSettings(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * update all parameters from settings section including
     * preferredFontSize, preferredDrugFeatures, preferredPackagingTopics,
     * levelOfDetail
     * 
     * @param user
     * @return
     */
    
    @RequestMapping(value="/update/password", method = RequestMethod.POST)
    public ResponseEntity<JsonObject> updateUserPassword(@RequestBody User user) {
    	// A pragmatic approach to security which does not use much framework-specific magic. While other approaches
        // with annotations, etc. are possible they are much more complex while this is quite easy to understand and
        // extend.
        if (service.isAnonymous()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        service.updateUserPassword(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    

    /**
     * physically delete complete user profile by id
     * 
     * @param id
     * @return
     */

    @RequestMapping(value={"/delete/{id}"}, method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") long id) {
    	// A pragmatic approach to security which does not use much framework-specific magic. While other approaches
        // with annotations, etc. are possible they are much more complex while this is quite easy to understand and
        // extend.
        if (service.isAnonymous()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        service.deleteUser(id);
        
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    
    /**
     * delete current user
     * @return
     */
    
    @RequestMapping(value={"/delete"}, method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteCurrentUser() {
    	// A pragmatic approach to security which does not use much framework-specific magic. While other approaches
        // with annotations, etc. are possible they are much more complex while this is quite easy to understand and
        // extend.
        if (service.isAnonymous()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        service.deleteCurrentUser();

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/updateA7", method = RequestMethod.POST)
    public ResponseEntity<JsonObject> updateA7UserData(@RequestBody User user) {
        // A pragmatic approach to security which does not use much framework-specific magic. While other approaches
        // with annotations, etc. are possible they are much more complex while this is quite easy to understand and
        // extend.
        if (service.isAnonymous()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        service.updateA7UserData(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
