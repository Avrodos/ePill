package com.doccuty.epill.model.util;


import com.doccuty.epill.drug.Drug;
import com.doccuty.epill.intolerance.Intolerance;
import com.doccuty.epill.user.User;
import de.uniks.networkparser.list.NumberList;
import de.uniks.networkparser.list.ObjectSet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class IntoleranceSet extends HashSet<Intolerance> {
    protected Class<?> getTypClass() {
        return Intolerance.class;
    }

    public IntoleranceSet() {
        // empty
    }

    public IntoleranceSet(Intolerance... objects) {
        for (Intolerance obj : objects) {
            this.add(obj);
        }
    }

    public IntoleranceSet(Collection<Intolerance> objects) {
        this.addAll(objects);
    }

    public static final IntoleranceSet EMPTY_SET = new IntoleranceSet();


    public String getEntryType() {
        return "com.doccuty.epill.model.Intolerance";
    }


    @SuppressWarnings("unchecked")
    public IntoleranceSet with(Object value) {
        if (value == null) {
            return this;
        } else if (value instanceof java.util.Collection) {
            this.addAll((Collection<Intolerance>) value);
        } else if (value != null) {
            this.add((Intolerance) value);
        }

        return this;
    }

    public IntoleranceSet without(Intolerance value) {
        this.remove(value);
        return this;
    }


    /**
     * Loop through the current set of Intolerance objects and collect a list of the id attribute values.
     *
     * @return List of int objects reachable via id attribute
     */
    public NumberList getId() {
        NumberList result = new NumberList();

        for (Intolerance obj : this) {
            result.add(obj.getId());
        }

        return result;
    }


    /**
     * Loop through the current set of Intolerance objects and collect those Intolerance objects where the id attribute matches the parameter value.
     *
     * @param value Search value
     * @return Subset of Intolerance objects that match the parameter
     */
    public IntoleranceSet filterId(int value) {
        IntoleranceSet result = new IntoleranceSet();

        for (Intolerance obj : this) {
            if (value == obj.getId()) {
                result.add(obj);
            }
        }

        return result;
    }


    /**
     * Loop through the current set of Intolerance objects and collect those Intolerance objects where the id attribute is between lower and upper.
     *
     * @param lower Lower bound
     * @param upper Upper bound
     * @return Subset of Intolerance objects that match the parameter
     */
    public IntoleranceSet filterId(int lower, int upper) {
        IntoleranceSet result = new IntoleranceSet();

        for (Intolerance obj : this) {
            if (lower <= obj.getId() && obj.getId() <= upper) {
                result.add(obj);
            }
        }

        return result;
    }


    /**
     * Loop through the current set of Intolerance objects and assign value to the id attribute of each of it.
     *
     * @param value New attribute value
     * @return Current set of Intolerance objects now with new attribute values.
     */
    public IntoleranceSet withId(int value) {
        for (Intolerance obj : this) {
            obj.setId(value);
        }

        return this;
    }


    /**
     * Loop through the current set of Intolerance objects and collect a list of the name attribute values.
     *
     * @return List of String objects reachable via name attribute
     */
    public ObjectSet getName() {
        ObjectSet result = new ObjectSet();

        for (Intolerance obj : this) {
            result.add(obj.getName());
        }

        return result;
    }


    /**
     * Loop through the current set of Intolerance objects and collect those Intolerance objects where the name attribute matches the parameter value.
     *
     * @param value Search value
     * @return Subset of Intolerance objects that match the parameter
     */
    public IntoleranceSet filterName(String value) {
        IntoleranceSet result = new IntoleranceSet();

        for (Intolerance obj : this) {
            if (value.equals(obj.getName())) {
                result.add(obj);
            }
        }

        return result;
    }


    /**
     * Loop through the current set of Intolerance objects and collect those Intolerance objects where the name attribute is between lower and upper.
     *
     * @param lower Lower bound
     * @param upper Upper bound
     * @return Subset of Intolerance objects that match the parameter
     */
    public IntoleranceSet filterName(String lower, String upper) {
        IntoleranceSet result = new IntoleranceSet();

        for (Intolerance obj : this) {
            if (lower.compareTo(obj.getName()) <= 0 && obj.getName().compareTo(upper) <= 0) {
                result.add(obj);
            }
        }

        return result;
    }


    /**
     * Loop through the current set of Intolerance objects and assign value to the name attribute of each of it.
     *
     * @param value New attribute value
     * @return Current set of Intolerance objects now with new attribute values.
     */
    public IntoleranceSet withName(String value) {
        for (Intolerance obj : this) {
            obj.setName(value);
        }

        return this;
    }

    /**
     * Loop through the current set of Intolerance objects and collect a set of the Drug objects reached via drug.
     *
     * @return Set of Drug objects reachable via drug
     */
    public DrugSet getDrug() {
        DrugSet result = new DrugSet();

        for (Intolerance obj : this) {
            result.with(obj.getDrug());
        }

        return result;
    }

    /**
     * Loop through the current set of Intolerance objects and collect all contained objects with reference drug pointing to the object passed as parameter.
     *
     * @param value The object required as drug neighbor of the collected results.
     * @return Set of Drug objects referring to value via drug
     */
    public IntoleranceSet filterDrug(Object value) {
        ObjectSet neighbors = new ObjectSet();

        if (value instanceof Collection) {
            neighbors.addAll((Collection<?>) value);
        } else {
            neighbors.add(value);
        }

        IntoleranceSet answer = new IntoleranceSet();

        for (Intolerance obj : this) {
            if (!Collections.disjoint(neighbors, obj.getDrug())) {
                answer.add(obj);
            }
        }

        return answer;
    }

    /**
     * Loop through current set of ModelType objects and attach the Intolerance object passed as parameter to the Drug attribute of each of it.
     *
     * @return The original set of ModelType objects now with the new neighbor attached to their Drug attributes.
     */
    public IntoleranceSet withDrug(Drug value) {
        for (Intolerance obj : this) {
            obj.withDrug(value);
        }

        return this;
    }

    /**
     * Loop through current set of ModelType objects and remove the Intolerance object passed as parameter from the Drug attribute of each of it.
     *
     * @return The original set of ModelType objects now without the old neighbor.
     */
    public IntoleranceSet withoutDrug(Drug value) {
        for (Intolerance obj : this) {
            obj.withoutDrug(value);
        }

        return this;
    }

    /**
     * Loop through the current set of Intolerance objects and collect a set of the User objects reached via user.
     *
     * @return Set of User objects reachable via user
     */
    public UserSet getUser() {
        UserSet result = new UserSet();

        for (Intolerance obj : this) {
            result.with(obj.getUser());
        }

        return result;
    }

    /**
     * Loop through the current set of Intolerance objects and collect all contained objects with reference user pointing to the object passed as parameter.
     *
     * @param value The object required as user neighbor of the collected results.
     * @return Set of User objects referring to value via user
     */
    public IntoleranceSet filterUser(Object value) {
        ObjectSet neighbors = new ObjectSet();

        if (value instanceof Collection) {
            neighbors.addAll((Collection<?>) value);
        } else {
            neighbors.add(value);
        }

        IntoleranceSet answer = new IntoleranceSet();

        for (Intolerance obj : this) {
            if (!Collections.disjoint(neighbors, obj.getUser())) {
                answer.add(obj);
            }
        }

        return answer;
    }

    /**
     * Loop through current set of ModelType objects and attach the Intolerance object passed as parameter to the User attribute of each of it.
     *
     * @return The original set of ModelType objects now with the new neighbor attached to their User attributes.
     */
    public IntoleranceSet withUser(User value) {
        for (Intolerance obj : this) {
            obj.withUser(value);
        }

        return this;
    }

    /**
     * Loop through current set of ModelType objects and remove the Intolerance object passed as parameter from the User attribute of each of it.
     *
     * @return The original set of ModelType objects now without the old neighbor.
     */
    public IntoleranceSet withoutUser(User value) {
        for (Intolerance obj : this) {
            obj.withoutUser(value);
        }

        return this;
    }

}

