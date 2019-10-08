package com.doccuty.epill.userdrugplan;

import de.uniks.networkparser.interfaces.SendableEntity;
import java.beans.PropertyChangeSupport;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.doccuty.epill.drug.Drug;
import com.doccuty.epill.model.util.DrugSet;

import java.beans.PropertyChangeListener;
import de.uniks.networkparser.EntityUtil;

@Entity
@Table(name = "food_to_avoid")
public class FoodToAvoid implements SendableEntity{
public FoodToAvoid() {
		
	}
	
	public FoodToAvoid(long id, String foodToAvoid){
		this.id = id;
		this.foodToAvoid = foodToAvoid;
	}
	
	
	// ==========================================================================

	protected PropertyChangeSupport listeners = null;

	public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		if (listeners != null) {
			listeners.firePropertyChange(propertyName, oldValue, newValue);
			return true;
		}
		return false;
	}

	public boolean addPropertyChangeListener(PropertyChangeListener listener) {
		if (listeners == null) {
			listeners = new PropertyChangeSupport(this);
		}
		listeners.addPropertyChangeListener(listener);
		return true;
	}

	public boolean addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		if (listeners == null) {
			listeners = new PropertyChangeSupport(this);
		}
		listeners.addPropertyChangeListener(propertyName, listener);
		return true;
	}

	public boolean removePropertyChangeListener(PropertyChangeListener listener) {
		if (listeners == null) {
			listeners.removePropertyChangeListener(listener);
		}
		listeners.removePropertyChangeListener(listener);
		return true;
	}

	public boolean removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		if (listeners != null) {
			listeners.removePropertyChangeListener(propertyName, listener);
		}
		return true;
	}

	// ==========================================================================

	public void removeYou() {
		firePropertyChange("REMOVE_YOU", this, null);
	}

	// ==========================================================================

	public static final String PROPERTY_ID = "id";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	public long getId() {
		return this.id;
	}

	public void setId(long value) {
		if (this.id != value) {

			long oldValue = this.id;
			this.id = value;
			this.firePropertyChange(PROPERTY_ID, oldValue, value);
		}
	}

	public FoodToAvoid withId(long value) {
		setId(value);
		return this;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append(" ").append(this.getId());
		result.append(" ").append(this.getFoodToAvoid());
		return result.substring(1);
	}

	// ==========================================================================

	public static final String PROPERTY_FOOD_TO_AVOID = "foodToAvoid";

	private String foodToAvoid;

	public String getFoodToAvoid() {
		return this.foodToAvoid;
	}

	public void setFoodToAvoid(String value) {
		if (!EntityUtil.stringEquals(this.foodToAvoid, value)) {

			String oldValue = this.foodToAvoid;
			this.foodToAvoid = value;
			this.firePropertyChange(PROPERTY_FOOD_TO_AVOID, oldValue, value);
		}
	}

	public FoodToAvoid withFoodToAvoid(String value) {
		setFoodToAvoid(value);
		return this;
	}
	
	 /********************************************************************
	    * <pre>
	    *              one                       many
	    * FoodToAvoid ----------------------------------- Drug
	    *     foodToAvoid                drug
	    * </pre>
	    */
	   
	   public static final String PROPERTY_DRUG = "food_to_avoid";

	   @ManyToMany(cascade=CascadeType.ALL, mappedBy="foodToAvoid")
	   private Set<Drug> drug = null;
	   
	   public Set<Drug> getDrug()
	   {
	      if (this.drug == null)
	      {
	         return DrugSet.EMPTY_SET;
	      }
	   
	      return this.drug;
	   }

	   public FoodToAvoid withDrug(Drug... value)
	   {
	      if(value==null){
	         return this;
	      }
	      for (Drug item : value)
	      {
	         if (item != null)
	         {
	            if (this.drug == null)
	            {
	               this.drug = new DrugSet();
	            }
	            
	            boolean changed = this.drug.add (item);

	            if (changed)
	            {
	               item.withFoodToAvoid(this);
	               firePropertyChange(PROPERTY_FOOD_TO_AVOID, null, item);
	            }
	         }
	      }
	      return this;
	   } 

	   public FoodToAvoid withoutDrug(Drug... value)
	   {
	      for (Drug item : value)
	      {
	         if ((this.drug != null) && (item != null))
	         {
	            if (this.drug.remove(item))
	            {
	               item.withoutFoodToAvoid(this);
	               firePropertyChange(PROPERTY_FOOD_TO_AVOID, item, null);
	            }
	         }
	      }
	      return this;
	   }

	   public Drug createDrug()
	   {
	      Drug value = new Drug();
	      withDrug(value);
	      return value;
	   }
	
}
