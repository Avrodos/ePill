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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import de.uniks.networkparser.EntityUtil;
import de.uniks.networkparser.interfaces.SendableEntity;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "user_simple")
@Inheritance(strategy = InheritanceType.JOINED)
public class SimpleUser implements SendableEntity {

	private static final String PROPERTY_PREFERREDFONTSIZE_DEFAULT = "defaultFontSize";

	public SimpleUser() {

	}

	public SimpleUser(long id, String firstname, String lastname) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
	}

	//TODO: Clean this up.
	public SimpleUser(long id, String firstname, String lastname, String username, String password, String salt,
					  String preferredFontSize, int levelOfDetail, boolean redGreenColorblind, String gid, Boolean tpa, String a7id, Boolean firstSignIn, int weight) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.preferredFontSize = preferredFontSize;
		this.levelOfDetail = levelOfDetail;
		this.redGreenColorblind = redGreenColorblind;
		this.gid = gid;
		this.tpa = tpa;
		this.a7id = a7id;
		this.firstSignIn = firstSignIn;
        this.weight = weight;
	}

	public SimpleUser(User user) {
		this.id = user.getId();
		this.firstname = user.getFirstname();
		this.lastname = user.getLastname();
		this.dateOfBirth = user.getDateOfBirth();
		this.redGreenColorblind = user.getRedGreenColorblind();

		this.dateOfRegistration = user.getDateOfRegistration();

		this.username = user.getUsername();
		this.salt = user.getSalt();
		this.password = user.getPassword();

		this.email = user.getEmail();

		this.preferredFontSize = user.getPreferredFontSize();
		this.levelOfDetail = user.getLevelOfDetail();
		this.gid = user.getGid();
		this.tpa = user.getTPA();
		this.a7id = user.getA7id();
		this.firstSignIn = user.getFirstSignIn();
		this.weight = user.getWeight();
		this.breakfastTime = user.getBreakfastTime();
		this.dinnerTime = user.getDinnerTime();
		this.lunchTime = user.getLunchTime();
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

	@Override
	public boolean addPropertyChangeListener(PropertyChangeListener listener) {
		if (listeners == null) {
			listeners = new PropertyChangeSupport(this);
		}
		listeners.addPropertyChangeListener(listener);
		return true;
	}

	@Override
	public boolean addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		if (listeners == null) {
			listeners = new PropertyChangeSupport(this);
		}
		listeners.addPropertyChangeListener(propertyName, listener);
		return true;
	}

	@Override
	public boolean removePropertyChangeListener(PropertyChangeListener listener) {
		if (listeners == null) {
			listeners.removePropertyChangeListener(listener);
		}
		listeners.removePropertyChangeListener(listener);
		return true;
	}

	@Override
	public boolean removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		if (listeners != null) {
			listeners.removePropertyChangeListener(propertyName, listener);
		}
		return true;
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
			this.id = value;
		}
	}

	public SimpleUser withId(long value) {
		setId(value);
		return this;
	}

	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();

		result.append(" ").append(this.getId());
		result.append(" ").append(this.getFirstname());
		result.append(" ").append(this.getLastname());
		result.append(" ").append(this.getUsername());
		result.append(" ").append(this.getPassword());
		result.append(" ").append(this.getEmail());
		result.append(" ").append(this.getPreferredFontSize());
		result.append(" ").append(this.getGid());
		result.append(" ").append(this.getTPA());
		result.append(" ").append(this.getA7id());
        result.append(" ").append(this.getWeight());
		return result.substring(1);
	}


	// ==========================================================================

	public static final String PROPERTY_GID = "gid";

	@Column(unique = true)
	private String gid;

	public String getGid() {
		return this.gid;
	}

	public void setGid(String value) {
		if (!EntityUtil.stringEquals(this.gid, value)) {
			this.gid = value;
		}
	}

	public SimpleUser withGid(String value) {
		setGid(value);
		return this;
	}

	// ==========================================================================

	public static final String PROPERTY_A7ID = "a7id";

	@Column(unique = true)
	private String a7id;

	public String getA7id() {
		return this.a7id;
	}

	public void setA7id(String value) {
		if (!EntityUtil.stringEquals(this.a7id, value)) {
			this.a7id = value;
		}
	}

	public SimpleUser withA7id(String value) {
		setA7id(value);
		return this;
	}

	// ==========================================================================

	public static final String PROPERTY_TPA = "tpa";

	@Column
	private Boolean tpa;

	public Boolean getTPA() {
		return this.tpa;
	}

	public void setTPA(Boolean value) {
		if (this.tpa!= value) {
			Boolean oldValue = this.tpa;
			this.tpa = value;
			this.firePropertyChange(PROPERTY_TPA, oldValue, value);
            //TODO: Brauche ich das hier? Brauche ich das bei den anderen?
		}
	}

	public SimpleUser withTPA(Boolean value) {
		setTPA(value);
		return this;
	}

	// ==========================================================================

	public static final String PROPERTY_FIRSTNAME = "firstname";

	private String firstname;

	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String value) {
		if (!EntityUtil.stringEquals(this.firstname, value)) {
			this.firstname = value;
		}
	}

	public SimpleUser withFirstname(String value) {
		setFirstname(value);
		return this;
	}

	// ==========================================================================

	public static final String PROPERTY_LASTNAME = "lastname";

	private String lastname;

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String value) {
		if (!EntityUtil.stringEquals(this.lastname, value)) {

			this.lastname = value;
		}
	}

	public SimpleUser withLastname(String value) {
		setLastname(value);
		return this;
	}

	// ==========================================================================

	public static final String PROPERTY_USERNAME = "username";

	@Column(unique = true)
	private String username;

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String value) {
		if (!EntityUtil.stringEquals(this.username, value)) {
			this.username = value;
		}
	}

	public SimpleUser withUsername(String value) {
		setUsername(value);
		return this;
	}

	// ==========================================================================

	public static final String PROPERTY_PASSWORD = "password";

	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String value) {
		if (!EntityUtil.stringEquals(this.password, value)) {
			this.password = value;
		}
	}

	public SimpleUser withPassword(String value) {
		setPassword(value);
		return this;
	}

	// ==========================================================================

	public static final String PROPERTY_SALT = "salt";

	@JsonIgnore
	private String salt;

	public String getSalt() {
		return this.salt;
	}

	public void setSalt(String value) {
		if (!EntityUtil.stringEquals(this.salt, value)) {

			final String oldValue = this.salt;
			this.salt = value;
			this.firePropertyChange(PROPERTY_SALT, oldValue, value);
		}
	}

	public SimpleUser withSalt(String value) {
		setSalt(value);
		return this;
	}

	// ==========================================================================

	public static final String PROPERTY_EMAIL = "email";

	@Column
	private String email;

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String value) {
		if (!EntityUtil.stringEquals(this.email, value)) {
			this.email = value;
		}
	}

	public SimpleUser withEmail(String value) {
		setEmail(value);
		return this;
	}

	// ==========================================================================

	public static final String PROPERTY_DATEOFBIRTH = "dateOfBirth";

	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;

	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date value) {
		if (this.dateOfBirth != value) {
			this.dateOfBirth = value;
		}
	}

	public SimpleUser withDateOfBirth(Date value) {
		setDateOfBirth(value);
		return this;
	}

	@JsonIgnore
	public int getAge() {

		if (this.dateOfBirth == null) {
			return 0;
		}

		final Calendar cal = Calendar.getInstance();
		cal.setTime(this.dateOfBirth);

		final LocalDate b = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH));
		final long age = b.until(LocalDate.now(), ChronoUnit.YEARS);

		return (int) age;
	}

	// ==========================================================================

	public static final String PROPERTY_PREFERREDFONTSIZE = "preferredFontSize";

	private String preferredFontSize = PROPERTY_PREFERREDFONTSIZE_DEFAULT;

	public String getPreferredFontSize() {
		return this.preferredFontSize;
	}

	public void setPreferredFontSize(String value) {
		if (this.preferredFontSize != value) {
			this.preferredFontSize = value;
		}
	}

	public SimpleUser withPreferredFontSize(String value) {
		setPreferredFontSize(value);
		return this;
	}

	// ==========================================================================

	public static final String PROPERTY_LEVELOFDETAIL = "levelOfDetail";

	private int levelOfDetail;

	public int getLevelOfDetail() {
		return this.levelOfDetail;
	}

	public void setLevelOfDetail(int value) {
		if (this.levelOfDetail != value) {
			final int oldValue = this.levelOfDetail;
			this.levelOfDetail = value;
			this.firePropertyChange(PROPERTY_LEVELOFDETAIL, oldValue, value);
		}
	}

	public SimpleUser withLevelOfDetail(int value) {
		setLevelOfDetail(value);
		return this;
	}

	// ==========================================================================

	public static final String PROPERTY_DATEOFREGISTRATION = "dateOfRegistration";

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private Date dateOfRegistration;

	public Date getDateOfRegistration() {
		return this.dateOfRegistration;
	}

	public void setDateOfRegistration(Date value) {
		if (this.dateOfRegistration != value) {
			this.dateOfRegistration = value;
		}
	}

	public SimpleUser withDateOfRegistration(Date value) {
		setDateOfRegistration(value);
		return this;
	}

	// ==========================================================================

	public static final String PROPERTY_REDGREENCOLORBLIND = "redGreenColorblind";

	private boolean redGreenColorblind = false;

	public boolean getRedGreenColorblind() {
		return this.redGreenColorblind;
	}

	public void setRedGreenColorblind(boolean value) {
		if (this.redGreenColorblind != value) {
			final boolean oldValue = this.redGreenColorblind;
			this.redGreenColorblind = value;
			this.firePropertyChange(PROPERTY_REDGREENCOLORBLIND, oldValue, value);
		}
	}

	public SimpleUser withRedGreenColorblind(boolean value) {
		setRedGreenColorblind(value);
		return this;
	}

	// ==========================================================================

	//This aids us identifying the first sign in of the user
	//Needed for e.g., red-green colorblindness
	public static final String PROPERTY_FIRSTSIGNIN = "firstSignIn";

	@Column
	private Boolean firstSignIn;

	public Boolean getFirstSignIn() {
		return this.firstSignIn;
	}

	public void setFirstSignIn(Boolean value) {
		if (this.firstSignIn != value) {
			Boolean oldValue = this.firstSignIn;
			this.firstSignIn = value;
			this.firePropertyChange(PROPERTY_FIRSTSIGNIN, oldValue, value);
		}
	}

	public SimpleUser withFirstSignIn(Boolean value) {
		setFirstSignIn(value);
		return this;
	}


	// ==========================================================================

	public static final String PROPERTY_WEIGHT = "weight";

	@Column(nullable = false, columnDefinition = "int default 0")
	private int weight;

	public int getWeight() {
		return this.weight;
	}

	public void setWeight(int value) {
		if (this.weight != value) {
			final int oldValue = this.weight;
			this.weight = value;
			this.firePropertyChange(PROPERTY_WEIGHT, oldValue, value);
		}
	}

	public SimpleUser withWeight(int value) {
		setWeight(value);
		return this;
	}

	// ==========================================================================

	public static final String PROPERTY_BREAKFAST_TIME = "breakfasttime";

	@Column(name = "breakfast_time")
	private int breakfastTime;

	public int getBreakfastTime() {
		return this.breakfastTime;
	}

	public void setBreakfastTime(int value) {
		if (this.breakfastTime != value) {
			final int oldValue = this.breakfastTime;
			this.breakfastTime = value;
			this.firePropertyChange(PROPERTY_BREAKFAST_TIME, oldValue, value);
		}
	}

	public SimpleUser withBreakfastTime(int value) {
		setBreakfastTime(value);
		return this;
	}

	// ==========================================================================

	public static final String PROPERTY_LUNCH_TIME = "lunchtime";

	@Column(name = "lunch_time")
	private int lunchTime;

	public int getLunchTime() {
		return this.lunchTime;
	}

	public void setLunchTime(int value) {
		if (this.lunchTime != value) {
			final int oldValue = this.lunchTime;
			this.lunchTime = value;
			this.firePropertyChange(PROPERTY_LUNCH_TIME, oldValue, value);
		}
	}

	public SimpleUser withLunchTime(int value) {
		setLunchTime(value);
		return this;
	}

	// ==========================================================================

	public static final String PROPERTY_DINNER_TIME = "dinnertime";

	@Column(name = "dinner_time")
	private int dinnerTime;

	public int getDinnerTime() {
		return this.dinnerTime;
	}

	public void setDinnerTime(int value) {
		if (this.dinnerTime != value) {
			final int oldValue = this.dinnerTime;
			this.dinnerTime = value;
			this.firePropertyChange(PROPERTY_DINNER_TIME, oldValue, value);
		}
	}

	public SimpleUser withDinnerTime(int value) {
		setDinnerTime(value);
		return this;
	}

    // ==========================================================================

    public static final String PROPERTY_SMOKER = "smoker";

    @Column
    private Boolean smoker;

    public Boolean getSmoker() {
        return this.smoker;
    }

    public void setSmoker(Boolean value) {
        if (this.smoker != value) {
            final Boolean oldValue = this.smoker;
            this.smoker = value;
            this.firePropertyChange(PROPERTY_SMOKER, oldValue, value);
        }
    }

    public SimpleUser withSmoker(Boolean value) {
        setSmoker(value);
        return this;
    }

}
