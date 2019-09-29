package com.doccuty.epill.drugplan;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.doccuty.epill.drug.Drug;
import com.doccuty.epill.drug.DrugService;
import com.doccuty.epill.user.User;
import com.doccuty.epill.user.UserService;
import com.doccuty.epill.userdrugplan.UserDrugPlanService;
import com.doccuty.epill.userdrugplan.UserPrescriptionRequestParameter;
import com.doccuty.epill.userprescription.UserPrescription;
import com.doccuty.epill.userprescription.UserPrescriptionItem;
import com.doccuty.epill.userprescription.UserPrescriptionRepository;

//Use Spring's testing support in JUnit
@RunWith(SpringRunner.class)
//Enable Spring features, e.g. loading of application-properties, etc.
@SpringBootTest
public class UserPrescriptionRepositoryTest {

	@Autowired
	private UserPrescriptionRepository userPrescriptionRepo;

	@Autowired
	private UserService userService;
	
	@Autowired
	private DrugService drugService;
	
	@Autowired
	private UserDrugPlanService userDrugPlanService;
	
	@Before
	public void setup() {
		userService.setCurrentUser(2L, "cs@test.de");
	}
	
	/**
	 * Test that dependency injection works.
	 */
	@Test
	public void notNull() {
		assertNotNull("We should have an instance of userService", userService);
	}
	
	@Test
	@Transactional
	public void saveUserPrescription() {
		
		final User currentUser = userService.findUserById(userService.getCurrentUser().getId());
		List<Drug> drugs = drugService.findUserDrugsTaking(currentUser);
		Drug firstDrug = drugs.get(0);
		List<UserPrescription> prescriptions = userPrescriptionRepo.findPrescriptions(
				userService.getCurrentUser().getId(), firstDrug.getId());
		assertTrue(!prescriptions.isEmpty());
		for (UserPrescription prescription : prescriptions) {
			userPrescriptionRepo.deleteUserPrescriptionItems(prescription.getId());
		}
		for (UserPrescription prescription : prescriptions) {
			userPrescriptionRepo.deletePrescription(prescription.getId());
		}
		userPrescriptionRepo.flush();
		
		UserPrescription up = new UserPrescription();
		up.setDrug(firstDrug);
		up.setUser(currentUser);
		up.setPeriodInDays(2);
		
		UserPrescriptionItem item1 = new UserPrescriptionItem();
		item1.setIntakeTime(7);
		item1.setUserPrescription(up);
		UserPrescriptionItem item2 = new UserPrescriptionItem();
		item2.setIntakeTime(18);
		item2.setUserPrescription(up);
		up.getUserPrescriptionItems().add(item2);
		
		UserPrescription upSaved = userPrescriptionRepo.save(up);
		assertNotNull(upSaved);
		
		prescriptions = userPrescriptionRepo.findPrescriptions(
				userService.getCurrentUser().getId(), firstDrug.getId());
		assertTrue(!prescriptions.isEmpty());
	}
	
	@Test
	public void getUserPrescription() {
		UserPrescriptionRequestParameter param = userDrugPlanService.getUserPrescription(1);
		assertNotNull(param);
	}
}
