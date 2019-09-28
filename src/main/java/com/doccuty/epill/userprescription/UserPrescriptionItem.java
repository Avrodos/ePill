package com.doccuty.epill.userprescription;

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
 * UserPrescriptionItem contains prescription items contains intake times 
 * 
 * @author cs
 *
 */
@Entity
@Table(name="user_prescription_item")
public class UserPrescriptionItem implements SendableEntity {

    public UserPrescriptionItem() {

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

    public UserPrescriptionItem withId(long value)
    {
        setId(value);
        return this;
    }

    //Planned intake time==========================================================================
    public static final String PROPERTY_INTAKE_TIME = "intake_time";

	@Column(nullable = false)
	private int intakeTime;

    public int getIntakeTime()
    {
        return this.intakeTime;
    }

    public void setIntakeTime(int value)
    {
            int oldValue = this.intakeTime;
            this.intakeTime = value;
            this.firePropertyChange(PROPERTY_INTAKE_TIME, oldValue, value);
    }

    /********************************************************************
     * <pre>
     *              many                       one
     * UserPrescriptionItem---------------------------------- UserPrescription
     *              userPrescriptionItems                userPrescription
     * </pre>
     */

    public static final String PROPERTY_USER_PRESCRIPTION = "user_prescription";

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="id_user_prescription")
    private UserPrescription userPrescription = null;

    public UserPrescription getDrug()
    {
        return this.userPrescription;
    }

    public boolean setUserPrescription(UserPrescription value)
    {
        boolean changed = false;

        if (this.userPrescription != value)
        {
        	UserPrescription oldValue = this.userPrescription;

            if (this.userPrescription != null)
            {
                this.userPrescription = null;
                oldValue.withUserPrescriptionItems(this);
            }

            this.userPrescription = value;
            if (value != null)
            {
                value.withUserPrescriptionItems(this);
            }

            firePropertyChange(PROPERTY_USER_PRESCRIPTION, oldValue, value);
            changed = true;
        }

        return changed;
    }
    
	public UserPrescriptionItem withPrescription(UserPrescription userPrescription) {
		setUserPrescription(userPrescription);
		return this;
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
