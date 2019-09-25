package com.doccuty.epill.drug;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.doccuty.epill.authentication.ForbiddenException;
import com.doccuty.epill.iteminvocation.ItemInvocation;
import com.doccuty.epill.model.DrugFeature;
import com.doccuty.epill.model.util.DrugCreator;
import com.doccuty.epill.model.util.ItemInvocationCreator;
import com.doccuty.epill.model.util.UserDrugPlanItemCreator;
import com.doccuty.epill.user.UserService;
import com.doccuty.epill.userdrugplan.DateUtils;
import com.doccuty.epill.userdrugplan.UserDrugPlanItem;
import com.doccuty.epill.userdrugplan.UserDrugPlanItemViewModel;

import de.uniks.networkparser.Deep;
import de.uniks.networkparser.Filter;
import de.uniks.networkparser.IdMap;
import de.uniks.networkparser.json.JsonArray;
import de.uniks.networkparser.json.JsonObject;

@RestController
@RequestMapping("/drug")
public class DrugController {

	private static final Logger LOG = LoggerFactory.getLogger(DrugController.class);

	@Autowired
	private DrugService service;

	@Autowired
	private UserService userService;

	/**
	 * get a drug by id
	 * 
	 * @param id
	 * @param lang
	 * @return
	 */

	@RequestMapping(value = { "{id}/{lang}" }, method = RequestMethod.GET)
	public ResponseEntity<JsonObject> getDrugById(@PathVariable(value = "id") long id,
			@PathVariable(value = "lang") String lang) {

		final Drug drug = service.findDrugById(id);

		// generate JSON formatted string
		final IdMap map = DrugCreator.createIdMap("");
		map.withFilter(Filter.regard(Deep.create(4)));

		final JsonObject json = map.toJsonObject(drug);

		return new ResponseEntity<>(json, HttpStatus.OK);
	}

	/**
	 * Get full list of all drugs in the system
	 * 
	 * @return
	 */

	@RequestMapping(value = "/list/all", method = RequestMethod.GET)
	public ResponseEntity<JsonObject> getAllDrugs() {

		final List<Drug> set = service.findAllDrugs();

		// generate JSON formatted string
		final IdMap map = DrugCreator.createIdMap("");
		map.withFilter(Filter.regard(Deep.create(2)));

		final JsonObject json = new JsonObject();
		final JsonArray drugArray = new JsonArray();

		for (final Drug drug : set) {
			drugArray.add(map.toJsonObject(drug));
		}

		json.add("value", drugArray);

		return new ResponseEntity<>(json, HttpStatus.OK);
	}

	/**
	 * save a new drug
	 * 
	 * @param drug
	 * @return
	 */

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<Object> addDrug(@RequestBody Drug drug) {
		// A pragmatic approach to security which does not use much
		// framework-specific magic. While other approaches
		// with annotations, etc. are possible they are much more complex while
		// this is quite easy to understand and
		// extend.
		if (userService.isAnonymous()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		service.saveDrug(drug);

		LOG.info("New drug saved drug={}", drug);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Search database for drugs matching the sent expression This is used for
	 * autocompletion
	 * 
	 * @param exp
	 * @return
	 */

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<JsonObject> searchDrug(@RequestParam("exp") String exp) {

		final List<Drug> list = service.findDrugByName(exp);

		// generate JSON formatted string

		final IdMap map = DrugCreator.createIdMap("");
		map.withFilter(Filter.regard(Deep.create(2)));

		final JsonObject json = new JsonObject();
		final JsonArray drugArray = new JsonArray();

		for (final Drug drug : list) {
			drugArray.add(map.toJsonObject(drug));
		}

		json.add("value", drugArray);

		return new ResponseEntity<>(json, HttpStatus.OK);
	}

	/**
	 * get all drug features
	 * 
	 * @return
	 */

	@RequestMapping(value = "/feature/all", method = RequestMethod.GET)
	public ResponseEntity<JsonObject> findAllDrugFeaturesSimple() {

		final List<DrugFeature> list = service.findAllDrugFeaturesSimple();

		// generate JSON formatted string

		final IdMap map = DrugCreator.createIdMap("");
		map.withFilter(Filter.regard(Deep.create(1)));

		final JsonObject json = new JsonObject();
		final JsonArray drugArray = new JsonArray();

		for (final DrugFeature feature : list) {
			drugArray.add(map.toJsonObject(feature));
		}

		json.add("value", drugArray);

		return new ResponseEntity<>(json, HttpStatus.OK);
	}

	/**
	 * check a collection of drugs for interactions
	 * 
	 * @return
	 */

	@RequestMapping(value = "/interactions/{listname}", method = RequestMethod.GET)
	public ResponseEntity<JsonObject> checkForAdverseEffects(@PathVariable(value = "listname") String listname) {
		// A pragmatic approach to security which does not use much
		// framework-specific magic. While other approaches
		// with annotations, etc. are possible they are much more complex while
		// this is quite easy to understand and
		// extend.

		if (userService.isAnonymous()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		final IdMap map = DrugCreator.createIdMap("");
		map.withFilter(Filter.regard(Deep.create(1)));

		final JsonObject json = new JsonObject();
		json.add("value", service.checkUserDrugsInteractions(listname));

		return new ResponseEntity<>(json, HttpStatus.OK);
	}

	/**
	 * retrieve drugs a user has marked as frequently taking
	 * 
	 * @return
	 */

	@RequestMapping(value = "/list/taking", method = RequestMethod.GET)
	public ResponseEntity<JsonObject> getTakenDrugByUser() {

		// A pragmatic approach to security which does not use much
		// framework-specific magic. While other approaches
		// with annotations, etc. are possible they are much more complex while
		// this is quite easy to understand and
		// extend.

		if (userService.isAnonymous()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		final List<Drug> set = service.findUserDrugsTaking(userService.getCurrentUser());

		final IdMap map = DrugCreator.createIdMap("");
		map.withFilter(Filter.regard(Deep.create(2)));

		final JsonObject json = new JsonObject();
		final JsonArray drugArray = new JsonArray();

		for (final Drug drug : set) {
			drugArray.add(map.toJsonObject(drug));
		}

		json.add("value", drugArray);

		return new ResponseEntity<>(json, HttpStatus.OK);
	}

	/**
	 * handling drugs a user is frequently taking
	 * 
	 * @param drug
	 * @return
	 */

	@RequestMapping(value = "/taking/add", method = RequestMethod.POST)
	public ResponseEntity<Object> addDrugToUserFavorites(@RequestBody Drug drug) {

		// A pragmatic approach to security which does not use much
		// framework-specific magic. While other approaches
		// with annotations, etc. are possible they are much more complex while
		// this is quite easy to understand and
		// extend.
		if (userService.isAnonymous()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		if (userService.addDrugToUserTakingList(drug)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/taking/remove", method = RequestMethod.POST)
	public ResponseEntity<Object> removeDrugToUserFavorites(@RequestBody Drug drug) {

		// A pragmatic approach to security which does not use much
		// framework-specific magic. While other approaches
		// with annotations, etc. are possible they are much more complex while
		// this is quite easy to understand and
		// extend.
		if (userService.isAnonymous()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		if (userService.removeDrugFromUserTakingList(drug)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * retrieve drugs a user has marked as frequently taking
	 * 
	 * @return
	 */

	@RequestMapping(value = "/list/remember", method = RequestMethod.GET)
	public ResponseEntity<JsonObject> getUserDrugsRemembered() {

		// A pragmatic approach to security which does not use much
		// framework-specific magic. While other approaches
		// with annotations, etc. are possible they are much more complex while
		// this is quite easy to understand and
		// extend.

		if (userService.isAnonymous()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		final List<Drug> list = service.findUserDrugsRemembered(userService.getCurrentUser());

		final IdMap map = DrugCreator.createIdMap("");
		map.withFilter(Filter.regard(Deep.create(2)));

		final JsonObject json = new JsonObject();
		final JsonArray drugArray = new JsonArray();

		for (final Drug drug : list) {
			drugArray.add(map.toJsonObject(drug));
		}

		json.add("value", drugArray);

		return new ResponseEntity<>(json, HttpStatus.OK);
	}

	/**
	 * handling drugs a user is frequently taking
	 * 
	 * @param drug
	 * @return
	 */

	@RequestMapping(value = "/remember/add", method = RequestMethod.POST)
	public ResponseEntity<Object> addDrugToRememberList(@RequestBody Drug drug) {

		// A pragmatic approach to security which does not use much
		// framework-specific magic. While other approaches
		// with annotations, etc. are possible they are much more complex while
		// this is quite easy to understand and
		// extend.
		if (userService.isAnonymous()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		if (userService.addDrugToUserRememberList(drug)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/remember/remove", method = RequestMethod.POST)
	public ResponseEntity<Object> removeDrugFromRememberList(@RequestBody Drug drug) {

		// A pragmatic approach to security which does not use much
		// framework-specific magic. While other approaches
		// with annotations, etc. are possible they are much more complex while
		// this is quite easy to understand and
		// extend.
		if (userService.isAnonymous()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		if (userService.removeDrugFromUserRememberList(drug)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * get frequently visited items by current user
	 * 
	 * @return
	 */

	@RequestMapping(value = { "/frequentlyVisited" }, method = RequestMethod.GET)
	public ResponseEntity<JsonArray> getFrequentlyVisited() {

		// A pragmatic approach to security which does not use much
		// framework-specific magic. While other approaches
		// with annotations, etc. are possible they are much more complex while
		// this is quite easy to understand and
		// extend.
		if (userService.isAnonymous()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		final List<ItemInvocation> list = service.getClicksByUserId();

		final IdMap map = ItemInvocationCreator.createIdMap("");
		map.withFilter(Filter.regard(Deep.create(2)));

		final JsonArray json = new JsonArray();

		for (final ItemInvocation invocation : list) {
			json.add(map.toJsonObject(invocation));
		}

		return new ResponseEntity<>(json, HttpStatus.OK);
	}

	/**
	 * get planned drugs for user for day
	 * 
	 * @param day
	 * @return
	 */
	@RequestMapping(value = { "/list/drugintakeplan/date" }, method = RequestMethod.GET)
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
	 * get planned drugs for user for day
	 * 
	 * @param day
	 * @return
	 */
	@RequestMapping(value = { "/list/userdrugplanned/date" }, method = RequestMethod.GET)
	public ResponseEntity<JsonObject> getUserDrugsPlannedByDay(
			@RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date day) {

		// A pragmatic approach to security which does not use much
		// framework-specific magic. While other approaches
		// with annotations, etc. are possible they are much more complex while
		// this is quite easy to understand and
		// extend.
		if (userService.isAnonymous()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		final List<UserDrugPlanItem> userDrugPlanList = service
				.getUserDrugPlansByUserIdAndDate(DateUtils.asDateStartOfDay(day), DateUtils.asDateEndOfDay(day));
		LOG.info("getUserDrugsPlanned, count of drugs={}", userDrugPlanList.size());
		final IdMap map = UserDrugPlanItemCreator.createIdMap("");
		map.withFilter(Filter.regard(Deep.create(2)));

		final JsonObject json = new JsonObject();
		final JsonArray userDrugPlanArray = new JsonArray();

		for (final UserDrugPlanItem userDrugPlan : userDrugPlanList) {
			userDrugPlanArray.add(map.toJsonObject(userDrugPlan));
		}

		json.add("value", userDrugPlanArray);

		return new ResponseEntity<>(json, HttpStatus.OK);
	}

	/**
	 * set drug taken / not taken
	 * 
	 * @param date
	 * @return
	 */
	@RequestMapping(value = "/drugintakeplan/taken", method = RequestMethod.POST)
	public ResponseEntity<Object> setDrugTaken(@RequestBody long userDrugPlanItemId, String taken) {
		// A pragmatic approach to security which does not use much
		// framework-specific magic. While other approaches
		// with annotations, etc. are possible they are much more complex while
		// this is quite easy to understand and
		// extend.
		LOG.info("set drug taken for userDrugPlanItemId= {}, taken = {}", userDrugPlanItemId, taken);
		if (userService.isAnonymous()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		service.setDrugTaken(userDrugPlanItemId, true);

		LOG.info("drug taken set");

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * recalculate drug intake plan at day for logged in user
	 * 
	 * @param date
	 * @return
	 */
	@RequestMapping(value = "/drugintakeplan/calculate/date", method = RequestMethod.POST)
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