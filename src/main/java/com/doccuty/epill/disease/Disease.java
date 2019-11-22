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
   
package com.doccuty.epill.disease;

import com.doccuty.epill.diabetes.Diabetes;
import com.doccuty.epill.drug.Drug;
import com.doccuty.epill.gender.Gender;
import com.doccuty.epill.model.util.DiabetesSet;
import com.doccuty.epill.model.util.DrugSet;
import com.doccuty.epill.model.util.GenderSet;
import com.doccuty.epill.model.util.UserSet;
import com.doccuty.epill.user.User;
import de.uniks.networkparser.EntityUtil;
import de.uniks.networkparser.interfaces.SendableEntity;

import javax.persistence.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Set;
   /**
    * 
    * @see <a href='../../../../../../../src/test/java/com/doccuty/epill/model/SDMLib/ModelCreator.java'>ModelCreator.java</a>
 */
@Entity
@Table(name="disease")
   public  class Disease implements SendableEntity
{

   
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
      withoutDrug(this.getDrug().toArray(new Drug[this.getDrug().size()]));
      withoutUser(this.getUser().toArray(new User[this.getUser().size()]));
      firePropertyChange("REMOVE_YOU", this, null);
   }

   
   //==========================================================================
   
   public static final String PROPERTY_ID = "id";
   
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)
   private int id;

   public int getId()
   {
      return this.id;
   }
   
   public void setId(int value)
   {
      if (this.id != value) {
      
         int oldValue = this.id;
         this.id = value;
         this.firePropertyChange(PROPERTY_ID, oldValue, value);
      }
   }
   
   public Disease withId(int value)
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
      return result.substring(1);
   }


   
   //==========================================================================
   
   public static final String PROPERTY_NAME = "name";
   
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
   
   public Disease withName(String value)
   {
      setName(value);
      return this;
   } 

   
   /********************************************************************
    * <pre>
    *              many                       many
    * Disease ----------------------------------- Drug
    *              disease                   drug
    * </pre>
    */
   
   public static final String PROPERTY_DRUG = "drug";

   @ManyToMany(cascade=CascadeType.ALL, mappedBy="disease")
   private Set<Drug> drug = null;

   public Set<Drug> getDrug()
   {
      if (this.drug == null)
      {
         return DrugSet.EMPTY_SET;
      }
   
      return this.drug;
   }

   public Disease withDrug(Drug... value)
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
               item.withDisease(this);
               firePropertyChange(PROPERTY_DRUG, null, item);
            }
         }
      }
      return this;
   } 

   public Disease withoutDrug(Drug... value)
   {
      for (Drug item : value)
      {
         if ((this.drug != null) && (item != null))
         {
            if (this.drug.remove(item))
            {
               item.withoutDisease(this);
               firePropertyChange(PROPERTY_DRUG, item, null);
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

   
   /********************************************************************
    * <pre>
    *              many                       many
    * Disease ----------------------------------- User
    *              disease                   user
    * </pre>
    */
   
   public static final String PROPERTY_USER = "user";

   @ManyToMany(cascade=CascadeType.ALL, mappedBy="disease")
   private Set<User> user = null;
   
   public Set<User> getUser()
   {
      if (this.user == null)
      {
         return UserSet.EMPTY_SET;
      }
   
      return this.user;
   }

   public Disease withUser(User... value)
   {
      if(value==null){
         return this;
      }
      for (User item : value)
      {
         if (item != null)
         {
            if (this.user == null)
            {
               this.user = new UserSet();
            }
            
            boolean changed = this.user.add (item);

            if (changed)
            {
               item.withDisease(this);
               firePropertyChange(PROPERTY_USER, null, item);
            }
         }
      }
      return this;
   } 

   public Disease withoutUser(User... value)
   {
      for (User item : value)
      {
         if ((this.user != null) && (item != null))
         {
            if (this.user.remove(item))
            {
               item.withoutDisease(this);
               firePropertyChange(PROPERTY_USER, item, null);
            }
         }
      }
      return this;
   }

   public User createUser()
   {
      User value = new User();
      withUser(value);
      return value;
   } 
   
   
   
   /********************************************************************
    * <pre>
    *              many                       many
    * Disease ----------------------------------- Gender
    *              disease                   gender
    * </pre>
    */
   
   public static final String PROPERTY_GENDER = "gender";

   @ManyToMany(cascade=CascadeType.ALL, mappedBy="disease")
   private Set<Gender> gender = null;
   
   public Set<Gender> getGender()
   {
      if (this.gender == null)
      {
         return GenderSet.EMPTY_SET;
      }
   
      return this.gender;
   }

   public Disease withGender(Gender... value)
   {
      if(value==null){
         return this;
      }
      for (Gender item : value)
      {
         if (item != null)
         {
            if (this.gender == null)
            {
               this.gender = new GenderSet();
            }
            
            boolean changed = this.gender.add (item);

            if (changed)
            {
               item.withDisease(this);
               firePropertyChange(PROPERTY_GENDER, null, item);
            }
         }
      }
      return this;
   } 

   public Disease withoutGender(Gender... value)
   {
      for (Gender item : value)
      {
         if ((this.gender != null) && (item != null))
         {
            if (this.gender.remove(item))
            {
               item.withoutDisease(this);
               firePropertyChange(PROPERTY_GENDER, item, null);
            }
         }
      }
      return this;
   }

   public Gender createGender()
   {
      Gender value = new Gender();
      withGender(value);
      return value;
   }

    /********************************************************************
     * <pre>
     *              many                       many
     * Disease ----------------------------------- Diabetes
     *              disease                   diabetes
     * </pre>
     */

    public static final String PROPERTY_DIABETES = "diabetes";

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "disease")
    private Set<Diabetes> diabetes = null;

    public Set<Diabetes> getDiabetes() {
        if (this.diabetes == null) {
            return DiabetesSet.EMPTY_SET;
        }

        return this.diabetes;
    }

    public Disease withDiabetes(Diabetes... value) {
        if (value == null) {
            return this;
        }
        for (Diabetes item : value) {
            if (item != null) {
                if (this.diabetes == null) {
                    this.diabetes = new DiabetesSet();
                }

                boolean changed = this.diabetes.add(item);

                if (changed) {
                    item.withDisease(this);
                    firePropertyChange(PROPERTY_DIABETES, null, item);
                }
            }
        }
        return this;
    }

    public Disease withoutDiabetes(Diabetes... value) {
        for (Diabetes item : value) {
            if ((this.diabetes != null) && (item != null)) {
                if (this.diabetes.remove(item)) {
                    item.withoutDisease(this);
                    firePropertyChange(PROPERTY_DIABETES, item, null);
                }
            }
        }
        return this;
    }

    public Diabetes createDiabetes() {
        Diabetes value = new Diabetes();
        withDiabetes(value);
        return value;
    }
   
}
