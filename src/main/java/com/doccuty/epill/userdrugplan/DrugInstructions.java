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
@Table(name = "drug_instructions")

public class DrugInstructions implements SendableEntity {
public DrugInstructions() {
		
	}
	
	public DrugInstructions(long id, String instructions){
		this.id = id;
		this.instructions = instructions;
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

	public DrugInstructions withId(long value) {
		setId(value);
		return this;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append(" ").append(this.getId());
		result.append(" ").append(this.getInstructions());
		return result.substring(1);
	}

	// ==========================================================================

	public static final String PROPERTY_INSTRUCTIONS = "instructions";

	private String instructions;

	public String getInstructions() {
		return this.instructions;
	}

	public void setInstructions(String value) {
		if (!EntityUtil.stringEquals(this.instructions, value)) {

			String oldValue = this.instructions;
			this.instructions = value;
			this.firePropertyChange(PROPERTY_INSTRUCTIONS, oldValue, value);
		}
	}

	public DrugInstructions withFoodToAvoid(String value) {
		setInstructions(value);
		return this;
	}
	
	 /********************************************************************
	    * <pre>
	    *              one                       many
	    * DrugInstructions ----------------------------------- Drug
	    *     instructions                drug
	    * </pre>
	    */
	   
	   public static final String PROPERTY_DRUG = "instructions";

	   @ManyToMany(cascade=CascadeType.ALL, mappedBy="instructions")
	   private Set<Drug> drug = null;
	   
	   public Set<Drug> getDrug()
	   {
	      if (this.drug == null)
	      {
	         return DrugSet.EMPTY_SET;
	      }
	   
	      return this.drug;
	   }

	   public DrugInstructions withDrug(Drug... value)
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
	               item.withInstructions(this);
	               firePropertyChange(PROPERTY_INSTRUCTIONS, null, item);
	            }
	         }
	      }
	      return this;
	   } 

	   public DrugInstructions withoutDrug(Drug... value)
	   {
	      for (Drug item : value)
	      {
	         if ((this.drug != null) && (item != null))
	         {
	            if (this.drug.remove(item))
	            {
	               item.withoutInstructions(this);
	               firePropertyChange(PROPERTY_INSTRUCTIONS, item, null);
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
