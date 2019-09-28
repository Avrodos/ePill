package com.doccuty.epill.userdrugplan;

/**
 * Request parameter for drug taken 
 * 
 * i.e.
 * {"DrugTakenRequestParameter": {
		   "drugTaken":true,"userDrugPlanItemId":31}
		}
 * 
 * @author cs
 *
 */
public class DrugTakenRequestParameter {

	private boolean drugTaken;
	private long userDrugPlanItemId;
	
	public boolean getDrugTaken() {
		return drugTaken;
	}
	public void setDrugTaken(boolean drugTaken) {
		this.drugTaken = drugTaken;
	}
	public long getUserDrugPlanItemId() {
		return userDrugPlanItemId;
	}
	public void setUserDrugPlanItemId(long userDrugPlanItemId) {
		this.userDrugPlanItemId = userDrugPlanItemId;
	}
}

