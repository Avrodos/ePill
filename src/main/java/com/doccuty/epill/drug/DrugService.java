package com.doccuty.epill.drug;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doccuty.epill.disease.Disease;
import com.doccuty.epill.iteminvocation.ItemInvocation;
import com.doccuty.epill.iteminvocation.ItemInvocationRepository;
import com.doccuty.epill.model.DrugFeature;
import com.doccuty.epill.model.Interaction;
import com.doccuty.epill.tailoredtext.TailoredText;
import com.doccuty.epill.tailoredtext.TailoredTextService;
import com.doccuty.epill.user.User;
import com.doccuty.epill.user.UserService;
import com.doccuty.epill.userdrugplan.DateUtils;
import com.doccuty.epill.userdrugplan.UserDrugPlan;
import com.doccuty.epill.userdrugplan.UserDrugPlanCalculator;
import com.doccuty.epill.userdrugplan.UserDrugPlanItemViewModel;
import com.doccuty.epill.userdrugplan.UserDrugPlanRepository;

@Service
public class DrugService {

	private static final Logger LOG = LoggerFactory.getLogger(DrugService.class);

	private static final String PROPERTY_DRUG_TAKING = "taking";

	private static final String PROPERTY_DRUG_REMEMBER = "remember";

	@Autowired
	DrugRepository repository;

	@Autowired
	UserService userService;

	@Autowired
	ItemInvocationRepository invocationRepository;

	@Autowired
	UserDrugPlanRepository userDrugPlanRepository;

	@Autowired
	DrugFeatureRepository featureRepository;

	@Autowired
	TailoredTextService tailoringService;

	public List<Drug> findAllDrugs() {
		final List<Drug> drugs = repository.findAllOrderByName();

		if (!userService.isAnonymous()) {
			final User user = userService.getUserById(userService.getCurrentUser().getId());

			if (user == null)
				return drugs;

			final List<Drug> taking = repository.findUserDrugsTaking(user.getId());
			final List<Drug> remember = repository.findUserDrugsRemembered(user.getId());

			for (Drug drug : drugs) {
				if (taking.contains(drug))
					drug.setIsTaken(true);

				if (remember.contains(drug))
					drug.setIsRemembered(true);

				drug = tailoringService.tailorDrugToUser(drug, user);

				// load tailored summary
				final TailoredText summary = tailoringService.getTailoredMinimumSummaryByDrugAndUser(drug, user);

				if (summary != null) {
					drug.setTailoredSummary(summary.getText());
				}
			}
		}

		LOG.info("Found {} drugs.", drugs.size());

		return drugs;
	}

	public Drug saveDrug(Drug drug) {

		LOG.info("Saved drug={}", drug);

		return repository.save(drug);
	}

	public Drug findDrugById(long id) {

		Drug drug = repository.findOne(id);

		if (drug != null && !userService.isAnonymous()) {

			// save invocation of the drug

			User user = userService.getCurrentUser();

			final ItemInvocation invocation = new ItemInvocation();
			invocation.withDrug(drug).withUser(user);

			user = userService.saveItemInvocation(invocation);

			// check if drug is already in remember or taking list
			for (final User usr : drug.getUserRemembering()) {
				if (usr.getId() == user.getId()) {
					drug.setIsRemembered(true);
					break;
				}
			}

			for (final User usr : drug.getUser()) {
				if (usr.getId() == user.getId()) {
					drug.setIsTaken(true);
					break;
				}
			}

			// tailor drug information to user characteristics
			drug = this.tailoringService.tailorDrugToUser(drug, user);
		}

		return drug;
	}

	public List<Drug> findDrugByName(String exp) {
		final List<Drug> list = repository.findByNameContainingIgnoreCase(exp);
		return list;
	}

	public List<Drug> getDrugMinimized(String value) {
		return repository.findByNameMinimized(value);
	}

	public List<DrugFeature> findAllDrugFeaturesSimple() {
		return featureRepository.findAllSimple();
	}

	/**
	 * check
	 * 
	 * @return
	 */

	public String checkUserDrugsInteractions(String listname) {

		final StringBuilder interactionText = new StringBuilder();

		List<Drug> list = null;

		if (listname.equals(DrugService.PROPERTY_DRUG_TAKING)) {
			list = repository.findUserDrugsTaking(userService.getCurrentUser().getId());
		} else if (listname.equals(DrugService.PROPERTY_DRUG_REMEMBER)) {
			list = repository.findUserDrugsRemembered(userService.getCurrentUser().getId());
		} else {
			list = new ArrayList<Drug>();
		}

		for (final Drug drug : list) {
			for (final Interaction interaction : drug.getInteraction()) {
				for (final Drug drugCompare : list) {
					if (interaction.getInteractionDrug().contains(drugCompare)) {
						interactionText.append("<p>" + drug.getName() + "</p> - " + drugCompare.getName() + ": "
								+ interaction.getInteraction() + "</p>");
					}
				}
			}
		}

		return interactionText.toString();
	}

	public List<Drug> findUserDrugsTaking(User user) {

		user = userService.getUserById(user.getId());

		final List<Drug> drugs = repository.findUserDrugsTaking(user.getId());
		final List<Drug> remembered = repository.findUserDrugsRemembered(user.getId());

		for (Drug drug : drugs) {
			drug.setIsTaken(true);

			if (remembered.contains(drug)) {
				drug.setIsRemembered(true);
			}

			drug = this.tailoringService.tailorDrugToUser(drug, user);
		}

		return drugs;
	}

	public List<Drug> findUserDrugsRemembered(User user) {

		user = userService.getUserById(user.getId());

		final List<Drug> taking = repository.findUserDrugsTaking(user.getId());
		final List<Drug> drugs = repository.findUserDrugsRemembered(user.getId());

		for (Drug drug : drugs) {
			drug.setIsRemembered(true);

			if (taking.contains(drug)) {
				drug.setIsTaken(true);
			}

			drug = tailoringService.tailorDrugToUser(drug, user);
		}

		return drugs;
	}

	public List<ItemInvocation> getClicksByUserId() {

		final List<ItemInvocation> list = invocationRepository.findInvocedDrugs(userService.getCurrentUser());
		LOG.info("Retreived last visited items={}", list);

		// sort by Date

		Collections.sort(list, new Comparator<ItemInvocation>() {
			@Override
			public int compare(ItemInvocation invocation1, ItemInvocation invocation2) {

				if (invocation1.getCounter() == invocation2.getCounter())
					return invocation1.getTimestamp().compareTo(invocation2.getTimestamp());

				if (invocation1.getCounter() < invocation2.getCounter())
					return 1;

				return -1;
			}
		});

		if (list.size() > 6)
			return list.subList(0, 6);

		return list;
	}

	public List<UserDrugPlan> getUserDrugPlansByUserId() {

		final List<UserDrugPlan> userDrugPlans = userDrugPlanRepository.findByUser(userService.getCurrentUser());
		LOG.info("found items={} in UserDrugPlan", userDrugPlans.size());
		return userDrugPlans;
	}

	/**
	 * get User drug plan between two dates (with intermediate rows each hour
	 * independent on planned intake of drug)
	 * 
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 */
	public List<UserDrugPlanItemViewModel> getCompleteUserDrugPlansByUserIdAndDate(Date dateFrom, Date dateTo) {
		final User currentUser = userService.findUserById(userService.getCurrentUser().getId());

		final List<UserDrugPlanItemViewModel> userDrugPlanView = new ArrayList<>();
		final List<UserDrugPlan> userDrugItemsPlanned = getUserDrugPlansByUserIdAndDate(dateFrom, dateTo);
		Date lastDateTime = (Date) dateFrom.clone();
		for (int hour = currentUser.getBreakfastTime(); hour <= currentUser.getDinnerTime() + 2; hour++) {
			final int hourToCompare = hour;
			final List<UserDrugPlan> plannedItemsForHour = userDrugItemsPlanned.stream().parallel()
					.filter(p -> DateUtils.getHours(p.getDatetimeIntakePlanned()) == hourToCompare)
					.collect(Collectors.toList());
			if (!plannedItemsForHour.isEmpty()) {
				// Collect all items in one String, take the longest halftime period
				lastDateTime = (Date) plannedItemsForHour.get(0).getDatetimeIntakePlanned().clone();
				String drugNames = plannedItemsForHour.get(0).getDrug().getName();
				String personalizedInformation = this.tailoringService.getTailoredMinimumSummaryByDrugAndUser(plannedItemsForHour.get(0).getDrug(), currentUser).getText();
				List <Disease> gettingDiseases = plannedItemsForHour.get(0).getDrug().getDisease();
				String drugDiseases = "";
				for (int j = 0; j < gettingDiseases.size(); j++) {
					drugDiseases = drugDiseases + " " + gettingDiseases.get(j).getName();
				}
				int halfTimePeriodMax = plannedItemsForHour.get(0).getDrug().getPeriod();
				if (plannedItemsForHour.size() > 1) {
					for (int i = 1; i < plannedItemsForHour.size(); i++) {
						drugNames = String.format("%s, %s", drugNames, plannedItemsForHour.get(i).getDrug().getName());
						personalizedInformation = String.format("%s, %s", personalizedInformation, this.tailoringService.getTailoredMinimumSummaryByDrugAndUser(plannedItemsForHour.get(i).getDrug(), currentUser).getText());
						List <Disease> diseasesDrugi = plannedItemsForHour.get(i).getDrug().getDisease();
						String drugjDiseases = "";
						for (int j = 0; j < diseasesDrugi.size(); j++) {
							drugjDiseases = drugjDiseases + " " + diseasesDrugi.get(j).getName();
						}
						drugDiseases = String.format("%s, %s", drugDiseases, drugjDiseases); 
						if (halfTimePeriodMax < plannedItemsForHour.get(i).getDrug().getPeriod()) {
							halfTimePeriodMax = plannedItemsForHour.get(i).getDrug().getPeriod();
						}
					}
				}
				userDrugPlanView.add(mapUserDrugPlanToView(plannedItemsForHour.get(0), drugNames, halfTimePeriodMax, personalizedInformation, drugDiseases));
			} else {
				// intermediate step
				final UserDrugPlan userDrugPlanItemIntermediate = new UserDrugPlan();
				userDrugPlanItemIntermediate.setUser(currentUser);
				userDrugPlanItemIntermediate.setDateTimePlanned(DateUtils.setHoursOfDate(lastDateTime, hour));
				userDrugPlanView.add(mapUserDrugPlanToView(userDrugPlanItemIntermediate, "", 0, "", ""));
			}
		}
		LOG.info("items={} in UserDrugPlan with intermediate steps", userDrugPlanView.size());
		return setHalftimeAndPercentagePerDrugPlanItem(userDrugPlanView);
	}

	/**
	 * map UserDrugPlan to UserDrugPlanItemViewModel
	 * 
	 * @param userDrugPlanItem
	 * @param drugNamesSameTime
	 * @param halfTimePeriodMax
	 * @param halfTimePeriodMax
	 * @return
	 */
	private UserDrugPlanItemViewModel mapUserDrugPlanToView(UserDrugPlan userDrugPlanItem, String drugNamesSameTime,
			int halfTimePeriodMax, String personalizedDrugInformation, String drugDiseases) {
		final UserDrugPlanItemViewModel model = new UserDrugPlanItemViewModel();
		final Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(userDrugPlanItem.getDatetimeIntakePlanned());
		model.setTimeString(String.format("%02d:00", calendar.get(Calendar.HOUR_OF_DAY)));
		final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		model.setDateString(format.format(calendar.getTime()));
		if (userDrugPlanItem.getId() <= 0) {
			// Intermediate Step
			model.setDrugName("");
			model.setIntake(false);
			model.setIntermediateStep(true);
			model.setTakeOnEmptyStomach(false);
			model.setTakeOnFullStomach(false);
			model.setPercentage(0);
			model.setHalfTimePeriod(0);
			model.setPersonalizedInformation("");
		} else {
			// intake 1 or more drugs
			model.setDrugName(userDrugPlanItem.getDrug().getName());
			model.setDrugNamesSameTime(drugNamesSameTime);
			model.setIntake(true);
			model.setIntermediateStep(false);
			model.setTakeOnEmptyStomach(userDrugPlanItem.getDrug().getTakeOnEmptyStomach());
			model.setTakeOnFullStomach(userDrugPlanItem.getDrug().getTakeOnFullStomach());
			model.setPercentage(100);
			model.setHalfTimePeriod(halfTimePeriodMax);
			model.setPersonalizedInformation(personalizedDrugInformation);
			model.setDrugDiseases(drugDiseases);
		}
		return model;
	}

	/**
	 * set halftime period and percentage for intermediate steps depending on drugs
	 * taken
	 * 
	 * @param completePlanWithIntermediateSteps
	 * @return
	 */
	private List<UserDrugPlanItemViewModel> setHalftimeAndPercentagePerDrugPlanItem(
			List<UserDrugPlanItemViewModel> completePlanWithIntermediateSteps) {
		final List<UserDrugPlanItemViewModel> viewModel = new ArrayList<>();
		int currentHalfTimePeriod = 0;
		int currentPercentage = 0;
		for (final UserDrugPlanItemViewModel model : completePlanWithIntermediateSteps) {
			if (model.isIntermediateStep()) {
				// Intermediate Step
				model.setPercentage(setPercentage(currentHalfTimePeriod, currentPercentage));
				currentPercentage = model.getPercentage();
				model.setHalfTimePeriod(0);
				viewModel.add(model);
			} else {
				// intake
				model.setPercentage(100);
				currentPercentage = model.getPercentage();
				currentHalfTimePeriod = model.getHalfTimePeriod();
				viewModel.add(model);
			}
		}
//			private final String hints;
//			private final boolean hasInteractions;
		return viewModel;
	}

	private int setPercentage(int halfTimePeriod, int currentPercentage) {
		if (halfTimePeriod == 0) {
			return 0;
		} else {
			if ((currentPercentage - 50 / halfTimePeriod) > 0) {
				return currentPercentage - 50 / halfTimePeriod;
			} else {
				return 0;
			}
		}
	}

	/**
	 * get User drug plan between two dates (only for planned intake timestamps)
	 * 
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 */
	public List<UserDrugPlan> getUserDrugPlansByUserIdAndDate(Date dateFrom, Date dateTo) {

		final Long userId = userService.getCurrentUser().getId();
		final List<UserDrugPlan> userDrugPlans = userDrugPlanRepository.findByUserBetweenDates(userId, dateFrom,
				dateTo);
		LOG.info("found items={} in UserDrugPlan", userDrugPlans.size());
		return userDrugPlans;
	}

	/**
	 * recalculate drug plan at day for logged in user
	 * 
	 * @param date
	 * @return
	 */
	public List<UserDrugPlan> recalculateAndSaveUserDrugPlanForDay(Date day) {
		LOG.info("calculate drug plan for day {}", day);
		final User currentUser = userService.findUserById(userService.getCurrentUser().getId());
		final UserDrugPlanCalculator calculator = new UserDrugPlanCalculator(currentUser,
				this.findUserDrugsTaking(currentUser));
		final List<UserDrugPlan> plannedItemsForDay = calculator.calculatePlanForDay(day);
		logDrugPlanItems("planed drugs for day", plannedItemsForDay);
		LOG.info("plan for day calculated with {} items", plannedItemsForDay.size());
		// delete current plan (if existing) and save new plan for day
		userDrugPlanRepository.deleteByUserBetweenDates(currentUser.getId(), DateUtils.asDateStartOfDay(day),
				DateUtils.asDateEndOfDay(day));
		LOG.info("old plan deleted for day {}", day);
		LOG.info("saving plan for day for calculated {} items", plannedItemsForDay.size());
		final List<UserDrugPlan> savedItems = userDrugPlanRepository.save(plannedItemsForDay);
		logDrugPlanItems("saved drug plan", savedItems);
		return savedItems;
	}

	private void logDrugPlanItems(String message, List<UserDrugPlan> items) {
		for (final UserDrugPlan item : items) {
			LOG.info("{}: drug {}: {}", message, item.getDrug().getName(), item.getDatetimeIntakePlanned());
		}

	}
}