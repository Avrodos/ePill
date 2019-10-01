package com.doccuty.epill.userdrugplan;

import java.util.List;

/**
 * view model for medication list:
 */
public class UserDrugPlanItemViewModel {

	private long userDrugPlanItemId; 
	private boolean intermediateStep;
	private String dateString;
	private String timeString;
	private int halfTimePeriod;
	private int percentage;
	private List<DrugViewModel> drugsPlannedSameTime;

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

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public long getUserDrugPlanItemId() {
		return userDrugPlanItemId;
	}

	public void setUserDrugPlanItemId(long userDrugPlanItemId) {
		this.userDrugPlanItemId = userDrugPlanItemId;
	}

	public int getHalfTimePeriod() {
		return halfTimePeriod;
	}

	public void setHalfTimePeriod(int halfTimePeriod) {
		this.halfTimePeriod = halfTimePeriod;
	}

	public List<DrugViewModel> getDrugsPlannedSameTime() {
		return drugsPlannedSameTime;
	}

	public void setDrugsPlannedSameTime(List<DrugViewModel> drugsSameTime) {
		this.drugsPlannedSameTime = drugsSameTime;
	}
}
