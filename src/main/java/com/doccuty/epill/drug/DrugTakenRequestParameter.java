package com.doccuty.epill.drug;

/**
 * Request parameter for drug taken 
 * 
 * i.e.
 * {"DrugTakenRequestParameter": {
		   "isTaken":true,"userDrugPlanItemId":31}
		}
 * 
 * @author cs
 *
 */
public class DrugTakenRequestParameter {

	private boolean isTaken;
	private long userDrugPlanItemId;
	
	public boolean isTaken() {
		return isTaken;
	}
	public void setTaken(boolean isTaken) {
		this.isTaken = isTaken;
	}
	public long getUserDrugPlanItemId() {
		return userDrugPlanItemId;
	}
	public void setUserDrugPlanItemId(long userDrugPlanItemId) {
		this.userDrugPlanItemId = userDrugPlanItemId;
	}
}
