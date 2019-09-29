package com.doccuty.epill.userdrugplan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.doccuty.epill.drug.Drug;
import com.doccuty.epill.model.Interaction;
import com.doccuty.epill.user.User;
import com.doccuty.epill.userprescription.UserPrescription;
import com.doccuty.epill.userprescription.UserPrescriptionItem;

public class UserDrugPlanCalculator {
        private static final Logger LOG = LoggerFactory.getLogger(UserDrugPlanCalculator.class);

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
                //TODO: emptyStomach, 
                for (final Drug drug : userDrugsTaking) {
                        LOG.info("drug taking {}, ", drug.getName());
                        userDrugPlanForDay.addAll(createDefaultUserDrugPlanItems(drug, user, day));
                }

                userDrugPlanForDay = getSortedUserDrugPlanByDatetimeIntakePlanned(userDrugPlanForDay);

                //userDrugPlanForDay = adjustUserDrugPlanByInteractions(userDrugPlanForDay);

                return userDrugPlanForDay;
        }

        private List<UserDrugPlanItem> adjustUserDrugPlanByInteractions(List<UserDrugPlanItem> userDrugPlanForDay) {
                final StringBuilder interactionText = new StringBuilder();
                for (int i = 0; i < userDrugPlanForDay.size() - 1; i++) {
                        // check interactions between drugs
                        // if interaction:
                        final Drug drug = userDrugPlanForDay.get(i).getDrug();
                        for (final Interaction interaction : drug.getInteraction()) {
                                final Drug drugCompare = userDrugPlanForDay.get(i + 1).getDrug();
                                if (interaction.getInteractionDrug().contains(drugCompare)) {
                                        interactionText.append("<p>" + drug.getName() + "</p> - " + drugCompare.getName() + ": "
                                                        + interaction.getInteraction() + "</p>");
                                }
                        }
                        //TODO: only adjust if NOT onEmptyStomach AND NOT onFullStomach 
                        //check interactions with next drug
                        if (checkInteraction(userDrugPlanForDay.get(i), userDrugPlanForDay.get(i + 1))) {
                        	if (checkAdjustmentAllowed(userDrugPlanForDay.get(i + 1))) {
                                adjustDateTimeIntakePlanned(userDrugPlanForDay.get(i + 1));
                        	} else if (checkAdjustmentAllowed(userDrugPlanForDay.get(i))) {
                                adjustDateTimeIntakePlanned(userDrugPlanForDay.get(i + 1));
                        	}
                        }
                }

                return getSortedUserDrugPlanByDatetimeIntakePlanned(userDrugPlanForDay);
        }

       private boolean checkAdjustmentAllowed( UserDrugPlanItem userDrugPlan) {
    	   if (userDrugPlan.getDrug().getTakeOnEmptyStomach() || userDrugPlan.getDrug().getTakeOnFullStomach()) {
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
       private void adjustDateTimeIntakePlanned(UserDrugPlanItem userDrugPlanItemToAdjust) {
               LOG.info("adjust intake time for {}, current intake time = {}, add hours",
                               userDrugPlanItemToAdjust.getDrug().getName(), userDrugPlanItemToAdjust.getDatetimeIntakePlanned());
               userDrugPlanItemToAdjust
                               .setDateTimePlanned(DateUtils.setHoursOfDate(userDrugPlanItemToAdjust.getDatetimeIntakePlanned(), 2));
               LOG.info("intake time for {} adjusted: current intake time = {}", userDrugPlanItemToAdjust.getDrug().getName(),
                               userDrugPlanItemToAdjust.getDatetimeIntakePlanned());
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

        private List<UserDrugPlanItem> createDefaultUserDrugPlanItemsOld(Drug drug, User user, Date day) {
                final List<UserDrugPlanItem> userDrugPlanForDay = new ArrayList<>();
                LOG.info("create default plan for day={} user={} drug={}", day, user.getUsername(), drug.getName());
                if (drug.getCountPerDay() == 1) {
                        userDrugPlanForDay.add(createDefaultUserDrugPlanItem(drug, user, day, this.user.getBreakfastTime()));
                } else if (drug.getCountPerDay() == 2) {
                        userDrugPlanForDay.add(createDefaultUserDrugPlanItem(drug, user, day, this.user.getBreakfastTime()));
                        userDrugPlanForDay.add(createDefaultUserDrugPlanItem(drug, user, day, this.user.getDinnerTime()));
                } else if (drug.getCountPerDay() == 3) {
                        userDrugPlanForDay.add(createDefaultUserDrugPlanItem(drug, user, day, this.user.getBreakfastTime()));
                        userDrugPlanForDay.add(createDefaultUserDrugPlanItem(drug, user, day, this.user.getLunchTime()));
                        userDrugPlanForDay.add(createDefaultUserDrugPlanItem(drug, user, day, this.user.getDinnerTime()));
                } else if (drug.getCountPerDay() > 3) {
                        LOG.warn("not supported for more than 3 intake times per day");
                }
                LOG.info("created {} items day={} user={} drug={}", userDrugPlanForDay.size(), day, user.getUsername(),
                                drug.getName());
                return userDrugPlanForDay;
        }
        
        private List<UserDrugPlanItem> createDefaultUserDrugPlanItems(Drug drug, User user, Date day) {
            final List<UserDrugPlanItem> userDrugPlanForDay = new ArrayList<>();
            LOG.info("create default plan for day={} user={} drug={}", day, user.getUsername(), drug.getName());
            for (UserPrescription userPrescription : drug.getUserPrescriptions()) {
            	for (UserPrescriptionItem item : userPrescription.getUserPrescriptionItems()) {
            		userDrugPlanForDay.add(createDefaultUserDrugPlanItem(drug, user, day, item.getIntakeTime()));
            	}
            }
            LOG.info("created {} items day={} user={} drug={}", userDrugPlanForDay.size(), day, user.getUsername(),
                            drug.getName());
            return userDrugPlanForDay;
    }

        private UserDrugPlanItem createDefaultUserDrugPlanItem(Drug drug, User user, Date day, int hourOfDay) {
                final UserDrugPlanItem item = new UserDrugPlanItem();
                item.setDateTimePlanned(getDate(day, hourOfDay));
                LOG.info("planned time {} for drug {} ", item.getDatetimeIntakePlanned(), drug.getName());
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

        private List<UserDrugPlanItem> getSortedUserDrugPlanByDatetimeIntakePlanned(List<UserDrugPlanItem> userDrugPlanForDay) {
                userDrugPlanForDay.sort(Comparator.comparing(o -> o.getDatetimeIntakePlanned()));
                return userDrugPlanForDay;
        }

}

