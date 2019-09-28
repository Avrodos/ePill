package com.doccuty.epill.drugplan;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;

import com.doccuty.epill.userdrugplan.DateUtils;

public class DateUtilsTest {

	@Test
	public void testGetEndOfDay() {
		final Date testDate = new Date();
		final Date startOfTheDay = DateUtils.asDateStartOfDay(testDate);
		assertNotNull(startOfTheDay);
		final Date endOfTheDay = DateUtils.asDateEndOfDay(testDate);
		assertNotNull(endOfTheDay);
	}
}
