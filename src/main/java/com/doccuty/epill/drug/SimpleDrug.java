 package com.doccuty.epill.drug;

import de.uniks.networkparser.interfaces.SendableEntity;
import java.beans.PropertyChangeSupport;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.doccuty.epill.image.Image;

import java.beans.PropertyChangeListener;
import de.uniks.networkparser.EntityUtil;


@Entity  
@Table(name = "drug_simple")  
@Inheritance(strategy=InheritanceType.JOINED) 
   public  class SimpleDrug implements SendableEntity
{

	public SimpleDrug() {
	}
	
	public SimpleDrug(long id, String name) {
		this.setId(id);
		this.setName(name);
	}
	
   
   //==========================================================================
   
   protected PropertyChangeSupport listeners = null;
   
   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (listeners != null) {
   		listeners.firePropertyChange(propertyName, oldValue, newValue);
   		return true;
   	}
   	return false;
   }
   
   public boolean addPropertyChangeListener(PropertyChangeListener listener) 
   {
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

   public boolean removePropertyChangeListener(String propertyName,PropertyChangeListener listener) {
   	if (listeners != null) {
   		listeners.removePropertyChangeListener(propertyName, listener);
   	}
   	return true;
   }

   
   //==========================================================================
   
   
   public void removeYou()
   {
      firePropertyChange("REMOVE_YOU", this, null);
   }

   
   //==========================================================================
   
   public static final String PROPERTY_ID = "id";
   
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)
   private long id;

   public long getId()
   {
      return this.id;
   }
   
   public void setId(long value)
   {
      if (this.id != value) {
      
    	  long oldValue = this.id;
         this.id = value;
         this.firePropertyChange(PROPERTY_ID, oldValue, value);
      }
   }
   
   public SimpleDrug withId(long value)
   {
      setId(value);
      return this;
   } 


   @Override
   public String toString()
   {
      StringBuilder result = new StringBuilder();
      
      result.append(" ").append(this.getId());
      result.append(" ").append(this.getName());
      result.append(" ").append(this.getVersion());
      result.append(" ").append(this.getStatus());
      result.append(" ").append(this.getYear());
      return result.substring(1);
   }

   //==========================================================================
   
   public static final String PROPERTY_NAME = "name";
   
   @Column(unique = true)
   private String name;

   public String getName()
   {
      return this.name;
   }
   
   public void setName(String value)
   {
      if ( ! EntityUtil.stringEquals(this.name, value)) {
      
         String oldValue = this.name;
         this.name = value;
         this.firePropertyChange(PROPERTY_NAME, oldValue, value);
      }
   }
   
   public SimpleDrug withName(String value)
   {
      setName(value);
      return this;
   } 
   
   //==========================================================================
   
   public static final String PROPERTY_VERSION = "version";
   
   private String version;

   public String getVersion()
   {
      return this.version;
   }
   
   public void setVersion(String value)
   {
      if ( ! EntityUtil.stringEquals(this.version, value)) {
      
         String oldValue = this.version;
         this.version = value;
         this.firePropertyChange(PROPERTY_VERSION, oldValue, value);
      }
   }
   
   public SimpleDrug withVersion(String value)
   {
      setVersion(value);
      return this;
   }
   
   //==========================================================================
   
   public static final String PROPERTY_NUMBER = "number";
   
   private int number;

   public int getNumber()
   {
      return this.number;
   }
   
   public void setNumber(int value)
   {
      if (this.number != value) {
      
         int oldValue = this.number;
         this.number = value;
         this.firePropertyChange(PROPERTY_NUMBER, oldValue, value);
      }
   }
   
   public SimpleDrug withNumber(int value)
   {
      setNumber(value);
      return this;
   }


	// ==========================================================================

	public static final String PROPERTY_STATUS = "status";

	@Temporal(TemporalType.DATE)
	private Date status;
	
	public Date getStatus()
	{
	   return this.status;
	}
	
	public void setStatus(Date value)
	{
	   if (this.status != value) {
	   
		   Date oldValue = this.status;
	      this.status = value;
	      this.firePropertyChange(PROPERTY_STATUS, oldValue, value);
	   }
	}
	
	public SimpleDrug withStatus(Date value)
	{
	   setStatus(value);
	   return this;
	}


	// ==========================================================================

	public static final String PROPERTY_YEAR = "year";

	@Temporal(TemporalType.DATE)
	private Date year;
	
	public Date getYear()
	{
	   return this.year;
	}
	
	public void setYear(Date value)
	{
	   if (this.year != value) {
	   
	      Date oldValue = this.year;
	      this.year = value;
	      this.firePropertyChange(PROPERTY_YEAR, oldValue, value);
	   }
	}
	
	public SimpleDrug withYear(Date value)
	{
	   setYear(value);
	   return this;
	}

   //==========================================================================
   
   public static final String PROPERTY_IMAGE = "drug_image";
   
   @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "idimage")
   private Image image;

   public Image getImage()
   {
      return this.image;
   }
   
   public void setImage(Image value)
   {
      if (this.image != value) {
      
    	 Image oldValue = this.image;
         this.image = value;
         this.firePropertyChange(PROPERTY_IMAGE, oldValue, value);
      }
   }

   public SimpleDrug withImage(Image value)
   {
      setImage(value);
      return this;
   }
   
   
   //==========================================================================
   
   public static final String PROPERTY_TAILOREDSUMMARY = "personalizedInformation";
   
   @Transient
   private String tailoredSummary;

   public String getTailoredSummary()
   {
      return this.tailoredSummary;
   }
   
   public void setTailoredSummary(String value)
   {
      if ( ! EntityUtil.stringEquals(this.tailoredSummary, value)) {
      
         String oldValue = this.tailoredSummary;
         this.tailoredSummary = value;
         this.firePropertyChange(PROPERTY_TAILOREDSUMMARY, oldValue, value);
      }
   }
   
   public SimpleDrug withTailoredSummary(String value)
   {
      setName(value);
      return this;
   } 
   
//==========================================================================
   
   public static final String PROPERTY_HALF_TIME_PERIOD = "period";
   
   @Column(name = "period")
   private int period;

   public int getPeriod()
   {
      return this.period;
   }
   
   public void setPeriod(int value)
   {
      if (this.period != value) {
      
         int oldValue = this.period;
         this.period = value;
         this.firePropertyChange(PROPERTY_HALF_TIME_PERIOD, oldValue, value);
      }
   }
   
   public SimpleDrug withPeriod(int value)
   {
      setPeriod(value);
      return this;
   }
   
//==========================================================================
   
   public static final String PROPERTY_TAKE_ON_EMPTY_STOMACH = "takeonemptystomach";
   
   @Column(name = "take_on_empty_stomach")
   private boolean takeOnEmptyStomach;

   public boolean getTakeOnEmptyStomach()
   {
      return this.takeOnEmptyStomach;
   }
   
   public void setTakeOnEmptyStomach(boolean value)
   {
      if (this.takeOnEmptyStomach != value) {
      
         boolean oldValue = this.takeOnEmptyStomach;
         this.takeOnEmptyStomach = value;
         this.firePropertyChange(PROPERTY_TAKE_ON_EMPTY_STOMACH, oldValue, value);
      }
   }
   
//   public SimpleDrug withTakeOnEmptyStomach(boolean value)
//   {
//	   setTakeOnEmptyStomach(value);
//      return this;
//   }

//==========================================================================
   
   public static final String PROPERTY_TAKE_ON_FULL_STOMACH = "takeonfullstomach";
   
   @Column(name = "take_on_full_stomach")
   private boolean takeOnFullStomach;

   public boolean getTakeOnFullStomach()
   {
      return this.takeOnFullStomach;
   }
   
   public void setTakeOnFullStomach(boolean value)
   {
      if (this.takeOnFullStomach != value) {
      
         boolean oldValue = this.takeOnFullStomach;
         this.takeOnFullStomach = value;
         this.firePropertyChange(PROPERTY_TAKE_ON_FULL_STOMACH, oldValue, value);
      }
   }
   
//   public SimpleDrug withTakeOnFullStomach(boolean value)
//   {
//	   setTakeOnFullStomach(value);
//      return this;
//   }
   

//==========================================================================
   
   public static final String PROPERTY_TAKE_TO_MEALS = "taketomeals";
   
   @Column(name = "take_to_meals")
   private boolean takeToMeals;

   public boolean getTakeToMeals()
   {
      return this.takeToMeals;
   }
   
   public void setTakeToMeals(boolean value)
   {
      if (this.takeToMeals != value) {
      
         boolean oldValue = this.takeToMeals;
         this.takeToMeals = value;
         this.firePropertyChange(PROPERTY_TAKE_TO_MEALS, oldValue, value);
      }
   }
   
//   public SimpleDrug withTakeToMeals(boolean value)
//   {
//	   setTakeToMeals(value);
//      return this;
//   }
   
//==========================================================================
   
   public static final String PROPERTY_COUNT_PER_DAY = "countperday";
   
   @Column(name = "count_per_day")
   private int countPerDay;

   public int getCountPerDay()
   {
      return this.countPerDay;
   }
   
   public void setCountPerDay(int value)
   {
      if (this.countPerDay != value) {
      
         int oldValue = this.countPerDay;
         this.countPerDay = value;
         this.firePropertyChange(PROPERTY_COUNT_PER_DAY, oldValue, value);
      }
   }
   

}


