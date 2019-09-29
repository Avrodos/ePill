package com.doccuty.epill.userprescription;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.doccuty.epill.drug.Drug;
import com.doccuty.epill.model.LoginAttempt;
import com.doccuty.epill.user.User;

import de.uniks.networkparser.interfaces.SendableEntity;

/**
 * UserPrescription contains prescription information for user
 * 
 * @author cs
 *
 */
@Entity
@Table(name="user_prescription")
public class UserPrescription implements SendableEntity {

    public UserPrescription() {

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

    public UserPrescription withId(long value)
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
    public static final String PROPERTY_PERIOD_IN_DAYS = "period_in_days";

	@Column(nullable = false)
	private int periodInDays;

    public int getPeriodInDays()
    {
        return this.periodInDays;
    }

    public void setPeriodInDays(int value)
    {
            int oldValue = this.periodInDays;
            this.periodInDays = value;
            this.firePropertyChange(PROPERTY_PERIOD_IN_DAYS, oldValue, value);
    }

    public UserPrescription withPeriodInDays(int value)
    {
        setPeriodInDays(value);
        return this;
    }
    
    //==========================================================================

    /********************************************************************
     * <pre>
     *              many                       one
     * UserPrescription ----------------------------------- Drug
     *              userPrescriptions                   drug
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
                oldValue.withUserPrescriptions(this);
            }

            this.drug = value;
            if (value != null)
            {
                value.withUserPrescriptions(this);
            }

            firePropertyChange(PROPERTY_DRUG, oldValue, value);
            changed = true;
        }

        return changed;
    }

    public UserPrescription withDrug(Drug value)
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
     * UserPrescription ----------------------------------- User
     *              userPrescriptions                   user
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
                oldValue.withUserPrescriptions(this);
            }
            this.user = value;
            if (value != null)
            {
                value.withUserPrescriptions(this);
            }

            firePropertyChange(PROPERTY_USER, oldValue, value);
            changed = true;
        }

        return changed;
    }

    public UserPrescription withUser(User value)
    {
        setUser(value);
        return this;
    }

    //==========================================================================


    public void removeYou()
    {
        setDrug(null);
        setUser(null);
        setPeriodInDays(0);
        firePropertyChange("REMOVE_YOU", this, null);
    }

	/********************************************************************
	 * <pre>
	 *              one                       many
	 * UserPrescription ----------------------------------- UserPrescriptionItem
	 *              userPrescription                   userPrescriptionItems
	 * </pre>
	 */

	public static final String PROPERTY_USER_PRESCRIPTION_ITEMS = "user_prescription_items";

	@OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "userPrescription")
	private Set<UserPrescriptionItem> userPrescriptionItems = null;

	public Set<UserPrescriptionItem> getUserPrescriptionItems() {
		if (this.userPrescriptionItems == null) {
			return new HashSet<>();
		}
		return this.userPrescriptionItems;
	}

	public UserPrescription withUserPrescriptionItems(UserPrescriptionItem... value) {
		if (value == null) {
			return this;
		}
		for (final UserPrescriptionItem item : value) {
			if (item != null) {
				if (this.userPrescriptionItems == null) {
					this.userPrescriptionItems = new HashSet<>();
				}

				final boolean changed = this.userPrescriptionItems.add(item);

				if (changed) {
					item.withPrescription(this);
					firePropertyChange(PROPERTY_USER_PRESCRIPTION_ITEMS, null, item);
				}
			}
		}
		return this;
	}

	public UserPrescription withoutPrescriptionItems(UserPrescriptionItem... values) {
		for (final UserPrescriptionItem item : values) {
			if ((this.userPrescriptionItems != null) && (item != null)) {
				if (this.userPrescriptionItems.remove(item)) {
					item.setUserPrescription(null);
					firePropertyChange(PROPERTY_USER_PRESCRIPTION_ITEMS, item, null);
				}
			}
		}
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
