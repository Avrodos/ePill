package com.doccuty.epill.model.util;

import com.doccuty.epill.condition.Condition;
import com.doccuty.epill.user.User;
import de.uniks.networkparser.IdMap;
import de.uniks.networkparser.interfaces.SendableEntityCreatorNoIndex;

public class ConditionCreator implements SendableEntityCreatorNoIndex {

    private final String[] properties = new String[]
            {
                    Condition.PROPERTY_ID,
                    Condition.PROPERTY_NAME
            };

    @Override
    public String[] getProperties() {
        return properties;
    }

    @Override
    public Object getSendableInstance(boolean reference) {
        return new Condition();
    }

    @Override
    public Object getValue(Object target, String attrName) {
        int pos = attrName.indexOf('.');
        String attribute = attrName;

        if (pos > 0) {
            attribute = attrName.substring(0, pos);
        }

        if (Condition.PROPERTY_ID.equalsIgnoreCase(attribute)) {
            return ((Condition) target).getId();
        }

        if (Condition.PROPERTY_NAME.equalsIgnoreCase(attribute)) {
            return ((Condition) target).getName();
        }

        if (Condition.PROPERTY_USER.equalsIgnoreCase(attribute)) {
            return ((Condition) target).getUser();
        }

        return null;
    }

    @Override
    public boolean setValue(Object target, String attrName, Object value, String type) {
        if (Condition.PROPERTY_NAME.equalsIgnoreCase(attrName)) {
            ((Condition) target).setName((String) value);
            return true;
        }

        if (Condition.PROPERTY_ID.equalsIgnoreCase(attrName)) {
            ((Condition) target).setId(Integer.parseInt(value.toString()));
            return true;
        }

        if (SendableEntityCreatorNoIndex.REMOVE.equals(type) && value != null) {
            attrName = attrName + type;
        }

        if (Condition.PROPERTY_USER.equalsIgnoreCase(attrName)) {
            ((Condition) target).withUser((User) value);
            return true;
        }

        if ((Condition.PROPERTY_USER + SendableEntityCreatorNoIndex.REMOVE).equalsIgnoreCase(attrName)) {
            ((Condition) target).withoutUser((User) value);
            return true;
        }

        return false;
    }

    public static IdMap createIdMap(String sessionID) {
        return com.doccuty.epill.model.util.CreatorCreator.createIdMap(sessionID);
    }

    //==========================================================================
    public void removeObject(Object entity) {
        ((Condition) entity).removeYou();
    }

}
