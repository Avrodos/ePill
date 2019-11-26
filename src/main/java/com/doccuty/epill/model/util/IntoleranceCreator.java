package com.doccuty.epill.model.util;

import com.doccuty.epill.drug.Drug;
import com.doccuty.epill.intolerance.Intolerance;
import com.doccuty.epill.user.User;
import de.uniks.networkparser.IdMap;
import de.uniks.networkparser.interfaces.SendableEntityCreatorNoIndex;

public class IntoleranceCreator implements SendableEntityCreatorNoIndex {

    private final String[] properties = new String[]
            {
                    Intolerance.PROPERTY_ID,
                    Intolerance.PROPERTY_NAME
            };

    @Override
    public String[] getProperties() {
        return properties;
    }

    @Override
    public Object getSendableInstance(boolean reference) {
        return new Intolerance();
    }

    @Override
    public Object getValue(Object target, String attrName) {
        int pos = attrName.indexOf('.');
        String attribute = attrName;

        if (pos > 0) {
            attribute = attrName.substring(0, pos);
        }

        if (Intolerance.PROPERTY_ID.equalsIgnoreCase(attribute)) {
            return ((Intolerance) target).getId();
        }

        if (Intolerance.PROPERTY_NAME.equalsIgnoreCase(attribute)) {
            return ((Intolerance) target).getName();
        }

        if (Intolerance.PROPERTY_DRUG.equalsIgnoreCase(attribute)) {
            return ((Intolerance) target).getDrug();
        }

        if (Intolerance.PROPERTY_USER.equalsIgnoreCase(attribute)) {
            return ((Intolerance) target).getUser();
        }

        return null;
    }

    @Override
    public boolean setValue(Object target, String attrName, Object value, String type) {
        if (Intolerance.PROPERTY_NAME.equalsIgnoreCase(attrName)) {
            ((Intolerance) target).setName((String) value);
            return true;
        }

        if (Intolerance.PROPERTY_ID.equalsIgnoreCase(attrName)) {
            ((Intolerance) target).setId(Integer.parseInt(value.toString()));
            return true;
        }

        if (SendableEntityCreatorNoIndex.REMOVE.equals(type) && value != null) {
            attrName = attrName + type;
        }

        if (Intolerance.PROPERTY_DRUG.equalsIgnoreCase(attrName)) {
            ((Intolerance) target).withDrug((Drug) value);
            return true;
        }

        if ((Intolerance.PROPERTY_DRUG + SendableEntityCreatorNoIndex.REMOVE).equalsIgnoreCase(attrName)) {
            ((Intolerance) target).withoutDrug((Drug) value);
            return true;
        }

        if (Intolerance.PROPERTY_USER.equalsIgnoreCase(attrName)) {
            ((Intolerance) target).withUser((User) value);
            return true;
        }

        if ((Intolerance.PROPERTY_USER + SendableEntityCreatorNoIndex.REMOVE).equalsIgnoreCase(attrName)) {
            ((Intolerance) target).withoutUser((User) value);
            return true;
        }

        return false;
    }

    public static IdMap createIdMap(String sessionID) {
        return com.doccuty.epill.model.util.CreatorCreator.createIdMap(sessionID);
    }

    //==========================================================================
    public void removeObject(Object entity) {
        ((Intolerance) entity).removeYou();
    }

}
