package com.doccuty.epill.userdrugplan;

/**
* Request parameter for UserPrescription 
* 
* i.e.
* {"UserPrescriptionRequestParameter": {
*          "drugId": 2,
		   "periodInDays":1,"intakeBreakfastTime":true, 
		   "intakeSleepTime":false, 
		   "intakeLunchTime":false, 
		   "intakeDinnerTime":true}
		}
* 
* @author cs
*
*/
public class UserPrescriptionRequestParameter {
	private long drugId;
	private int periodInDays;
	private boolean intakeSleepTime;
	private boolean intakeBreakfastTime;
	private boolean intakeLunchTime;
	private boolean intakeDinnerTime;
	
	public int getPeriodInDays() {
		return periodInDays;
	}
	public void setPeriodInDays(int periodInDays) {
		this.periodInDays = periodInDays;
	}
	public boolean getIntakeSleepTime() {
		return intakeSleepTime;
	}
	public void setIntakeSleepTime(boolean intakeSleepTime) {
		this.intakeSleepTime = intakeSleepTime;
	}
	public boolean getIntakeBreakfastTime() {
		return intakeBreakfastTime;
	}
	public void setIntakeBreakfastTime(boolean intakeBreakfastTime) {
		this.intakeBreakfastTime = intakeBreakfastTime;
	}
	public boolean getIntakeLunchTime() {
		return intakeLunchTime;
	}
	public void setIntakeLunchTime(boolean intakeLunchTime) {
		this.intakeLunchTime = intakeLunchTime;
	}
	public boolean getIntakeDinnerTime() {
		return intakeDinnerTime;
	}
	public void setIntakeDinnerTime(boolean intakeDinnerTime) {
		this.intakeDinnerTime = intakeDinnerTime;
	}
	public long getDrugId() {
		return drugId;
	}
	public void setDrugId(long drugId) {
		this.drugId = drugId;
	}
	
}
