package com.doccuty.epill.model.util;

import com.doccuty.epill.diabetes.Diabetes;
import com.doccuty.epill.user.User;
import de.uniks.networkparser.IdMap;
import de.uniks.networkparser.interfaces.SendableEntityCreatorNoIndex;

public class DiabetesCreator implements SendableEntityCreatorNoIndex {
    private final String[] properties = new String[]
            {
                    Diabetes.PROPERTY_ID,
                    Diabetes.PROPERTY_DIABETES
            };

    @Override
    public String[] getProperties() {
        return properties;
    }

    @Override
    public Object getSendableInstance(boolean reference) {
        return new Diabetes();
    }

    @Override
    public Object getValue(Object target, String attrName) {
        int pos = attrName.indexOf('.');
        String attribute = attrName;

        if (pos > 0) {
            attribute = attrName.substring(0, pos);
        }

        if (Diabetes.PROPERTY_ID.equalsIgnoreCase(attribute)) {
            return ((Diabetes) target).getId();
        }

        if (Diabetes.PROPERTY_DIABETES.equalsIgnoreCase(attribute)) {
            return ((Diabetes) target).getDiabetes();
        }

        if (Diabetes.PROPERTY_USER.equalsIgnoreCase(attribute)) {
            return ((Diabetes) target).getUser();
        }

        return null;
    }

    @Override
    public boolean setValue(Object target, String attrName, Object value, String type) {
        if (Diabetes.PROPERTY_DIABETES.equalsIgnoreCase(attrName)) {
            ((Diabetes) target).setDiabetes((String) value);
            return true;
        }

        if (Diabetes.PROPERTY_ID.equalsIgnoreCase(attrName)) {
            ((Diabetes) target).setId(Integer.parseInt(value.toString()));
            return true;
        }

        if (SendableEntityCreatorNoIndex.REMOVE.equals(type) && value != null) {
            attrName = attrName + type;
        }

        if (Diabetes.PROPERTY_USER.equalsIgnoreCase(attrName)) {
            ((Diabetes) target).withUser((User) value);
            return true;
        }

        if ((Diabetes.PROPERTY_USER + SendableEntityCreatorNoIndex.REMOVE).equalsIgnoreCase(attrName)) {
            ((Diabetes) target).withoutUser((User) value);
            return true;
        }

        return false;
    }

    public static IdMap createIdMap(String sessionID) {
        return com.doccuty.epill.model.util.CreatorCreator.createIdMap(sessionID);
    }

    //==========================================================================
    public void removeObject(Object entity) {
        ((Diabetes) entity).removeYou();
    }
}

