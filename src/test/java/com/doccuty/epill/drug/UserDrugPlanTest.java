package com.doccuty.epill.drug;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.doccuty.epill.user.UserService;
import com.doccuty.epill.userdrugplan.UserDrugPlanItem;
import com.doccuty.epill.userdrugplan.UserDrugPlanItemViewModel;;

// Use Spring's testing support in JUnit
@RunWith(SpringRunner.class)
// Enable Spring features, e.g. loading of application-properties, etc.
@SpringBootTest
public class UserDrugPlanTest {
	private static final Logger LOG = LoggerFactory.getLogger(UserDrugPlanTest.class);

	@Autowired
	private DrugService drugService;

	@Autowired
	private UserService userService;

	@Before
	public void setup() {
		userService.setCurrentUser(2L, "cs@test.de");
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
	@Transactional
	public void testGetAllUserDrugPlans() {
		LOG.info("testing UserDrugPlan data access");
		final List<UserDrugPlanItem> userDrugPlanList = drugService.getUserDrugPlansByUserId();
		assertNotNull(userDrugPlanList);
		assertTrue(userDrugPlanList.size() > 2);
	}

	@Test
	@Transactional
	public void testGetUserDrugPlansByDate() {
		LOG.info("testing UserDrugPlan data access by date");
		final Date dateFrom = new GregorianCalendar(2019, 9 - 1, 1).getTime();
		final Date dateTo = new GregorianCalendar(2019, 9 - 1, 2).getTime();
		final List<UserDrugPlanItem> userDrugPlanList = drugService.getUserDrugPlansByUserIdAndDate(dateFrom, dateTo);
		assertNotNull(userDrugPlanList);
		assertEquals(2, userDrugPlanList.size());
	}

	@Test
	@Transactional
	public void testGetCompleteUserDrugPlansByDate() {
		LOG.info("testing UserDrugPlan data access by date");
		final Date dateFrom = new GregorianCalendar(2019, 9 - 1, 12).getTime();
		final Date dateTo = new GregorianCalendar(2019, 9 - 1, 13).getTime();
		final List<UserDrugPlanItemViewModel> userDrugPlanList = drugService
				.getCompleteUserDrugPlansByUserIdAndDate(dateFrom, dateTo);
		assertNotNull(userDrugPlanList);

		for (final UserDrugPlanItemViewModel viewModel : userDrugPlanList) {
			System.out.println("date: " + viewModel.getDateString() + "HT: " + viewModel.getPercentage() + "% | time ="
					+ viewModel.getTimeString() + " | name = " + viewModel.getDrugName() + ", drugsSameTime="
					+ viewModel.getDrugNamesSameTime());
		}
	}
}