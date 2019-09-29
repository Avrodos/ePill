package com.doccuty.epill.drugplan;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


//Use Spring's testing support in JUnit
@RunWith(SpringRunner.class)
//Enable Spring features, e.g. loading of application-properties, etc.
@SpringBootTest
public class RequestParameterTest {

	@Test
	public void testParseJsonStringToRequestParameter() {
		String json = "{\"UserPrescriptionRequestParameter\":{\"drugId\":1,\"periodInDays\":\"1\",\"intakeBreakfastTime\":true,\"intakeSleepTime\":false,\"intakeLunchTime\":false,\"intakeDinnerTime\":true}}";
		
		final JsonParser springParser = JsonParserFactory.getJsonParser();
		final Map<String, Object> jsonMap = springParser.parseMap(json);
		assertNotNull(jsonMap);
				
	}
}
