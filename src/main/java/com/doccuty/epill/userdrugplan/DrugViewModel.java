package com.doccuty.epill.userdrugplan;

import java.io.Serializable;
import java.util.List;

public class DrugViewModel {
	private static final long serialVersionUID = 4465215282796960562L;
	private long id;
	private long userDrugPlanItemId;
	private String name;
	private long link;
	private boolean drugTaken;	
	private boolean hasInteractions;
	private boolean takeOnEmptyStomach;
	private boolean takeOnFullStomach;
	private boolean takeToMeals;
	String personalizedInformation;
	private List<String> diseases;
	private String interactions;
	private List<String> food;
	private List<String> instructions;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getFood() {
		return food;
	}
	public void setFood(List<String> food) {
		this.food = food;
	}
	public List<String> getInstructions() {
		return instructions;
	}
	public void setInstructions(List<String> instructions) {
		this.instructions = instructions;
	}
	public long getLink() {
		return link;
	}
	public void setLink(long link) {
		this.link = link;
	}
	public long getUserDrugPlanItemId() {
		return userDrugPlanItemId;
	}
	public void setUserDrugPlanItemId(long userDrugPlanItemId) {
		this.userDrugPlanItemId = userDrugPlanItemId;
	}
	public List<String> getDiseases() {
		return diseases;
	}
	public void setDiseases(List<String> diseases) {
		this.diseases = diseases;
	}
	public boolean isDrugTaken() {
		return drugTaken;
	}
	public void setDrugTaken(boolean drugTaken) {
		this.drugTaken = drugTaken;
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
	public void setTakeOnFullStomach(boolean takeOnFullStomach) {
		this.takeOnFullStomach = takeOnFullStomach;
	}
	public boolean isTakeToMeals() {
		return takeToMeals;
	}
	public void setTakeToMeals(boolean takeToMeals) {
		this.takeToMeals = takeToMeals;
	}
	public String getPersonalizedInformation() {
		return personalizedInformation;
	}
	public void setPersonalizedInformation(String personalizedInformation) {
		this.personalizedInformation = personalizedInformation;
	}
	public String getInteractions() {
		return interactions;
	}
	public void setInteractions(String interactions) {
		this.interactions = interactions;
	}
}
