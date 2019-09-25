package com.doccuty.epill.userdrugplan;

/**
 * view model for medication list:
 */
public class UserDrugPlanItemViewModel {

	private long userDrugPlanItemId; 
	private boolean drugTaken;	
	private boolean intermediateStep;
	private String dateString;
	private String timeString;
	private String drugName;
	private String drugNamesSameTime;
	private String hints;
	private boolean hasInteractions;
	private boolean takeOnEmptyStomach;
	private boolean takeOnFullStomach;
	private boolean takeToMeals;
	private int halfTimePeriod;
	private int percentage;
	private String personalizedInformation;
	private String drugDiseases;

	public boolean isIntermediateStep() {
		return intermediateStep;
	}

	public void setIntermediateStep(boolean intermediateStep) {
		this.intermediateStep = intermediateStep;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	public String getTimeString() {
		return timeString;
	}

	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}

	public boolean isDrugTaken() {
		return drugTaken;
	}

	public void setDrugTaken(boolean drugTaken) {
		this.drugTaken = drugTaken;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public String getDrugNamesSameTime() {
		return drugNamesSameTime;
	}

	public void setDrugNamesSameTime(String drugNamesSameTime) {
		this.drugNamesSameTime = drugNamesSameTime;
	}

	public String getHints() {
		return hints;
	}

	public void setHints(String hints) {
		this.hints = hints;
	}

	public boolean isHasInteractions() {
		return hasInteractions;
	}

	public void setHasInteractions(boolean hasInteractions) {
		this.hasInteractions = hasInteractions;
	}

	public boolean isTakeOnEmptyStomach() {
		return takeOnEmptyStomach;
	}

	public void setTakeOnEmptyStomach(boolean takeOnEmptyStomach) {
		this.takeOnEmptyStomach = takeOnEmptyStomach;
	}

	public boolean isTakeOnFullStomach() {
		return takeOnFullStomach;
	}

	public long getUserDrugPlanItemId() {
		return userDrugPlanItemId;
	}

	public void setUserDrugPlanItemId(long userDrugPlanItemId) {
		this.userDrugPlanItemId = userDrugPlanItemId;
	}

	public void setTakeOnFullStomach(boolean takeOnFullStomach) {
		this.takeOnFullStomach = takeOnFullStomach;
	}

	public int getHalfTimePeriod() {
		return halfTimePeriod;
	}

	public void setHalfTimePeriod(int halfTimePeriod) {
		this.halfTimePeriod = halfTimePeriod;
	}

	public String getPersonalizedInformation() {
		return personalizedInformation;
	}

	public void setPersonalizedInformation(String personalizedInformation) {
		this.personalizedInformation = personalizedInformation;
	}

	public boolean isTakeToMeals() {
		return takeToMeals;
	}

	public void setTakeToMeals(boolean takeToMeals) {
		this.takeToMeals = takeToMeals;
	}

	public String getDrugDiseases() {
		return drugDiseases;
	}

	public void setDrugDiseases(String drugDiseases) {
		this.drugDiseases = drugDiseases;
	}
	
}
