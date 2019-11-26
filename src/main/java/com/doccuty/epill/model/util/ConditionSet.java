package com.doccuty.epill.model.util;


import com.doccuty.epill.condition.Condition;
import com.doccuty.epill.user.User;
import de.uniks.networkparser.list.NumberList;
import de.uniks.networkparser.list.ObjectSet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class ConditionSet extends HashSet<Condition> {
    protected Class<?> getTypClass() {
        return Condition.class;
    }

    public ConditionSet() {
        // empty
    }

    public ConditionSet(Condition... objects) {
        for (Condition obj : objects) {
            this.add(obj);
        }
    }

    public ConditionSet(Collection<Condition> objects) {
        this.addAll(objects);
    }

    public static final ConditionSet EMPTY_SET = new ConditionSet();


    public String getEntryType() {
        return "com.doccuty.epill.model.Condition";
    }


    @SuppressWarnings("unchecked")
    public ConditionSet with(Object value) {
        if (value == null) {
            return this;
        } else if (value instanceof java.util.Collection) {
            this.addAll((Collection<Condition>) value);
        } else if (value != null) {
            this.add((Condition) value);
        }

        return this;
    }

    public ConditionSet without(Condition value) {
        this.remove(value);
        return this;
    }


    /**
     * Loop through the current set of Condition objects and collect a list of the id attribute values.
     *
     * @return List of int objects reachable via id attribute
     */
    public NumberList getId() {
        NumberList result = new NumberList();

        for (Condition obj : this) {
            result.add(obj.getId());
        }

        return result;
    }


    /**
     * Loop through the current set of Condition objects and collect those Condition objects where the id attribute matches the parameter value.
     *
     * @param value Search value
     * @return Subset of Condition objects that match the parameter
     */
    public ConditionSet filterId(int value) {
        ConditionSet result = new ConditionSet();

        for (Condition obj : this) {
            if (value == obj.getId()) {
                result.add(obj);
            }
        }

        return result;
    }


    /**
     * Loop through the current set of Condition objects and collect those Condition objects where the id attribute is between lower and upper.
     *
     * @param lower Lower bound
     * @param upper Upper bound
     * @return Subset of Condition objects that match the parameter
     */
    public ConditionSet filterId(int lower, int upper) {
        ConditionSet result = new ConditionSet();

        for (Condition obj : this) {
            if (lower <= obj.getId() && obj.getId() <= upper) {
                result.add(obj);
            }
        }

        return result;
    }


    /**
     * Loop through the current set of Condition objects and assign value to the id attribute of each of it.
     *
     * @param value New attribute value
     * @return Current set of Condition objects now with new attribute values.
     */
    public ConditionSet withId(int value) {
        for (Condition obj : this) {
            obj.setId(value);
        }

        return this;
    }


    /**
     * Loop through the current set of Condition objects and collect a list of the name attribute values.
     *
     * @return List of String objects reachable via name attribute
     */
    public ObjectSet getName() {
        ObjectSet result = new ObjectSet();

        for (Condition obj : this) {
            result.add(obj.getName());
        }

        return result;
    }


    /**
     * Loop through the current set of Condition objects and collect those Condition objects where the name attribute matches the parameter value.
     *
     * @param value Search value
     * @return Subset of Condition objects that match the parameter
     */
    public ConditionSet filterName(String value) {
        ConditionSet result = new ConditionSet();

        for (Condition obj : this) {
            if (value.equals(obj.getName())) {
                result.add(obj);
            }
        }

        return result;
    }


    /**
     * Loop through the current set of Condition objects and collect those Condition objects where the name attribute is between lower and upper.
     *
     * @param lower Lower bound
     * @param upper Upper bound
     * @return Subset of Condition objects that match the parameter
     */
    public ConditionSet filterName(String lower, String upper) {
        ConditionSet result = new ConditionSet();

        for (Condition obj : this) {
            if (lower.compareTo(obj.getName()) <= 0 && obj.getName().compareTo(upper) <= 0) {
                result.add(obj);
            }
        }

        return result;
    }


    /**
     * Loop through the current set of Condition objects and assign value to the name attribute of each of it.
     *
     * @param value New attribute value
     * @return Current set of Condition objects now with new attribute values.
     */
    public ConditionSet withName(String value) {
        for (Condition obj : this) {
            obj.setName(value);
        }

        return this;
    }

    /**
     * Loop through the current set of Condition objects and collect a set of the User objects reached via user.
     *
     * @return Set of User objects reachable via user
     */
    public UserSet getUser() {
        UserSet result = new UserSet();

        for (Condition obj : this) {
            result.with(obj.getUser());
        }

        return result;
    }

    /**
     * Loop through the current set of Condition objects and collect all contained objects with reference user pointing to the object passed as parameter.
     *
     * @param value The object required as user neighbor of the collected results.
     * @return Set of User objects referring to value via user
     */
    public ConditionSet filterUser(Object value) {
        ObjectSet neighbors = new ObjectSet();

        if (value instanceof Collection) {
            neighbors.addAll((Collection<?>) value);
        } else {
            neighbors.add(value);
        }

        ConditionSet answer = new ConditionSet();

        for (Condition obj : this) {
            if (!Collections.disjoint(neighbors, obj.getUser())) {
                answer.add(obj);
            }
        }

        return answer;
    }

    /**
     * Loop through current set of ModelType objects and attach the Condition object passed as parameter to the User attribute of each of it.
     *
     * @return The original set of ModelType objects now with the new neighbor attached to their User attributes.
     */
    public ConditionSet withUser(User value) {
        for (Condition obj : this) {
            obj.withUser(value);
        }

        return this;
    }

    /**
     * Loop through current set of ModelType objects and remove the Condition object passed as parameter from the User attribute of each of it.
     *
     * @return The original set of ModelType objects now without the old neighbor.
     */
    public ConditionSet withoutUser(User value) {
        for (Condition obj : this) {
            obj.withoutUser(value);
        }

        return this;
    }

}

