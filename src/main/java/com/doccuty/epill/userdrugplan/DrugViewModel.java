package com.doccuty.epill.userdrugplan;

import java.io.Serializable;
import java.util.List;

public class DrugViewModel {
	private static final long serialVersionUID = 4465215282796960562L;
	private long id;
	private String name;
	private long link;
	private boolean drugTaken;	
	private boolean hasInteractions;
	private boolean takeOnEmptyStomach;
	private boolean takeOnFullStomach;
	private boolean takeToMeals;
	String personalizedInformation;
	private List<String> diseases;
	private List<String> interactions;
	
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
	public long getLink() {
		return link;
	}
	public void setLink(long link) {
		this.link = link;
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
	public List<String> getInteractions() {
		return interactions;
	}
	public void setInteractions(List<String> interactions) {
		this.interactions = interactions;
	}
}
