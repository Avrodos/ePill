package com.doccuty.epill.drug;


import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doccuty.epill.iteminvocation.ItemInvocation;
import com.doccuty.epill.model.DrugFeature;
import com.doccuty.epill.model.util.DrugCreator;
import com.doccuty.epill.model.util.ItemInvocationCreator;
import com.doccuty.epill.user.UserService;

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
}