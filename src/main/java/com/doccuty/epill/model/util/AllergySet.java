package com.doccuty.epill.model.util;


import com.doccuty.epill.allergy.Allergy;
import com.doccuty.epill.drug.Drug;
import com.doccuty.epill.user.User;
import de.uniks.networkparser.list.NumberList;
import de.uniks.networkparser.list.ObjectSet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class AllergySet extends HashSet<Allergy> {
    protected Class<?> getTypClass() {
        return Allergy.class;
    }

    public AllergySet() {
        // empty
    }

    public AllergySet(Allergy... objects) {
        for (Allergy obj : objects) {
            this.add(obj);
        }
    }

    public AllergySet(Collection<Allergy> objects) {
        this.addAll(objects);
    }

    public static final AllergySet EMPTY_SET = new AllergySet();


    public String getEntryType() {
        return "com.doccuty.epill.model.Allergy";
    }


    @SuppressWarnings("unchecked")
    public AllergySet with(Object value) {
        if (value == null) {
            return this;
        } else if (value instanceof java.util.Collection) {
            this.addAll((Collection<Allergy>) value);
        } else if (value != null) {
            this.add((Allergy) value);
        }

        return this;
    }

    public AllergySet without(Allergy value) {
        this.remove(value);
        return this;
    }


    /**
     * Loop through the current set of Allergy objects and collect a list of the id attribute values.
     *
     * @return List of int objects reachable via id attribute
     */
    public NumberList getId() {
        NumberList result = new NumberList();

        for (Allergy obj : this) {
            result.add(obj.getId());
        }

        return result;
    }


    /**
     * Loop through the current set of Allergy objects and collect those Allergy objects where the id attribute matches the parameter value.
     *
     * @param value Search value
     * @return Subset of Allergy objects that match the parameter
     */
    public AllergySet filterId(int value) {
        AllergySet result = new AllergySet();

        for (Allergy obj : this) {
            if (value == obj.getId()) {
                result.add(obj);
            }
        }

        return result;
    }


    /**
     * Loop through the current set of Allergy objects and collect those Allergy objects where the id attribute is between lower and upper.
     *
     * @param lower Lower bound
     * @param upper Upper bound
     * @return Subset of Allergy objects that match the parameter
     */
    public AllergySet filterId(int lower, int upper) {
        AllergySet result = new AllergySet();

        for (Allergy obj : this) {
            if (lower <= obj.getId() && obj.getId() <= upper) {
                result.add(obj);
            }
        }

        return result;
    }


    /**
     * Loop through the current set of Allergy objects and assign value to the id attribute of each of it.
     *
     * @param value New attribute value
     * @return Current set of Allergy objects now with new attribute values.
     */
    public AllergySet withId(int value) {
        for (Allergy obj : this) {
            obj.setId(value);
        }

        return this;
    }


    /**
     * Loop through the current set of Allergy objects and collect a list of the name attribute values.
     *
     * @return List of String objects reachable via name attribute
     */
    public ObjectSet getName() {
        ObjectSet result = new ObjectSet();

        for (Allergy obj : this) {
            result.add(obj.getName());
        }

        return result;
    }


    /**
     * Loop through the current set of Allergy objects and collect those Allergy objects where the name attribute matches the parameter value.
     *
     * @param value Search value
     * @return Subset of Allergy objects that match the parameter
     */
    public AllergySet filterName(String value) {
        AllergySet result = new AllergySet();

        for (Allergy obj : this) {
            if (value.equals(obj.getName())) {
                result.add(obj);
            }
        }

        return result;
    }


    /**
     * Loop through the current set of Allergy objects and collect those Allergy objects where the name attribute is between lower and upper.
     *
     * @param lower Lower bound
     * @param upper Upper bound
     * @return Subset of Allergy objects that match the parameter
     */
    public AllergySet filterName(String lower, String upper) {
        AllergySet result = new AllergySet();

        for (Allergy obj : this) {
            if (lower.compareTo(obj.getName()) <= 0 && obj.getName().compareTo(upper) <= 0) {
                result.add(obj);
            }
        }

        return result;
    }


    /**
     * Loop through the current set of Allergy objects and assign value to the name attribute of each of it.
     *
     * @param value New attribute value
     * @return Current set of Allergy objects now with new attribute values.
     */
    public AllergySet withName(String value) {
        for (Allergy obj : this) {
            obj.setName(value);
        }

        return this;
    }

    /**
     * Loop through the current set of Allergy objects and collect a set of the Drug objects reached via drug.
     *
     * @return Set of Drug objects reachable via drug
     */
    public DrugSet getDrug() {
        DrugSet result = new DrugSet();

        for (Allergy obj : this) {
            result.with(obj.getDrug());
        }

        return result;
    }

    /**
     * Loop through the current set of Allergy objects and collect all contained objects with reference drug pointing to the object passed as parameter.
     *
     * @param value The object required as drug neighbor of the collected results.
     * @return Set of Drug objects referring to value via drug
     */
    public AllergySet filterDrug(Object value) {
        ObjectSet neighbors = new ObjectSet();

        if (value instanceof Collection) {
            neighbors.addAll((Collection<?>) value);
        } else {
            neighbors.add(value);
        }

        AllergySet answer = new AllergySet();

        for (Allergy obj : this) {
            if (!Collections.disjoint(neighbors, obj.getDrug())) {
                answer.add(obj);
            }
        }

        return answer;
    }

    /**
     * Loop through current set of ModelType objects and attach the Allergy object passed as parameter to the Drug attribute of each of it.
     *
     * @return The original set of ModelType objects now with the new neighbor attached to their Drug attributes.
     */
    public AllergySet withDrug(Drug value) {
        for (Allergy obj : this) {
            obj.withDrug(value);
        }

        return this;
    }

    /**
     * Loop through current set of ModelType objects and remove the Allergy object passed as parameter from the Drug attribute of each of it.
     *
     * @return The original set of ModelType objects now without the old neighbor.
     */
    public AllergySet withoutDrug(Drug value) {
        for (Allergy obj : this) {
            obj.withoutDrug(value);
        }

        return this;
    }

    /**
     * Loop through the current set of Allergy objects and collect a set of the User objects reached via user.
     *
     * @return Set of User objects reachable via user
     */
    public UserSet getUser() {
        UserSet result = new UserSet();

        for (Allergy obj : this) {
            result.with(obj.getUser());
        }

        return result;
    }

    /**
     * Loop through the current set of Allergy objects and collect all contained objects with reference user pointing to the object passed as parameter.
     *
     * @param value The object required as user neighbor of the collected results.
     * @return Set of User objects referring to value via user
     */
    public AllergySet filterUser(Object value) {
        ObjectSet neighbors = new ObjectSet();

        if (value instanceof Collection) {
            neighbors.addAll((Collection<?>) value);
        } else {
            neighbors.add(value);
        }

        AllergySet answer = new AllergySet();

        for (Allergy obj : this) {
            if (!Collections.disjoint(neighbors, obj.getUser())) {
                answer.add(obj);
            }
        }

        return answer;
    }

    /**
     * Loop through current set of ModelType objects and attach the Allergy object passed as parameter to the User attribute of each of it.
     *
     * @return The original set of ModelType objects now with the new neighbor attached to their User attributes.
     */
    public AllergySet withUser(User value) {
        for (Allergy obj : this) {
            obj.withUser(value);
        }

        return this;
    }

    /**
     * Loop through current set of ModelType objects and remove the Allergy object passed as parameter from the User attribute of each of it.
     *
     * @return The original set of ModelType objects now without the old neighbor.
     */
    public AllergySet withoutUser(User value) {
        for (Allergy obj : this) {
            obj.withoutUser(value);
        }

        return this;
    }

}

