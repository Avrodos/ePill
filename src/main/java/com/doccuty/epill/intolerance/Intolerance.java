package com.doccuty.epill.intolerance;

import com.doccuty.epill.drug.Drug;
import com.doccuty.epill.model.util.DrugSet;
import com.doccuty.epill.model.util.UserSet;
import com.doccuty.epill.user.User;
import de.uniks.networkparser.EntityUtil;
import de.uniks.networkparser.interfaces.SendableEntity;

import javax.persistence.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Set;

@Entity
@Table(name = "intolerance")
public class Intolerance implements SendableEntity {

    //==========================================================================

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


    //==========================================================================

    public void removeYou() {
        withoutDrug(this.getDrug().toArray(new Drug[this.getDrug().size()]));
        withoutUser(this.getUser().toArray(new User[this.getUser().size()]));
        firePropertyChange("REMOVE_YOU", this, null);
    }


    //==========================================================================


    public static final String PROPERTY_ID = "id";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public int getId() {
        return this.id;
    }

    public void setId(int value) {
        if (this.id != value) {

            int oldValue = this.id;
            this.id = value;
            this.firePropertyChange(PROPERTY_ID, oldValue, value);
        }
    }

    public Intolerance withId(int value) {
        setId(value);
        return this;
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(" ").append(this.getId());
        result.append(" ").append(this.getName());
        return result.substring(1);
    }


    //==========================================================================


    public static final String PROPERTY_NAME = "name";

    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        if (!EntityUtil.stringEquals(this.name, value)) {

            String oldValue = this.name;
            this.name = value;
            this.firePropertyChange(PROPERTY_NAME, oldValue, value);
        }
    }

    public Intolerance withName(String value) {
        setName(value);
        return this;
    }

    /********************************************************************
     * <pre>
     *              many                       many
     * Intolerance ----------------------------------- Drug
     *              intolerance                   drug
     * </pre>
     */

    public static final String PROPERTY_DRUG = "drug";

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "intolerance")
    private Set<Drug> drug = null;

    public Set<Drug> getDrug() {
        if (this.drug == null) {
            return DrugSet.EMPTY_SET;
        }

        return this.drug;
    }

    public Intolerance withDrug(Drug... value) {
        if (value == null) {
            return this;
        }
        for (Drug item : value) {
            if (item != null) {
                if (this.drug == null) {
                    this.drug = new DrugSet();
                }

                boolean changed = this.drug.add(item);

                if (changed) {
                    item.withIntolerance(this);
                    firePropertyChange(PROPERTY_DRUG, null, item);
                }
            }
        }
        return this;
    }

    public Intolerance withoutDrug(Drug... value) {
        for (Drug item : value) {
            if ((this.drug != null) && (item != null)) {
                if (this.drug.remove(item)) {
                    item.withoutIntolerance(this);
                    firePropertyChange(PROPERTY_DRUG, item, null);
                }
            }
        }
        return this;
    }

    public Drug createDrug() {
        Drug value = new Drug();
        withDrug(value);
        return value;
    }

    /********************************************************************
     * <pre>
     *              many                       many
     * Intolerance ----------------------------------- User
     *              intolerance                   user
     * </pre>
     */

    public static final String PROPERTY_USER = "user";

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "intolerance")
    private Set<User> user = null;

    public Set<User> getUser() {
        if (this.user == null) {
            return UserSet.EMPTY_SET;
        }

        return this.user;
    }

    public Intolerance withUser(User... value) {
        if (value == null) {
            return this;
        }
        for (User item : value) {
            if (item != null) {
                if (this.user == null) {
                    this.user = new UserSet();
                }

                boolean changed = this.user.add(item);

                if (changed) {
                    item.withIntolerance(this);
                    firePropertyChange(PROPERTY_USER, null, item);
                }
            }
        }
        return this;
    }

    public Intolerance withoutUser(User... value) {
        for (User item : value) {
            if ((this.user != null) && (item != null)) {
                if (this.user.remove(item)) {
                    item.withoutIntolerance(this);
                    firePropertyChange(PROPERTY_USER, item, null);
                }
            }
        }
        return this;
    }

    public User createUser() {
        User value = new User();
        withUser(value);
        return value;
    }

}
