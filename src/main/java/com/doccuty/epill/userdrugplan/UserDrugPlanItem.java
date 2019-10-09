package com.doccuty.epill.userdrugplan;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.doccuty.epill.drug.Drug;
import com.doccuty.epill.model.LoginAttempt;
import com.doccuty.epill.user.User;

import de.uniks.networkparser.interfaces.SendableEntity;

/**
 * UserDrugPlanItem contains plan data (for future intakes) and historical data 
 * (for intakes in the past)  per intake of one drug. 
 * 
 * @author cs
 *
 */
@Entity
@Table(name="user_drug_plan_item")
public class UserDrugPlanItem implements SendableEntity {

    public UserDrugPlanItem() {

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

            double oldValue = this.id;
            this.id = value;
            this.firePropertyChange(PROPERTY_ID, oldValue, value);
        }
    }

    public UserDrugPlanItem withId(long value)
    {
        setId(value);
        return this;
    }


    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        result.append(" ").append(this.getId()).append(" ").append(this.getDrug());
        return result.substring(1);
    }

    //Planned Timestamp for drug taking==========================================================================
    public static final String PROPERTY_DATETIME_INTAKE_PLANNED = "datetime_intake_planned";

    @Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date dateTimePlanned;

    public Date getDateTimePlanned()
    {
        return this.dateTimePlanned;
    }

    public void setDateTimePlanned(Date value)
    {
        if (this.dateTimePlanned != value) {
            Date oldValue = this.dateTimePlanned;
            this.dateTimePlanned = value;
            this.firePropertyChange(PROPERTY_DATETIME_INTAKE_PLANNED, oldValue, value);
        }
    }

    public UserDrugPlanItem withDateTimePlanned(Date value)
    {
        setDateTimePlanned(value);
        return this;
    }

    //Planned Timestamp for drug taking==========================================================================
    public static final String PROPERTY_DATETIME_INTAKE = "datetime_intake";

    @Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date dateTimeIntake;

    public Date getDateTimeIntake()
    {
        return this.dateTimeIntake;
    }

    public void setDateTimeIntake(Date value)
    {
        if (this.dateTimeIntake != value) {
            Date oldValue = this.dateTimeIntake;
            this.dateTimeIntake = value;
            this.firePropertyChange(PROPERTY_DATETIME_INTAKE, oldValue, value);
        }
    }

    public UserDrugPlanItem withDateTimeIntake(Date value)
    {
        setDateTimeIntake(value);
        return this;
    }
    
    //indicates if drug is taken or not==========================================================================
    public static final String PROPERTY_DRUG_TAKEN = "drug_taken";
    private boolean drugTaken;	

    public boolean getDrugTaken()
    {
        return this.drugTaken;
    }

    public void setDrugTaken(boolean drugTaken)
    {
        this.drugTaken = drugTaken;
    }

    public UserDrugPlanItem withDrugTaken(boolean drugTaken)
    {
        this.setDrugTaken(drugTaken);
        return this;
    }
    
    //==========================================================================

    /********************************************************************
     * <pre>
     *              many                       one
     * UserDrugPlanItem ----------------------------------- Drug
     *              userDrugPlanItems                   drug
     * </pre>
     */

    public static final String PROPERTY_DRUG = "drug";

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="iddrug")
    private Drug drug = null;

    public Drug getDrug()
    {
        return this.drug;
    }

    public boolean setDrug(Drug value)
    {
        boolean changed = false;

        if (this.drug != value)
        {
            Drug oldValue = this.drug;

            if (this.drug != null)
            {
                this.drug = null;
                oldValue.withUserDrugPlanItems(this);
            }

            this.drug = value;
            if (value != null)
            {
                value.withUserDrugPlanItems(this);
            }

            firePropertyChange(PROPERTY_DRUG, oldValue, value);
            changed = true;
        }

        return changed;
    }

    public UserDrugPlanItem withDrug(Drug value)
    {
        setDrug(value);
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
     *              many                       one
     * UserDrugPlanItem ----------------------------------- User
     *              userDrugPlanItems                   user
     * </pre>
     */
    public static final String PROPERTY_USER = "user";
    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="iduser")
    private User user = null;

    public User getUser()
    {
        return this.user;
    }

    public boolean setUser(User value)
    {
        boolean changed = false;

        if (this.user != value)
        {
            User oldValue = this.user;
            if (this.user != null)
            {
                this.user = null;
                oldValue.withoutUserDrugPlanItems(this);
            }
            this.user = value;
            if (value != null)
            {
                value.withoutUserDrugPlanItems(this);
            }

            firePropertyChange(PROPERTY_USER, oldValue, value);
            changed = true;
        }

        return changed;
    }

    public UserDrugPlanItem withUser(User value)
    {
        setUser(value);
        return this;
    }

    //==========================================================================


    public void removeYou()
    {
        setDrug(null);
        setUser(null);
        setDateTimePlanned(null);
        setDateTimeIntake(null);
        firePropertyChange("REMOVE_YOU", this, null);
    }


    //TODO: listener really necessary here?==========================================================================
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
}
