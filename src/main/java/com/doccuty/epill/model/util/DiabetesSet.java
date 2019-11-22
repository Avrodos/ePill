package com.doccuty.epill.model.util;

import com.doccuty.epill.diabetes.Diabetes;
import com.doccuty.epill.user.User;
import de.uniks.networkparser.list.NumberList;
import de.uniks.networkparser.list.ObjectSet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class DiabetesSet extends HashSet<Diabetes> {
    protected Class<?> getTypClass() {
        return Diabetes.class;
    }

    public DiabetesSet() {
        // empty
    }

    public DiabetesSet(Diabetes... objects) {
        for (Diabetes obj : objects) {
            this.add(obj);
        }
    }

    public DiabetesSet(Collection<Diabetes> objects) {
        this.addAll(objects);
    }

    public static final DiabetesSet EMPTY_SET = new DiabetesSet();


    public String getEntryType() {
        return "com.doccuty.epill.model.Diabetes";
    }


    @SuppressWarnings("unchecked")
    public DiabetesSet with(Object value) {
        if (value == null) {
            return this;
        } else if (value instanceof java.util.Collection) {
            this.addAll((Collection<Diabetes>) value);
        } else if (value != null) {
            this.add((Diabetes) value);
        }

        return this;
    }

    public DiabetesSet without(Diabetes value) {
        this.remove(value);
        return this;
    }


    /**
     * Loop through the current set of Diabetes objects and collect a list of the id attribute values.
     *
     * @return List of int objects reachable via id attribute
     */
    public NumberList getId() {
        NumberList result = new NumberList();

        for (Diabetes obj : this) {
            result.add(obj.getId());
        }

        return result;
    }


    /**
     * Loop through the current set of Diabetes objects and collect those Diabetes objects where the id attribute matches the parameter value.
     *
     * @param value Search value
     * @return Subset of Diabetes objects that match the parameter
     */
    public DiabetesSet filterId(int value) {
        DiabetesSet result = new DiabetesSet();

        for (Diabetes obj : this) {
            if (value == obj.getId()) {
                result.add(obj);
            }
        }

        return result;
    }


    /**
     * Loop through the current set of Diabetes objects and collect those Diabetes objects where the id attribute is between lower and upper.
     *
     * @param lower Lower bound
     * @param upper Upper bound
     * @return Subset of Diabetes objects that match the parameter
     */
    public DiabetesSet filterId(int lower, int upper) {
        DiabetesSet result = new DiabetesSet();

        for (Diabetes obj : this) {
            if (lower <= obj.getId() && obj.getId() <= upper) {
                result.add(obj);
            }
        }

        return result;
    }


    /**
     * Loop through the current set of Diabetes objects and assign value to the id attribute of each of it.
     *
     * @param value New attribute value
     * @return Current set of Diabetes objects now with new attribute values.
     */
    public DiabetesSet withId(int value) {
        for (Diabetes obj : this) {
            obj.setId(value);
        }

        return this;
    }


    /**
     * Loop through the current set of Diabetes objects and collect a list of the Diabetes attribute values.
     *
     * @return List of String objects reachable via Diabetes attribute
     */
    public ObjectSet getDiabetes() {
        ObjectSet result = new ObjectSet();

        for (Diabetes obj : this) {
            result.add(obj.getDiabetes());
        }

        return result;
    }


    /**
     * Loop through the current set of Diabetes objects and collect those Diabetes objects where the diabetes attribute matches the parameter value.
     *
     * @param value Search value
     * @return Subset of Diabetes objects that match the parameter
     */
    public DiabetesSet filterDiabetes(String value) {
        DiabetesSet result = new DiabetesSet();

        for (Diabetes obj : this) {
            if (value.equals(obj.getDiabetes())) {
                result.add(obj);
            }
        }

        return result;
    }


    /**
     * Loop through the current set of Diabetes objects and collect those Diabetes objects where the diabetes attribute is between lower and upper.
     *
     * @param lower Lower bound
     * @param upper Upper bound
     * @return Subset of Diabetes objects that match the parameter
     */
    public DiabetesSet filterDiabetes(String lower, String upper) {
        DiabetesSet result = new DiabetesSet();

        for (Diabetes obj : this) {
            if (lower.compareTo(obj.getDiabetes()) <= 0 && obj.getDiabetes().compareTo(upper) <= 0) {
                result.add(obj);
            }
        }

        return result;
    }


    /**
     * Loop through the current set of Diabetes objects and assign value to the diabetes attribute of each of it.
     *
     * @param value New attribute value
     * @return Current set of Diabetes objects now with new attribute values.
     */
    public DiabetesSet withDiabetes(String value) {
        for (Diabetes obj : this) {
            obj.setDiabetes(value);
        }

        return this;
    }

    /**
     * Loop through the current set of Diabetes objects and collect a set of the User objects reached via user.
     *
     * @return Set of User objects reachable via user
     */
    public UserSet getUser() {
        UserSet result = new UserSet();

        for (Diabetes obj : this) {
            result.with(obj.getUser());
        }

        return result;
    }

    /**
     * Loop through the current set of Diabetes objects and collect all contained objects with reference user pointing to the object passed as parameter.
     *
     * @param value The object required as user neighbor of the collected results.
     * @return Set of User objects referring to value via user
     */
    public DiabetesSet filterUser(Object value) {
        ObjectSet neighbors = new ObjectSet();

        if (value instanceof Collection) {
            neighbors.addAll((Collection<?>) value);
        } else {
            neighbors.add(value);
        }

        DiabetesSet answer = new DiabetesSet();

        for (Diabetes obj : this) {
            if (!Collections.disjoint(neighbors, obj.getUser())) {
                answer.add(obj);
            }
        }

        return answer;
    }

    /**
     * Loop through current set of ModelType objects and attach the Diabetes object passed as parameter to the User attribute of each of it.
     *
     * @return The original set of ModelType objects now with the new neighbor attached to their User attributes.
     */
    public DiabetesSet withUser(User value) {
        for (Diabetes obj : this) {
            obj.withUser(value);
        }

        return this;
    }

    /**
     * Loop through current set of ModelType objects and remove the Diabetes object passed as parameter from the User attribute of each of it.
     *
     * @return The original set of ModelType objects now without the old neighbor.
     */
    public DiabetesSet withoutUser(User value) {
        for (Diabetes obj : this) {
            obj.withoutUser(value);
        }

        return this;
    }

}
