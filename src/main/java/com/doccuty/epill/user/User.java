/*
   Copyright (c) 2017 mac
   
   Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
   and associated documentation files (the "Software"), to deal in the Software without restriction, 
   including without limitation the rights to use, copy, modify, merge, publish, distribute, 
   sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is 
   furnished to do so, subject to the following conditions: 
   
   The above copyright notice and this permission notice shall be included in all copies or 
   substantial portions of the Software. 
   
   The Software shall be used for Good, not Evil. 
   
   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
   BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
   NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
   DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */

package com.doccuty.epill.user;

import com.doccuty.epill.allergy.Allergy;
import com.doccuty.epill.condition.Condition;
import com.doccuty.epill.diabetes.Diabetes;
import com.doccuty.epill.disease.Disease;
import com.doccuty.epill.drug.Drug;
import com.doccuty.epill.gender.Gender;
import com.doccuty.epill.intolerance.Intolerance;
import com.doccuty.epill.iteminvocation.ItemInvocation;
import com.doccuty.epill.language.Language;
import com.doccuty.epill.model.Country;
import com.doccuty.epill.model.DrugFeature;
import com.doccuty.epill.model.PackagingTopic;
import com.doccuty.epill.model.UserQuery;
import com.doccuty.epill.model.util.*;
import com.doccuty.epill.userdrugplan.UserDrugPlan;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @see <a href=
 *      '../../../../../../../src/test/java/com/doccuty/epill/model/SDMLib/ModelCreator.java'>ModelCreator.java</a>
 */
@Entity
@Table(name = "user_")
public class User extends SimpleUser {

	public void removeYou() {
		withoutPreferredPackagingTopic(this.getPreferredPackagingTopic()
				.toArray(new PackagingTopic[this.getPreferredPackagingTopic().size()]));
		withoutPreferredDrugFeature(
				this.getPreferredDrugFeature().toArray(new DrugFeature[this.getPreferredDrugFeature().size()]));
		setCountry(null);
		setLanguage(null);
		setGender(null);
		withoutClicks(this.getClicks().toArray(new ItemInvocation[this.getClicks().size()]));
		withoutUserDrugPlans(this.getUserDrugPlans().toArray(new UserDrugPlan[this.getUserDrugPlans().size()]));
		withoutQuery(this.getQuery().toArray(new UserQuery[this.getQuery().size()]));
		withoutDisease(this.getDisease().toArray(new Disease[this.getDisease().size()]));
		withoutAllergy(this.getAllergy().toArray(new Allergy[this.getAllergy().size()]));
		withoutIntolerance(this.getIntolerance().toArray(new Intolerance[this.getIntolerance().size()]));
		withoutCondition(this.getCondition().toArray(new Condition[this.getCondition().size()]));
		firePropertyChange("REMOVE_YOU", this, null);
		setDiabetes(null);
	}

	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();

		result.append(" ").append(this.getId());
		result.append(" ").append(this.getFirstname());
		result.append(" ").append(this.getLastname());
		result.append(" ").append(this.getUsername());
		result.append(" ").append(this.getEmail());
		result.append(" ").append(this.getLevelOfDetail());
		result.append(" ").append(this.getLanguage());
		result.append(" ").append(this.getGender());
		result.append(" ").append(this.getDisease());
        result.append(" ").append(this.getAllergy());
		result.append(" ").append(this.getIntolerance());
		result.append(" ").append(this.getCondition());
		result.append(" ").append(this.getDiabetes());

		return super.toString() + result.substring(1);
	}

	/********************************************************************
	 * <pre>
	 *              many                       many
	 * User ----------------------------------- PackagingTopic
	 *              user                   preferredPackagingTopic
	 * </pre>
	 */

	public static final String PROPERTY_PREFERREDPACKAGINGTOPIC = "preferredPackagingTopic";

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_preferred_topic", joinColumns = @JoinColumn(name = "id"), inverseJoinColumns = @JoinColumn(name = "idpackaging_topic"))
	private Set<PackagingTopic> preferredPackagingTopic = null;

	public Set<PackagingTopic> getPreferredPackagingTopic() {
		if (this.preferredPackagingTopic == null) {
			return PackagingTopicSet.EMPTY_SET;
		}

		return this.preferredPackagingTopic;
	}

	public User withPreferredPackagingTopic(PackagingTopic... value) {
		if (value == null) {
			return this;
		}
		for (final PackagingTopic item : value) {
			if (item != null) {
				if (this.preferredPackagingTopic == null) {
					this.preferredPackagingTopic = new PackagingTopicSet();
				}

				final boolean changed = this.preferredPackagingTopic.add(item);

				if (changed) {
					item.withUser(this);
					firePropertyChange(PROPERTY_PREFERREDPACKAGINGTOPIC, null, item);
				}
			}
		}
		return this;
	}

	public User withoutPreferredPackagingTopic(PackagingTopic... value) {
		for (final PackagingTopic item : value) {
			if ((this.preferredPackagingTopic != null) && (item != null)) {
				if (this.preferredPackagingTopic.remove(item)) {
					item.withoutUser(this);
					firePropertyChange(PROPERTY_PREFERREDPACKAGINGTOPIC, item, null);
				}
			}
		}
		return this;
	}

	public PackagingTopic createPreferredPackagingTopic() {
		final PackagingTopic value = new PackagingTopic();
		withPreferredPackagingTopic(value);
		return value;
	}

	/********************************************************************
	 * <pre>
	 *              many                       many
	 * User ----------------------------------- DrugFeature
	 *              user                   preferredDrugFeature
	 * </pre>
	 */

	public static final String PROPERTY_PREFERREDDRUGFEATURE = "preferredDrugFeature";

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_drug_feature", joinColumns = @JoinColumn(name = "iduser"), inverseJoinColumns = @JoinColumn(name = "iddrug_feature"))
	private Set<DrugFeature> preferredDrugFeature = null;

	public Set<DrugFeature> getPreferredDrugFeature() {
		if (this.preferredDrugFeature == null) {
			return DrugFeatureSet.EMPTY_SET;
		}

		return this.preferredDrugFeature;
	}

	public User withPreferredDrugFeature(DrugFeature... value) {
		if (value == null) {
			return this;
		}
		for (final DrugFeature item : value) {
			if (item != null) {
				if (this.preferredDrugFeature == null) {
					this.preferredDrugFeature = new DrugFeatureSet();
				}

				final boolean changed = this.preferredDrugFeature.add(item);

				if (changed) {
					item.withUser(this);
					firePropertyChange(PROPERTY_PREFERREDDRUGFEATURE, null, item);
				}
			}
		}
		return this;
	}

	public User withoutPreferredDrugFeature(DrugFeature... value) {
		for (final DrugFeature item : value) {
			if ((this.preferredDrugFeature != null) && (item != null)) {
				if (this.preferredDrugFeature.remove(item)) {
					item.withoutUser(this);
					firePropertyChange(PROPERTY_PREFERREDDRUGFEATURE, item, null);
				}
			}
		}
		return this;
	}

	public DrugFeature createPreferredDrugFeature() {
		final DrugFeature value = new DrugFeature();
		withPreferredDrugFeature(value);
		return value;
	}

	/********************************************************************
	 * <pre>
	 *              many                       many
	 * User ----------------------------------- Drug
	 *              user                   takingDrug
	 * </pre>
	 */

	public static final String PROPERTY_TAKINGDRUG = "takingDrug";

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_drug_taking", joinColumns = @JoinColumn(name = "iduser"), inverseJoinColumns = @JoinColumn(name = "iddrug"))
	private Set<Drug> takingDrug = null;

	public Set<Drug> getTakingDrug() {
		if (this.takingDrug == null) {
			return DrugSet.EMPTY_SET;
		}

		return this.takingDrug;
	}

	public User withTakingDrug(Drug... value) {
		if (value == null) {
			return this;
		}
		for (final Drug item : value) {
			if (item != null) {
				if (this.takingDrug == null) {
					this.takingDrug = new DrugSet();
				}

				final boolean changed = this.takingDrug.add(item);

				if (changed) {
					item.withUser(this);
					firePropertyChange(PROPERTY_TAKINGDRUG, null, item);
				}
			}
		}
		return this;
	}

	public User withoutTakingDrug(Drug... value) {
		for (final Drug item : value) {
			if ((this.takingDrug != null) && (item != null)) {
				if (this.takingDrug.remove(item)) {
					item.withoutUser(this);
					firePropertyChange(PROPERTY_TAKINGDRUG, item, null);
				}
			}
		}
		return this;
	}

	public Drug createTakingDrug() {
		final Drug value = new Drug();
		withTakingDrug(value);
		return value;
	}

	/********************************************************************
	 * <pre>
	 *              many                       many
	 * User ----------------------------------- Drug
	 *              user                   rememberedDrug
	 * </pre>
	 */

	public static final String PROPERTY_REMEMBEREDDRUG = "rememberedDrug";

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_drug_remember", joinColumns = @JoinColumn(name = "iduser"), inverseJoinColumns = @JoinColumn(name = "iddrug"))
	private Set<Drug> rememberedDrug = null;

	public Set<Drug> geRememberedDrug() {
		if (this.rememberedDrug == null) {
			return DrugSet.EMPTY_SET;
		}

		return this.rememberedDrug;
	}

	public User withRememberedDrug(Drug... value) {
		if (value == null) {
			return this;
		}
		for (final Drug item : value) {
			if (item != null) {
				if (this.rememberedDrug == null) {
					this.rememberedDrug = new DrugSet();
				}

				final boolean changed = this.rememberedDrug.add(item);

				if (changed) {
					item.withUserRemembering(this);
					firePropertyChange(PROPERTY_REMEMBEREDDRUG, null, item);
				}
			}
		}
		return this;
	}

	public User withoutRememberedDrug(Drug... value) {
		for (final Drug item : value) {
			if ((this.rememberedDrug != null) && (item != null)) {
				if (this.rememberedDrug.remove(item)) {
					item.withoutUserRemembering(this);
					firePropertyChange(PROPERTY_REMEMBEREDDRUG, item, null);
				}
			}
		}
		return this;
	}

	public Drug createRememberedDrug() {
		final Drug value = new Drug();
		withRememberedDrug(value);
		return value;
	}

	/********************************************************************
	 * <pre>
	 *              many                       one
	 * User ----------------------------------- Country
	 *              user                   country
	 * </pre>
	 */

	public static final String PROPERTY_COUNTRY = "country";

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idcountry")
	private Country country = null;

	public Country getCountry() {
		return this.country;
	}

	public boolean setCountry(Country value) {
		boolean changed = false;

		if (this.country != value) {
			final Country oldValue = this.country;

			if (this.country != null) {
				this.country = null;
				oldValue.withoutUser(this);
			}

			this.country = value;

			if (value != null) {
				value.withUser(this);
			}

			firePropertyChange(PROPERTY_COUNTRY, oldValue, value);
			changed = true;
		}

		return changed;
	}

	public User withCountry(Country value) {
		setCountry(value);
		return this;
	}

	public Country createCountry() {
		final Country value = new Country();
		withCountry(value);
		return value;
	}

	/********************************************************************
	 * <pre>
	 *              many                       one
	 * User ----------------------------------- Language
	 *              user                   language
	 * </pre>
	 */

	public static final String PROPERTY_LANGUAGE = "language";

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idlanguage")
	private Language language = null;

	public Language getLanguage() {
		return this.language;
	}

	public boolean setLanguage(Language value) {
		boolean changed = false;

		if (this.language != value) {
			final Language oldValue = this.language;

			if (this.language != null) {
				this.language = null;
				oldValue.withoutUser(this);
			}

			this.language = value;

			if (value != null) {
				value.withUser(this);
			}

			firePropertyChange(PROPERTY_LANGUAGE, oldValue, value);
			changed = true;
		}

		return changed;
	}

	public User withLanguage(Language value) {
		setLanguage(value);
		return this;
	}

	public Language createLanguage() {
		final Language value = new Language();
		withLanguage(value);
		return value;
	}

	/********************************************************************
	 * <pre>
	 *              many                       one
	 * User ----------------------------------- Gender
	 *              user                   gender
	 * </pre>
	 */

	public static final String PROPERTY_GENDER = "gender";

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idgender")
	private Gender gender = null;

	public Gender getGender() {
		return this.gender;
	}

	public boolean setGender(Gender value) {
		boolean changed = false;

		if (this.gender != value) {
			final Gender oldValue = this.gender;

			if (this.gender != null) {
				this.gender = null;
				oldValue.withoutUser(this);
			}

			this.gender = value;

			if (value != null) {
				value.withUser(this);
			}

			firePropertyChange(PROPERTY_GENDER, oldValue, value);
			changed = true;
		}

		return changed;
	}

	public User withGender(Gender value) {
		setGender(value);
		return this;
	}

	public Gender createGender() {
		final Gender value = new Gender();
		withGender(value);
		return value;
	}

	/********************************************************************
	 * <pre>
	 *              one                       many
	 * User ----------------------------------- ItemInvocation
	 *              user                   clicks
	 * </pre>
	 */

	public static final String PROPERTY_CLICKS = "clicks";

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private Set<ItemInvocation> clicks = null;

	public Set<ItemInvocation> getClicks() {
		if (this.clicks == null) {
			return ItemInvocationSet.EMPTY_SET;
		}

		return this.clicks;
	}

	public User withClicks(ItemInvocation... value) {
		if (value == null) {
			return this;
		}
		for (final ItemInvocation item : value) {
			if (item != null) {
				if (this.clicks == null) {
					this.clicks = new ItemInvocationSet();
				}

				final boolean changed = this.clicks.add(item);

				if (changed) {
					item.withUser(this);
					firePropertyChange(PROPERTY_CLICKS, null, item);
				}
			}
		}
		return this;
	}

	public User withoutClicks(ItemInvocation... value) {
		for (final ItemInvocation item : value) {
			if ((this.clicks != null) && (item != null)) {
				if (this.clicks.remove(item)) {
					item.setUser(null);
					firePropertyChange(PROPERTY_CLICKS, item, null);
				}
			}
		}
		return this;
	}

	/*
	 * public ItemInvocation createClicks() { ItemInvocation value = new
	 * ItemInvocation(); withClicks(value); return value; }
	 */

	/********************************************************************
	 * <pre>
	 *              one                       many
	 * User ----------------------------------- UserDrugPlan
	 *              user                   clicks
	 * </pre>
	 */

	public static final String PROPERTY_USER_DRUG_PLAN = "user_drug_plans";
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private Set<UserDrugPlan> userDrugPlans = null;

	public Set<UserDrugPlan> getUserDrugPlans() {
		if (this.userDrugPlans == null) {
			return new HashSet<>();
		}
		return this.userDrugPlans;
	}

	public User withUserDrugPlans(UserDrugPlan... value) {
		if (value == null) {
			return this;
		}
		for (final UserDrugPlan item : value) {
			if (item != null) {
				if (this.userDrugPlans == null) {
					this.userDrugPlans = new HashSet<>();
				}
				final boolean changed = this.userDrugPlans.add(item);
				if (changed) {
					item.withUser(this);
					firePropertyChange(PROPERTY_USER_DRUG_PLAN, null, item);
				}
			}
		}
		return this;
	}

	public User withoutUserDrugPlans(UserDrugPlan... value) {
		for (final UserDrugPlan item : value) {
			if ((this.userDrugPlans != null) && (item != null)) {
				if (this.userDrugPlans.remove(item)) {
					item.setUser(null);
					firePropertyChange(PROPERTY_USER_DRUG_PLAN, item, null);
				}
			}
		}
		return this;
	}

	/*
	 * public UserDrugPlan createUserDrugPlan() { UserDrugPlan value = new
	 * UserDrugPlan(); withUserDrugPlans(value); return value; }
	 */

	/********************************************************************
	 * <pre>
	 *              one                       many
	 * User ----------------------------------- UserQuery
	 *              user                   query
	 * </pre>
	 */

	public static final String PROPERTY_QUERY = "query";

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private Set<UserQuery> query = null;

	public Set<UserQuery> getQuery() {
		if (this.query == null) {
			return UserQuerySet.EMPTY_SET;
		}

		return this.query;
	}

	public User withQuery(UserQuery... value) {
		if (value == null) {
			return this;
		}
		for (final UserQuery item : value) {
			if (item != null) {
				if (this.query == null) {
					this.query = new UserQuerySet();
				}

				final boolean changed = this.query.add(item);

				if (changed) {
					item.withUser(this);
					firePropertyChange(PROPERTY_QUERY, null, item);
				}
			}
		}
		return this;
	}

	public User withoutQuery(UserQuery... value) {
		for (final UserQuery item : value) {
			if ((this.query != null) && (item != null)) {
				if (this.query.remove(item)) {
					item.setUser(null);
					firePropertyChange(PROPERTY_QUERY, item, null);
				}
			}
		}
		return this;
	}

	public UserQuery createQuery() {
		final UserQuery value = new UserQuery();
		withQuery(value);
		return value;
	}

	/********************************************************************
	 * <pre>
	 *              many                       many
	 * User ----------------------------------- Disease
	 *              user                   disease
	 * </pre>
	 */

	public static final String PROPERTY_DISEASE = "disease";

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_disease", joinColumns = @JoinColumn(name = "iduser"), inverseJoinColumns = @JoinColumn(name = "iddisease"))
	private Set<Disease> disease = null;

	public Set<Disease> getDisease() {
		if (this.disease == null) {
			return DiseaseSet.EMPTY_SET;
		}

		return this.disease;
	}

	public User withDisease(Disease... value) {
		if (value == null) {
			return this;
		}
		for (final Disease item : value) {
			if (item != null) {
				if (this.disease == null) {
					this.disease = new DiseaseSet();
				}

				final boolean changed = this.disease.add(item);

				if (changed) {
					item.withUser(this);
					firePropertyChange(PROPERTY_DISEASE, null, item);
				}
			}
		}
		return this;
	}

	public User withoutDisease(Disease... value) {
		for (final Disease item : value) {
			if ((this.disease != null) && (item != null)) {
				if (this.disease.remove(item)) {
					item.withoutUser(this);
					firePropertyChange(PROPERTY_DISEASE, item, null);
				}
			}
		}
		return this;
	}

	public Disease createDisease() {
		final Disease value = new Disease();
		withDisease(value);
		return value;
	}

	/********************************************************************
	 * <pre>
	 *              many                       many
	 * User ----------------------------------- Allergy
	 *              user                   allergies
	 * </pre>
	 */

	public static final String PROPERTY_ALLERGY = "allergy";


    @ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_allergy", joinColumns = @JoinColumn(name = "iduser"), inverseJoinColumns = @JoinColumn(name = "idallergy"))
	private Set<Allergy> allergy = null;

	public Set<Allergy> getAllergy() {
		if (this.allergy == null) {
			return AllergySet.EMPTY_SET;
		}

		return this.allergy;
	}

	public User withAllergy(Allergy... value) {
		if (value == null) {
			return this;
		}
		for (final Allergy item : value) {
			if (item != null) {
				if (this.allergy == null) {
					this.allergy = new AllergySet();
				}

				final boolean changed = this.allergy.add(item);

				if (changed) {
					item.withUser(this);
					firePropertyChange(PROPERTY_ALLERGY, null, item);
				}
			}
		}
		return this;
	}

	public User withoutAllergy(Allergy... value) {
		for (final Allergy item : value) {
			if ((this.allergy != null) && (item != null)) {
				if (this.allergy.remove(item)) {
					item.withoutUser(this);
					firePropertyChange(PROPERTY_ALLERGY, item, null);
				}
			}
		}
		return this;
	}

	public Allergy createAllergy() {
		final Allergy value = new Allergy();
		withAllergy(value);
		return value;
	}

	/********************************************************************
	 * <pre>
	 *              many                       many
	 * User ----------------------------------- Intolerance
	 *              user                   allergies
	 * </pre>
	 */

	public static final String PROPERTY_INTOLERANCE = "intolerance";

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_intolerance", joinColumns = @JoinColumn(name = "iduser"), inverseJoinColumns = @JoinColumn(name = "idintolerance"))
	private Set<Intolerance> intolerance = null;

	public Set<Intolerance> getIntolerance() {
		if (this.intolerance == null) {
			return IntoleranceSet.EMPTY_SET;
		}

		return this.intolerance;
	}

	public User withIntolerance(Intolerance... value) {
		if (value == null) {
			return this;
		}
		for (final Intolerance item : value) {
			if (item != null) {
				if (this.intolerance == null) {
					this.intolerance = new IntoleranceSet();
				}

				final boolean changed = this.intolerance.add(item);

				if (changed) {
					item.withUser(this);
					firePropertyChange(PROPERTY_INTOLERANCE, null, item);
				}
			}
		}
		return this;
	}

	public User withoutIntolerance(Intolerance... value) {
		for (final Intolerance item : value) {
			if ((this.intolerance != null) && (item != null)) {
				if (this.intolerance.remove(item)) {
					item.withoutUser(this);
					firePropertyChange(PROPERTY_INTOLERANCE, item, null);
				}
			}
		}
		return this;
	}

	public Intolerance createIntolerance() {
		final Intolerance value = new Intolerance();
		withIntolerance(value);
		return value;
	}

	/********************************************************************
	 * <pre>
	 *              many                       many
	 * User ----------------------------------- Condition
	 *              user                   conditions
	 * </pre>
	 */

	public static final String PROPERTY_CONDITION = "condition";

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_condition", joinColumns = @JoinColumn(name = "iduser"), inverseJoinColumns = @JoinColumn(name = "idcondition"))
	private Set<Condition> condition = null;

	public Set<Condition> getCondition() {
		if (this.condition == null) {
			return ConditionSet.EMPTY_SET;
		}

		return this.condition;
	}

	public User withCondition(Condition... value) {
		if (value == null) {
			return this;
		}
		for (final Condition item : value) {
			if (item != null) {
				if (this.condition == null) {
					this.condition = new ConditionSet();
				}

				final boolean changed = this.condition.add(item);

				if (changed) {
					item.withUser(this);
					firePropertyChange(PROPERTY_CONDITION, null, item);
				}
			}
		}
		return this;
	}

	public User withoutCondition(Condition... value) {
		for (final Condition item : value) {
			if ((this.condition != null) && (item != null)) {
				if (this.condition.remove(item)) {
					item.withoutUser(this);
					firePropertyChange(PROPERTY_CONDITION, item, null);
				}
			}
		}
		return this;
	}

	public Condition createCondition() {
		final Condition value = new Condition();
		withCondition(value);
		return value;
	}

	/********************************************************************
	 * <pre>
	 *              many                       one
	 * User ----------------------------------- Diabetes
	 *              user                   diabetes
	 * </pre>
	 */

	public static final String PROPERTY_DIABETES = "diabetes";

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "iddiabetes")
	private Diabetes diabetes = null;

	public Diabetes getDiabetes() {
		return this.diabetes;
	}

	public boolean setDiabetes(Diabetes value) {
		boolean changed = false;

		if (this.diabetes != value) {
			final Diabetes oldValue = this.diabetes;

			if (this.diabetes != null) {
				this.diabetes = null;
				oldValue.withoutUser(this);
			}

			this.diabetes = value;

			if (value != null) {
				value.withUser(this);
			}

			firePropertyChange(PROPERTY_DIABETES, oldValue, value);
			changed = true;
		}

		return changed;
	}

	public User withDiabetes(Diabetes value) {
		setDiabetes(value);
		return this;
	}

	public Diabetes createDiabetes() {
		final Diabetes value = new Diabetes();
		withDiabetes(value);
		return value;
	}


}
