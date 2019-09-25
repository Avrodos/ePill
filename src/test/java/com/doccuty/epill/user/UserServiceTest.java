package com.doccuty.epill.user;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.doccuty.epill.drug.Drug;
import com.doccuty.epill.drug.DrugService;
import com.doccuty.epill.user.UserService;

import javax.transaction.Transactional;
import static org.junit.Assert.*;

// Use Spring's testing support in JUnit
@RunWith(SpringRunner.class)
// Enable Spring features, e.g. loading of application-properties, etc.
@SpringBootTest
public class UserServiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    private DrugService drugService;

    @Autowired
    private UserService userService;

    @Before
    public void setup() {
        userService.setCurrentUser(1L, "Niclas");
    }

    /**
     * Test that dependency injection works.
     */
    @Test
    public void notNull() {
        assertNotNull("We should have an instance of drugService", drugService);
        assertNotNull("We should have an instance of userService", userService);
    }

    @Test
    public void testGetUserById() {
	    	
	    User user = userService.getUserById(1);
	
	    	assertNotNull("No user found by id = 1", user);	
	}

    @Test
    public void testGetUserByUsername() {
	    	
	    	SimpleUser user = userService.findByUsername("nic");
	
	    	assertNotNull("No user found by username", user);

	    	assertEquals("User has not id = 1", 1, user.getId());
	
	}

    /**
     * Test that adding a new user leads to an id (and the post is thus persisted).
     */
    @Test
    @Transactional
    public void testUserSave() {
	    	
	    	String username = "peterle";
	    	
	    	User user = new User();
	    	user.withFirstname("Peter")
	    		.withLastname("Mustermann")
	    		.withUsername("peterle")
	    		.withPassword("password")
	    		.withEmail("test@test.de");
	
	    	assertEquals("User has no id", user.getId(), 0);
	    	
	    	user = userService.saveUser(user);
	    	
	    	assertNotNull("User has id", user.getId());
	
	    	user = userService.getUserById(user.getId());
	
	    	assertNotNull("User found by id", user);
	
	    	SimpleUser userSimple = userService.findByUsername(username);
	
	    	assertNotNull("User found by username", userSimple);
	    	
	}
    
    /**
     * Test that updating a user works.
     */
    @Test
    @Transactional
    public void testUserUpdate() {

    	User user = userService.findUserById(userService.getCurrentUser().getId());
    	user.setSleepTime(23);
    	
    	User userSaved = userService.updateUserData(user);
    	assertNotNull(userSaved);
    	assertEquals(23, userSaved.getSleepTime());
    	
    	User userSaved2 = userService.findUserById(userService.getCurrentUser().getId());
    	assertNotNull(userSaved2);
    	assertEquals(23, userSaved2.getSleepTime());
	}
}
