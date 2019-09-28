package com.doccuty.epill.userdrugplan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.doccuty.epill.authentication.ForbiddenException;
import com.doccuty.epill.drug.DrugService;
import com.doccuty.epill.drug.DrugTakenRequestParameter;
import com.doccuty.epill.user.UserService;

/**
 * rest controller with methods to handle user drug plan
 * 
 * @author cs
 *
 */
@RestController
@RequestMapping("/drugplan")
public class UserDrugPlanController {

		private static final Logger LOG = LoggerFactory.getLogger(UserDrugPlanController.class);

		@Autowired
		private UserDrugPlanService service;

		@Autowired
		private UserService userService;
		
		/**
		 * get planned drugs for user for day
		 * 
		 * @param day
		 * @return
		 */
		@RequestMapping(value = { "/intake/date" }, method = RequestMethod.GET)
		@ResponseBody
		public List<UserDrugPlanItemViewModel> getMedicationPlanForDay(
				@RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date day) {

			// A pragmatic approach to security which does not use much
			// framework-specific magic. While other approaches
			// with annotations, etc. are possible they are much more complex while
			// this is quite easy to understand and
			// extend.
			if (userService.isAnonymous()) {
				throw new ForbiddenException();
			}

			final List<UserDrugPlanItemViewModel> userDrugPlanList = service.getCompleteUserDrugPlansByUserIdAndDate(
					DateUtils.asDateStartOfDay(day), DateUtils.asDateEndOfDay(day));
			LOG.info("getUserDrugsPlanned, count of drugs={}", userDrugPlanList.size());
			return userDrugPlanList;
		}

		/**
		 * set drug taken / not taken
		 * 
		 * @param date
		 * @return
		 */
		@RequestMapping(value = "/drug/taken", method = RequestMethod.POST)
		public ResponseEntity<Object> setDrugTaken(@RequestBody DrugTakenRequestParameter requestParam) {
			// A pragmatic approach to security which does not use much
			// framework-specific magic. While other approaches
			// with annotations, etc. are possible they are much more complex while
			// this is quite easy to understand and
			// extend.
			LOG.info("set drug taken for userDrugPlanItemId= {}, taken = {}", requestParam.getUserDrugPlanItemId(), requestParam.getDrugTaken());
			if (userService.isAnonymous()) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			service.setDrugTaken(requestParam.getUserDrugPlanItemId(), requestParam.getDrugTaken());

			LOG.info("drug taken set");

			return new ResponseEntity<>(HttpStatus.OK);
		}
		
		/**
		 * recalculate drug intake plan at day for logged in user
		 * 
		 * @param date
		 * @return
		 */
		@RequestMapping(value = "/calculate/date", method = RequestMethod.POST)
		public ResponseEntity<Object> recalculateDrugPlan(@RequestBody String dateString) {
			// A pragmatic approach to security which does not use much
			// framework-specific magic. While other approaches
			// with annotations, etc. are possible they are much more complex while
			// this is quite easy to understand and
			// extend.
			LOG.info("recalculating user drug plan for day {}", dateString);
			if (userService.isAnonymous()) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			service.recalculateAndSaveUserDrugPlanForDay(parseDateString(dateString));

			LOG.info("user drug plan recalculated");

			return new ResponseEntity<>(HttpStatus.OK);
		}

		private Date parseDateString(String jsonDate) {
			final JsonParser springParser = JsonParserFactory.getJsonParser();
			final Map<String, Object> jsonMap = springParser.parseMap(jsonDate);
			final Object obj = jsonMap.get("date");
			final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			try {
				final String dateString = (String) obj;
				final Date date = formatter.parse(dateString);
				LOG.info("converted date {}", date);
				return date;
			} catch (final ParseException e) {
				return new Date();
			}
		}

}
