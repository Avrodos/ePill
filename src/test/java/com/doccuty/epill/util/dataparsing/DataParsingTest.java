package com.doccuty.epill.util.dataparsing;


import com.doccuty.epill.allergy.Allergy;
import com.doccuty.epill.allergy.AllergyService;
import com.doccuty.epill.condition.Condition;
import com.doccuty.epill.condition.ConditionService;
import com.doccuty.epill.diabetes.Diabetes;
import com.doccuty.epill.diabetes.DiabetesService;
import com.doccuty.epill.gender.Gender;
import com.doccuty.epill.gender.GenderService;
import com.doccuty.epill.intolerance.Intolerance;
import com.doccuty.epill.intolerance.IntoleranceService;
import com.doccuty.epill.user.User;
import com.doccuty.epill.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import static org.junit.Assert.*;

// Use Spring's testing support in JUnit
@RunWith(SpringRunner.class)
// Enable Spring features, e.g. loading of application-properties, etc.
@SpringBootTest
public class DataParsingTest {
    private static final Logger LOG = LoggerFactory.getLogger(DataParsingTest.class);

    @Autowired
    private UserService userService;

    @Autowired
    private DataParsingService dataParsingService;

    @Autowired
    private GenderService genderService;

    @Autowired
    private AllergyService allergyService;

    @Autowired
    private IntoleranceService intoleranceService;

    @Autowired
    private ConditionService conditionService;

    @Autowired
    private DiabetesService diabetesService;

    @Before
    public void setup() {
        userService.setCurrentUser(3L, "sa@test.de");
    }

    /**
     * Test that dependency injection works.
     */
    @Test
    public void notNull() {
        assertNotNull("We should have an instance of userService", userService);
    }

    @Test
    public void testGetUserById() {

        User user = userService.getUserById(3);

        assertNotNull("No user found by id = 3", user);
    }

    /**
     * Test that all data from a .CSV file is correctly parsed into the user.
     */
    @Test
    @Transactional
    public void testDataParsing() {
        LOG.info("Testing the parsing of .CSV data into a user");
        User currentUser = userService.getCurrentUser();
        assertNotNull(currentUser);
        currentUser.setA7id("b56ceec4-3447-42e2-ae2a-75603d69cb33");
        //this test requires the to-be-parsed data to be in
        //File folder = new File("/home/fuji/Documents/Connector/Documents/andaman7/inbox");
        //the file has to be named {lastname}_{firstname}_{creationDate}
        User parsedUser = dataParsingService.importCA7UserData(currentUser);
        assertNotNull(parsedUser);
        assertEquals(parsedUser.getFirstname(), "Sajjad");
        assertEquals(parsedUser.getLastname(), "Ahmad");
        //confirm its a male
        Gender male = genderService.getGenderById(1);
        assertEquals(parsedUser.getGender(), male);

        //confirm he has two allergies
        Set<Allergy> allergies = parsedUser.getAllergy();
        assertTrue(allergies.size() == 2);

        //confirm he has one intolerance
        Set<Intolerance> intolerances = parsedUser.getIntolerance();
        assertTrue(intolerances.size() == 1);

        //confirm he has two conditions
        Set<Condition> conditions = parsedUser.getCondition();
        assertTrue(conditions.size() == 2);

        //confirm he has type 1 diabetes
        Diabetes type1 = diabetesService.getDiabetesById(1);
        assertEquals(parsedUser.getDiabetes(), type1);

        //confirm date of birth
        //an direct assertequals does not work here
        Date birthday = new GregorianCalendar(1997, Calendar.OCTOBER, 12).getTime();
        assertTrue(Math.abs(birthday.getTime() - parsedUser.getDateOfBirth().getTime()) < 1000);

    }
}
