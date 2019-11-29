package com.doccuty.epill.userdrugplan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.doccuty.epill.drug.Drug;
import com.doccuty.epill.model.Interaction;
import com.doccuty.epill.user.User;
import com.doccuty.epill.userprescription.UserPrescription;
import com.doccuty.epill.userprescription.UserPrescriptionItem;

public class UserDrugPlanCalculator {
        private static final Logger LOG = LoggerFactory.getLogger(UserDrugPlanCalculator.class);

        @Autowired
        UserDrugPlanItemRepository userDrugPlanRepository;

        private final User user;
        private final List<Drug> userDrugsTaking;

        public UserDrugPlanCalculator(User user, List<Drug> userDrugsTaking) {
                this.user = user;
                this.userDrugsTaking = userDrugsTaking;
        }

        public List<UserDrugPlanItem> calculatePlanForDay(Date day) {
                LOG.info("calculate plan for day {}", day);
                List<UserDrugPlanItem> userDrugPlanForDay = new ArrayList<>();
                LOG.info("breakfast={}, lunch={}, dinner={}", this.user.getBreakfastTime(), this.user.getLunchTime(),
                                this.user.getDinnerTime());
                // TODO: emptyStomach,
                for (final Drug drug : userDrugsTaking) {
                        LOG.info("drug taking {}, ", drug.getName());
                        userDrugPlanForDay.addAll(createDefaultUserDrugPlanItems(drug, user, day));
                }

                userDrugPlanForDay = getSortedUserDrugPlanByDatetimeIntake(userDrugPlanForDay);

                // userDrugPlanForDay = adjustUserDrugPlanByInteractions(userDrugPlanForDay);

                return userDrugPlanForDay;
        }

        public List<UserDrugPlanItem> adjustPlanForDay(List<UserDrugPlanItem> drugsPlannedForRestOfDay,
                        UserDrugPlanItem item) {
                LOG.info("adjustPlanForDay for item with user={}, drug={}", item.getUser().getUsername(),
                                item.getDrug().getName());
                return adjustUserDrugPlanByInteractions(drugsPlannedForRestOfDay, item);
        }

        private List<UserDrugPlanItem> adjustUserDrugPlanByInteractions(List<UserDrugPlanItem> drugsPlannedForRestOfDay,
                        UserDrugPlanItem item) {
                List<UserDrugPlanItem> potentialMovingItems = getFirstItemsAfterIntakeChange(drugsPlannedForRestOfDay, item);

                for (int i = 0; i < potentialMovingItems.size(); i++) {
                        LOG.info("check for potential interaction with {} at {}", potentialMovingItems.get(i).getDrug().getName(),
                                        potentialMovingItems.get(i).getDateTimePlanned());
                        int dateTimeDifference = DateUtils.getHours(potentialMovingItems.get(i).getDateTimePlanned())
                                        - DateUtils.getHours(item.getDateTimeIntake());
                        if (checkInteraction(potentialMovingItems.get(i), item)
                                        || potentialMovingItems.get(i).getDrug().equals(item.getDrug())) {
                                LOG.info("interaction between {} and {} found", item.getDrug().getName(),
                                                potentialMovingItems.get(i).getDrug().getName());
                                LOG.info("dateTimeDifference = {}, period = {}", dateTimeDifference, item.getDrug().getPeriod());
                                if (dateTimeDifference < item.getDrug().getPeriod()) {
                                        LOG.info("dateTimeDifference < period - check if adjustments are allowed");
                                        if (checkAdjustmentAllowed(potentialMovingItems.get(i))) {
                                                LOG.info("adjustments allowed - adjusting dateTimePlanned, adding {} hours",
                                                                dateTimeDifference);
                                                adjustDateTimePlanned(potentialMovingItems.get(i), item.getDrug().getPeriod(),
                                                                dateTimeDifference);
                                        }
                                }
                        }
                }
                return getSortedUserDrugPlanByDatetimeIntake(drugsPlannedForRestOfDay);
        }

        private List<UserDrugPlanItem> getFirstItemsAfterIntakeChange(List<UserDrugPlanItem> drugsPlannedForRestOfDay,
                        UserDrugPlanItem item) {
                LOG.info("getFirstItemsAfterIntakeChange for item with user={}, drug={}", item.getUser().getUsername(),
                                item.getDrug().getName());
                List<UserDrugPlanItem> firstIntakes = new ArrayList<>();
                int itemHalfPeriod = item.getDrug().getPeriod();
                for (int j = 1; j <= itemHalfPeriod; j++) {
                        for (int i = 0; i < drugsPlannedForRestOfDay.size(); i++) {
                                Date dateTimePlannedNextItem = drugsPlannedForRestOfDay.get(i).getDateTimePlanned();
                                Date addHoursToItemDate = DateUtils.addHoursToDate(item.getDateTimePlanned(), j);
                                LOG.info("dateTimePlannedNextItem={}, addHoursToItemDate={}", dateTimePlannedNextItem,
                                                addHoursToItemDate);
                                int hourPlannedNextItem = DateUtils.getHours(dateTimePlannedNextItem);
                                int nextHourInPlan = DateUtils.getHours(addHoursToItemDate);
                                LOG.info("nextHourInPlan={}, hourPlannedNextItem={}", nextHourInPlan, hourPlannedNextItem);
                                if (nextHourInPlan == hourPlannedNextItem) {
                                        LOG.info("getFirstItemsAfterIntakeChange -> item found at {}: drugname={}",
                                                        drugsPlannedForRestOfDay.get(i).getDateTimePlanned().getHours(),
                                                        drugsPlannedForRestOfDay.get(i).getDrug().getName());
                                        firstIntakes.add(drugsPlannedForRestOfDay.get(i));
                                }
                        }
                }
                return firstIntakes;
        }

        private boolean checkAdjustmentAllowed(UserDrugPlanItem userDrugPlan) {
                if (userDrugPlan.getDrug().getTakeOnEmptyStomach() || userDrugPlan.getDrug().getTakeOnFullStomach()
                                || userDrugPlan.getDrug().getTakeToMeals()) {
                        return false;
                } else {
                        return true;
                }
        }

        /**
         * add n hours ...
         *
         * @param userDrugPlanItemToAdjust
         */
        private void adjustDateTimePlanned(UserDrugPlanItem userDrugPlanItemToAdjust, int halfTimePeriod,
                        int dateTimeDifference) {
                LOG.info("adjust intake time for {}, current intake time = {}, add hours",
                                userDrugPlanItemToAdjust.getDrug().getName(), userDrugPlanItemToAdjust.getDateTimePlanned());
                int hourChange = halfTimePeriod - dateTimeDifference;
                if (DateUtils.addHoursToDate(userDrugPlanItemToAdjust.getDateTimePlanned(), hourChange).compareTo(
                                DateUtils.setHoursOfDate(userDrugPlanItemToAdjust.getDateTimePlanned(), user.getSleepTime())) <= 0) {
                        userDrugPlanItemToAdjust.setDateTimePlanned(
                                        DateUtils.addHoursToDate(userDrugPlanItemToAdjust.getDateTimePlanned(), hourChange));
                        userDrugPlanItemToAdjust.setDateTimeIntake(userDrugPlanItemToAdjust.getDateTimePlanned());
                        LOG.info("intake time for {} adjusted: current intake time = {}",
                                        userDrugPlanItemToAdjust.getDrug().getName(), userDrugPlanItemToAdjust.getDateTimePlanned());
                }
        }

        /**
         * check interactions between two takings
         *
         * @param userDrugPlanItem1 - planned intake 1 with drug 1
         * @param userDrugPlanItem2 - planned intake 2 with drug 2
         * @return
         */
        private boolean checkInteraction(UserDrugPlanItem userDrugPlanItem, UserDrugPlanItem userDrugPlanItem2) {
                for (final Interaction interaction : userDrugPlanItem.getDrug().getInteraction()) {
                        if (interaction.getInteractionDrug().contains(userDrugPlanItem2.getDrug())) {
                                LOG.info("interaction between {} and {]", userDrugPlanItem.getDrug().getName(),
                                                userDrugPlanItem2.getDrug().getName());
                                return true;
                        }
                }
                for (final Interaction interaction : userDrugPlanItem2.getDrug().getInteraction()) {
                        if (interaction.getInteractionDrug().contains(userDrugPlanItem.getDrug())) {
                                LOG.info("interaction between {} and {]", userDrugPlanItem2.getDrug().getName(),
                                                userDrugPlanItem.getDrug().getName());
                                return true;
                        }
                }
                return false;
        }

        public Date addHoursToJavaUtilDate(Date date, int hours) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.HOUR_OF_DAY, hours);
                return calendar.getTime();
        }

//        private List<UserDrugPlanItem> createDefaultUserDrugPlanItemsOld(Drug drug, User user, Date day) {
//                final List<UserDrugPlanItem> userDrugPlanForDay = new ArrayList<>();
//                LOG.info("create default plan for day={} user={} drug={}", day, user.getUsername(), drug.getName());
//                if (drug.getCountPerDay() == 1) {
//                        userDrugPlanForDay.add(createDefaultUserDrugPlanItem(drug, user, day, this.user.getBreakfastTime()));
//                } else if (drug.getCountPerDay() == 2) {
//                        userDrugPlanForDay.add(createDefaultUserDrugPlanItem(drug, user, day, this.user.getBreakfastTime()));
//                        userDrugPlanForDay.add(createDefaultUserDrugPlanItem(drug, user, day, this.user.getDinnerTime()));
//                } else if (drug.getCountPerDay() == 3) {
//                        userDrugPlanForDay.add(createDefaultUserDrugPlanItem(drug, user, day, this.user.getBreakfastTime()));
//                        userDrugPlanForDay.add(createDefaultUserDrugPlanItem(drug, user, day, this.user.getLunchTime()));
//                        userDrugPlanForDay.add(createDefaultUserDrugPlanItem(drug, user, day, this.user.getDinnerTime()));
//                } else if (drug.getCountPerDay() > 3) {
//                        LOG.warn("not supported for more than 3 intake times per day");
//                }
//                LOG.info("created {} items day={} user={} drug={}", userDrugPlanForDay.size(), day, user.getUsername(),
//                                drug.getName());
//                return userDrugPlanForDay;
//        }
//        

    	private List<UserDrugPlanItem> createDefaultUserDrugPlanItems(Drug drug, User user, Date day) {
            final List<UserDrugPlanItem> userDrugPlanForDay = new ArrayList<>();
            LOG.info("create default plan for day={} user={} drug={}", day, user.getUsername(), drug.getName());
            for (UserPrescription userPrescription : drug.getUserPrescriptions()) {
            	for (UserPrescriptionItem item : userPrescription.getUserPrescriptionItems()) {
            		if (item.getDrug().getUser().getId() == user.getId()) {
            			userDrugPlanForDay.add(createDefaultUserDrugPlanItem(drug, user, day, item.getIntakeTime()));
            		}
            	}
            }
            LOG.info("created {} items day={} user={} drug={}", userDrugPlanForDay.size(), day, user.getUsername(),
                            drug.getName());
            return userDrugPlanForDay;
    	}

        private UserDrugPlanItem createDefaultUserDrugPlanItem(Drug drug, User user, Date day, int hourOfDay) {
                final UserDrugPlanItem item = new UserDrugPlanItem();
                item.setDateTimePlanned(getDate(day, hourOfDay));
                item.setDateTimeIntake(item.getDateTimePlanned());
                LOG.info("planned time {} for drug {} ", item.getDateTimePlanned(), drug.getName());
                item.setUser(user);
                item.setDrug(drug);
                return item;
        }

        private Date getDate(Date day, int hourOfDay) {
                final Calendar cal = Calendar.getInstance();
                cal.setTime(day);
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, 0);
                return cal.getTime();
        }

        private List<UserDrugPlanItem> getSortedUserDrugPlanByDatetimeIntake(List<UserDrugPlanItem> userDrugPlanForDay) {
                userDrugPlanForDay.sort(Comparator.comparing(o -> o.getDateTimeIntake()));
                return userDrugPlanForDay;
        }

}