package com.doccuty.epill.diabetes;


import com.doccuty.epill.disease.Disease;
import com.doccuty.epill.drug.Drug;
import com.doccuty.epill.model.util.DiseaseSet;
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
@Table(name = "diabetes")
public class Diabetes implements SendableEntity {


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

    public Diabetes withId(int value) {
        setId(value);
        return this;
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(" ").append(this.getId());
        result.append(" ").append(this.getDiabetes());
        return result.substring(1);
    }


    //==========================================================================

    public static final String PROPERTY_DIABETES = "diabetes";

    @Column(unique = true)
    private String diabetes;

    public String getDiabetes() {
        return this.diabetes;
    }

    public void setDiabetes(String value) {
        if (!EntityUtil.stringEquals(this.diabetes, value)) {

            String oldValue = this.diabetes;
            this.diabetes = value;
            this.firePropertyChange(PROPERTY_DIABETES, oldValue, value);
        }
    }

    public Diabetes withDiabetes(String value) {
        setDiabetes(value);
        return this;
    }


    /********************************************************************
     * <pre>
     *              one                       many
     * Diabetes ----------------------------------- User
     *              diabetes                   user
     * </pre>
     */

    public static final String PROPERTY_USER = "user";

    @OneToMany(mappedBy = "diabetes", cascade = {CascadeType.ALL, CascadeType.MERGE, CascadeType.PERSIST})
    private Set<User> user = null;

    public Set<User> getUser() {
        if (this.user == null) {
            return UserSet.EMPTY_SET;
        }

        return this.user;
    }

    public Diabetes withUser(User... value) {
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
                    item.withDiabetes(this);
                    firePropertyChange(PROPERTY_USER, null, item);
                }
            }
        }
        return this;
    }

    public Diabetes withoutUser(User... value) {
        for (User item : value) {
            if ((this.user != null) && (item != null)) {
                if (this.user.remove(item)) {
                    item.setDiabetes(null);
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

    /********************************************************************
     * <pre>
     *              many                       many
     * Diabetes ----------------------------------- Disease
     *              diabetes                   disease
     * </pre>
     */

    public static final String PROPERTY_DISEASE = "disease";

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "diabetes_disease", joinColumns = @JoinColumn(name = "iddiabetes"), inverseJoinColumns = @JoinColumn(name = "iddisease"))
    private Set<Disease> disease = null;

    public Set<Disease> getDisease() {
        if (this.disease == null) {
            return DiseaseSet.EMPTY_SET;
        }

        return this.disease;
    }

    public Diabetes withDisease(Disease... value) {
        if (value == null) {
            return this;
        }
        for (Disease item : value) {
            if (item != null) {
                if (this.disease == null) {
                    this.disease = new DiseaseSet();
                }

                boolean changed = this.disease.add(item);

                if (changed) {
                    item.withDiabetes(this);
                    firePropertyChange(PROPERTY_DISEASE, null, item);
                }
            }
        }
        return this;
    }

    public Diabetes withoutDisease(Disease... value) {
        for (Disease item : value) {
            if ((this.disease != null) && (item != null)) {
                if (this.disease.remove(item)) {
                    item.withoutDiabetes(this);
                    firePropertyChange(PROPERTY_DISEASE, item, null);
                }
            }
        }
        return this;
    }

    public Disease createDisease() {
        Disease value = new Disease();
        withDisease(value);
        return value;
    }


    /********************************************************************
     * <pre>
     *              many                       many
     * Diabetes ----------------------------------- Disease
     *              diabetes                   disease
     * </pre>
     */

    public static final String PROPERTY_DRUG = "drug";

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "diabetes_drug", joinColumns = @JoinColumn(name = "iddiabetes"), inverseJoinColumns = @JoinColumn(name = "iddrug"))
    private Set<Drug> drug = null;

    public Set<Drug> getDrug() {
        if (this.drug == null) {
            return DrugSet.EMPTY_SET;
        }

        return this.drug;
    }

    public Diabetes withDrug(Drug... value) {
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
                    item.withDiabetes(this);
                    firePropertyChange(PROPERTY_DRUG, null, item);
                }
            }
        }
        return this;
    }

    public Diabetes withoutDrug(Drug... value) {
        for (Drug item : value) {
            if ((this.drug != null) && (item != null)) {
                if (this.drug.remove(item)) {
                    item.withoutDiabetes(this);
                    firePropertyChange(PROPERTY_DRUG, item, null);
                }
            }
        }
        return this;
    }
}

