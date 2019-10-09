package com.doccuty.epill.userdrugplan;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doccuty.epill.disease.Disease;
import com.doccuty.epill.drug.Drug;
import com.doccuty.epill.drug.DrugService;
import com.doccuty.epill.model.Interaction;
import com.doccuty.epill.tailoredtext.TailoredText;
import com.doccuty.epill.tailoredtext.TailoredTextService;
import com.doccuty.epill.user.User;
import com.doccuty.epill.user.UserService;
import com.doccuty.epill.userprescription.UserPrescription;
import com.doccuty.epill.userprescription.UserPrescriptionItem;
import com.doccuty.epill.userprescription.UserPrescriptionRepository;
import com.doccuty.epill.userdrugplan.FoodToAvoid;

/**
 * service methods used for user drug plan
 * 
 * @author cs
 *
 */
@Service
public class UserDrugPlanService {
		private static final Logger LOG = LoggerFactory.getLogger(UserDrugPlanService.class);

		@Autowired
		UserService userService;

		@Autowired
		DrugService drugService;
		
		@Autowired
		TailoredTextService tailoringService;
		
		@Autowired
		UserDrugPlanItemRepository userDrugPlanRepository;

		@Autowired
		private UserPrescriptionRepository userPrescriptionRepo;

		public List<UserDrugPlanItem> getUserDrugPlansByUserId() {

			final List<UserDrugPlanItem> userDrugPlans = userDrugPlanRepository.findByUser(userService.getCurrentUser());
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
			 List<UserDrugPlanItem> userDrugItemsPlanned = getUserDrugPlansByUserIdAndDate(dateFrom, dateTo);
			if (userDrugItemsPlanned.isEmpty() && dateFrom.after(new Date())) {
				//if plan is empty recalculate it - only for future, don't overwrite history
				this.recalculateAndSaveUserDrugPlanForDay(dateFrom);
				userDrugItemsPlanned = getUserDrugPlansByUserIdAndDate(dateFrom, dateTo);
			}
			Date lastDateTime = (Date) dateFrom.clone();
			for (int hour = currentUser.getBreakfastTime(); hour <= currentUser.getSleepTime() + 1; hour++) {
				final int hourToCompare = hour;
				final List<UserDrugPlanItem> plannedItemsForHour = userDrugItemsPlanned.stream().parallel()
						.filter(p -> DateUtils.getHours(p.getDateTimeIntake()) == hourToCompare)
						.collect(Collectors.toList());
				if (!plannedItemsForHour.isEmpty()) {
					// Collect all items in one String, take the longest halftime period
					lastDateTime = (Date) plannedItemsForHour.get(0).getDateTimeIntake();
					userDrugPlanView.add(mapUserDrugPlanToView(plannedItemsForHour, currentUser));
				} else {
					// intermediate step
					final UserDrugPlanItem userDrugPlanItemIntermediate = new UserDrugPlanItem();
					userDrugPlanItemIntermediate.setUser(currentUser);
					userDrugPlanItemIntermediate.setDateTimePlanned(DateUtils.setHoursOfDate(lastDateTime, hour)); 
					userDrugPlanItemIntermediate.setDateTimeIntake(userDrugPlanItemIntermediate.getDateTimePlanned());
					List<UserDrugPlanItem> items = new ArrayList<>();
					items.add(userDrugPlanItemIntermediate);
					
					userDrugPlanView.add(mapUserDrugPlanToView(items, currentUser));
				}
			}
			LOG.info("items={} in UserDrugPlan with intermediate steps", userDrugPlanView.size());
			return setHalftimeAndPercentagePerDrugPlanItem(userDrugPlanView);
		}
		
		
		public List<UserDrugPlanItemViewModel> getDrugPlanItemsNotTakenBetweenDates(Date dateFrom, Date dateTo) {
			final User currentUser = userService.findUserById(userService.getCurrentUser().getId());
			final List<UserDrugPlanItemViewModel> drugsNotTaken = new ArrayList<>();
			List<UserDrugPlanItem> drugsNotTakenBetweenDates = this.getDrugsNotTakenBetweenDates(dateFrom, dateTo);
			for (UserDrugPlanItem item : drugsNotTakenBetweenDates) {
				List<UserDrugPlanItem> items = new ArrayList<>();
				items.add(item);
				drugsNotTaken.add(mapUserDrugPlanToView(items, currentUser));
			}
			return drugsNotTaken;
		}
		
		/**
		 * map UserDrugPlan to UserDrugPlanItemViewModel
		 * 
		 * @param plannedItemsForHour
		 * @param user
		 * @return
		 */
		private UserDrugPlanItemViewModel mapUserDrugPlanToView(List<UserDrugPlanItem> plannedItemsForHour, User currentUser) {
			final UserDrugPlanItemViewModel model = new UserDrugPlanItemViewModel();
			final Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(plannedItemsForHour.get(0).getDateTimeIntake());
			model.setTimeString(String.format("%02d:00", calendar.get(Calendar.HOUR_OF_DAY)));
			final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
			model.setDateString(format.format(calendar.getTime()));
			
			int halfTimePeriodMax = 0;
			for (int i = 0; i < plannedItemsForHour.size(); i++) {
				if (plannedItemsForHour.get(i).getDrug() != null && halfTimePeriodMax < plannedItemsForHour.get(i).getDrug().getPeriod()) {
					halfTimePeriodMax = plannedItemsForHour.get(i).getDrug().getPeriod();
				}
			}
			
			if (plannedItemsForHour.get(0).getId() <= 0) {
				// Intermediate Step
				model.setIntermediateStep(true);
				model.setPercentage(0);
				model.setHalfTimePeriod(0);
				model.setDrugsPlannedSameTime(new ArrayList<>());
				model.setUserDrugPlanItemId(- calendar.get(Calendar.HOUR_OF_DAY));  //store negative hourOfDay
			} else {
				// intake 1 or more drugs
				model.setIntermediateStep(false);
				model.setPercentage(100);
				model.setUserDrugPlanItemId(plannedItemsForHour.get(0).getId());
				model.setHalfTimePeriod(halfTimePeriodMax);
				model.setInteraction(this.getInteractions(plannedItemsForHour));
				List<DrugViewModel> drugsSameTime = new ArrayList<>();
				for (UserDrugPlanItem item : plannedItemsForHour)
				{
					DrugViewModel drugViewModel = new DrugViewModel();
					drugViewModel.setUserDrugPlanItemId(item.getId());
					drugViewModel.setName(item.getDrug().getName());
					drugViewModel.setDrugTaken(item.getDrugTaken());
					drugViewModel.setTakeOnEmptyStomach(item.getDrug().getTakeOnEmptyStomach());
					drugViewModel.setTakeOnFullStomach(item.getDrug().getTakeOnFullStomach());
					drugViewModel.setDiseases(getDiseases(item));
					drugViewModel.setFood(getFoods(item));
					drugViewModel.setInteractions(getInteractions(item));
					TailoredText tt = this.tailoringService.getTailoredMinimumSummaryByDrugAndUser(item.getDrug(), currentUser);
					if (tt != null) {
						drugViewModel.setPersonalizedInformation(tt.getText());
					}
					
					drugViewModel.setLink(item.getDrug().getId());
					drugsSameTime.add(drugViewModel);
				}
				model.setDrugsPlannedSameTime(drugsSameTime);
			}

			return model;
		}
		
		private List<String> getInteractions(UserDrugPlanItem item) {
			List<String> interactions = new ArrayList<>();
			for (Interaction interaction : item.getDrug().getInteraction()) {
				interactions.add(interaction.getInteraction());
			}
			return interactions;
		}
		
		private String getInteractions(List<UserDrugPlanItem> item) {
			List<Drug> drugsForHour = new ArrayList<>();
			final StringBuilder interactionText = new StringBuilder();
			for (UserDrugPlanItem plannedItemForHour : item) {
				drugsForHour.add(plannedItemForHour.getDrug());
			}
			for (final Drug drug : drugsForHour) {
				for (final Interaction interaction : drug.getInteraction()) {
					for (final Drug drugCompare : drugsForHour) {
						if (interaction.getInteractionDrug().contains(drugCompare)) {
							interactionText.append("<p>" + drug.getName() + "</p> - " + drugCompare.getName() + ": "
									+ interaction.getInteraction() + "</p>");
						}
					}
				}
			}

			return interactionText.toString();
		}

		private List<String> getDiseases(UserDrugPlanItem item) {
			List<String> diseases = new ArrayList<>();
			for (Disease disease : item.getDrug().getDisease()) {
				diseases.add(disease.getName());
			}
			return diseases;
		}
		
		private List<String> getFoods(UserDrugPlanItem item) {
			List<String> foods = new ArrayList<>();
			for (FoodToAvoid food : item.getDrug().getFoodToAvoid()) {
				foods.add(food.getFoodToAvoid());
			}
			return foods;
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
//				private final String hints;
//				private final boolean hasInteractions;
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
		 * get User drug plan items between two dates (only for planned intake timestamps)
		 * 
		 * @param dateFrom
		 * @param dateTo
		 * @return
		 */
		public List<UserDrugPlanItem> getUserDrugPlansByUserIdAndDate(Date dateFrom, Date dateTo) {

			final Long userId = userService.getCurrentUser().getId();
			final List<UserDrugPlanItem> userDrugPlans = userDrugPlanRepository.findByUserBetweenDates(userId, dateFrom,
					dateTo);
			LOG.info("found items={} in UserDrugPlan", userDrugPlans.size());
			return userDrugPlans;
		}
		
		/**
		 * get User drug plan items not taken between two date times - 
		 * 
		 * @param dateFrom
		 * @param dateTo
		 * @return
		 */
		public List<UserDrugPlanItem> getDrugsNotTakenBetweenDates(Date dateFrom, Date dateTo) {

			final Long userId = userService.getCurrentUser().getId();
			final List<UserDrugPlanItem> userDrugPlans = userDrugPlanRepository.findNotTakenBetweenDates(userId, dateFrom,
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
		public List<UserDrugPlanItem> recalculateAndSaveUserDrugPlanForDay(Date day) {
			LOG.info("calculate drug plan for day {}", day);
			final User currentUser = userService.findUserById(userService.getCurrentUser().getId());
			final UserDrugPlanCalculator calculator = new UserDrugPlanCalculator(currentUser,
					drugService.findUserDrugsTaking(currentUser));
			final List<UserDrugPlanItem> plannedItemsForDay = calculator.calculatePlanForDay(day);
			logDrugPlanItems("planed drugs for day", plannedItemsForDay);
			LOG.info("plan for day calculated with {} items", plannedItemsForDay.size());
			// delete current plan (if existing) and save new plan for day
			userDrugPlanRepository.deleteByUserBetweenDates(currentUser.getId(), DateUtils.asDateStartOfDay(day),
					DateUtils.asDateEndOfDay(day));
			LOG.info("old plan deleted for day {}", day);
			LOG.info("saving plan for day for calculated {} items", plannedItemsForDay.size());
			final List<UserDrugPlanItem> savedItems = userDrugPlanRepository.save(plannedItemsForDay);
			logDrugPlanItems("saved drug plan", savedItems);
			return savedItems;
		}

		private void logDrugPlanItems(String message, List<UserDrugPlanItem> items) {
			for (final UserDrugPlanItem item : items) {
				LOG.info("{}: drug {}: {}", message, item.getDrug().getName(), item.getDateTimeIntake());
			}

		}

		/**
		 * set flag if drug is taken or not
		 * @param userDrugPlanItemId - id of UserDrugPlanItem
		 * @param isTaken - true/false
		 * @param intakeHour - hour of intake 
		 */
		public void setDrugTaken(long userDrugPlanItemId, boolean isTaken, Integer intakeHour) {
			LOG.info("set drug taken {} for userDrugPlanItemId {}, intake hour = {}", userDrugPlanItemId, isTaken, intakeHour);
			UserDrugPlanItem item = userDrugPlanRepository.getOne(userDrugPlanItemId);
			if (intakeHour != null && intakeHour > 0) {
				Date dateTimeIntake = DateUtils.setHoursOfDate(item.getDateTimeIntake(), intakeHour);
				LOG.info("datetime intake = {}", dateTimeIntake);
				item.setDateTimeIntake(dateTimeIntake);
				item.setDateTimePlanned(dateTimeIntake);  //TODO
				item.setDrugTaken(isTaken);
				userDrugPlanRepository.save(item);
			} else {
				userDrugPlanRepository.updateDrugTaken(userDrugPlanItemId, isTaken);
			}
			item = userDrugPlanRepository.getOne(userDrugPlanItemId);
			LOG.info("updated drugTaken = {} for userDrugPlanItemId {}, intakeTime = {}", userDrugPlanItemId, item.getDrugTaken(), item.getDateTimeIntake());
		}

		/**
		 * save user prescription for drug and current user
		 * @param requestParam
		 */
		public void saveUserPrescription(UserPrescriptionRequestParameter requestParam) {
			LOG.info("save user prescription for drug_id = {}, period in days={}", 
					requestParam.getDrugId(), requestParam.getPeriodInDays());
			final User currentUser = userService.findUserById(userService.getCurrentUser().getId());
			Drug drug = drugService.findDrugById(requestParam.getDrugId());
			if (drug != null) {
				List<UserPrescription> prescriptions = userPrescriptionRepo.findPrescriptions(
						userService.getCurrentUser().getId(), requestParam.getDrugId());
				deletePrescriptions(prescriptions);
				
				prescriptions = userPrescriptionRepo.findPrescriptions(
						userService.getCurrentUser().getId(), requestParam.getDrugId());
				if (prescriptions.isEmpty()) {
					UserPrescription up = new UserPrescription();
					up.setDrug(drug);
					up.setUser(currentUser);
					up.setPeriodInDays(requestParam.getPeriodInDays());
					if (requestParam.getIntakeBreakfastTime()) {
						up.getUserPrescriptionItems().add(getUserPrescription(currentUser.getBreakfastTime(), up));
					}
					if (requestParam.getIntakeLunchTime()) {
						up.getUserPrescriptionItems().add(getUserPrescription(currentUser.getLunchTime(), up));
					}
					if (requestParam.getIntakeDinnerTime()) {
						up.getUserPrescriptionItems().add(getUserPrescription(currentUser.getDinnerTime(), up));
					}
					if (requestParam.getIntakeSleepTime()) {
						up.getUserPrescriptionItems().add(getUserPrescription(currentUser.getSleepTime(), up));
					}
					UserPrescription upSaved = userPrescriptionRepo.save(up);
					LOG.info("user prescription saved: id = {}, items = {}", upSaved.getId(), upSaved.getUserPrescriptionItems().size() );
				} else {
					LOG.error("prescription not deleted");
				}
			} else {
				LOG.error("drug not found");
			}
			
		}

		private void deletePrescriptions(List<UserPrescription> prescriptions) {
			for (UserPrescription prescription : prescriptions) {
				userPrescriptionRepo.deleteUserPrescriptionItems(prescription.getId());
			}
			for (UserPrescription prescription : prescriptions) {
				userPrescriptionRepo.deletePrescription(prescription.getId());
			}
			userPrescriptionRepo.flush();
		}

		private UserPrescriptionItem getUserPrescription(final int hourOfDay, UserPrescription up) {
			UserPrescriptionItem item = new UserPrescriptionItem();
			item.setIntakeTime(hourOfDay);
			item.setUserPrescription(up);
			return item;
		}

		/**
		 * get user prescription for current user and drug
		 * 
		 * @param drugId
		 * @return
		 */
		public UserPrescriptionRequestParameter getUserPrescription(long drugId) {
			final User currentUser = userService.findUserById(userService.getCurrentUser().getId());
			List<UserPrescription> prescriptions = userPrescriptionRepo.findPrescriptions(
					userService.getCurrentUser().getId(), drugId);
			if (!prescriptions.isEmpty()) {
				UserPrescriptionRequestParameter param = new UserPrescriptionRequestParameter();
				UserPrescription firstPrescription = prescriptions.get(0);
				param.setPeriodInDays(firstPrescription.getPeriodInDays());
				for (UserPrescriptionItem item : firstPrescription.getUserPrescriptionItems()) {
					if (item.getIntakeTime() == currentUser.getBreakfastTime()) {
						param.setIntakeBreakfastTime(true);
					} else if (item.getIntakeTime() == currentUser.getLunchTime()) {
						param.setIntakeLunchTime(true);
					} else if (item.getIntakeTime() == currentUser.getDinnerTime()) {
						param.setIntakeDinnerTime(true);
					} else if (item.getIntakeTime() == currentUser.getSleepTime()) {
						param.setIntakeSleepTime(true);
					}
				}
				return param;
			} else {
				return null;
			}
		}
}