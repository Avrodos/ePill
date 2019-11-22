package com.doccuty.epill.model.util;

import com.doccuty.epill.allergy.Allergy;
import com.doccuty.epill.drug.Drug;
import com.doccuty.epill.user.User;
import de.uniks.networkparser.IdMap;
import de.uniks.networkparser.interfaces.SendableEntityCreatorNoIndex;

public class AllergyCreator implements SendableEntityCreatorNoIndex {

    private final String[] properties = new String[]
            {
                    Allergy.PROPERTY_ID,
                    Allergy.PROPERTY_NAME
            };

    @Override
    public String[] getProperties() {
        return properties;
    }

    @Override
    public Object getSendableInstance(boolean reference) {
        return new Allergy();
    }

    @Override
    public Object getValue(Object target, String attrName) {
        int pos = attrName.indexOf('.');
        String attribute = attrName;

        if (pos > 0) {
            attribute = attrName.substring(0, pos);
        }

        if (Allergy.PROPERTY_ID.equalsIgnoreCase(attribute)) {
            return ((Allergy) target).getId();
        }

        if (Allergy.PROPERTY_NAME.equalsIgnoreCase(attribute)) {
            return ((Allergy) target).getName();
        }

        if (Allergy.PROPERTY_DRUG.equalsIgnoreCase(attribute)) {
            return ((Allergy) target).getDrug();
        }

        if (Allergy.PROPERTY_USER.equalsIgnoreCase(attribute)) {
            return ((Allergy) target).getUser();
        }

        return null;
    }

    @Override
    public boolean setValue(Object target, String attrName, Object value, String type) {
        if (Allergy.PROPERTY_NAME.equalsIgnoreCase(attrName)) {
            ((Allergy) target).setName((String) value);
            return true;
        }

        if (Allergy.PROPERTY_ID.equalsIgnoreCase(attrName)) {
            ((Allergy) target).setId(Integer.parseInt(value.toString()));
            return true;
        }

        if (SendableEntityCreatorNoIndex.REMOVE.equals(type) && value != null) {
            attrName = attrName + type;
        }

        if (Allergy.PROPERTY_DRUG.equalsIgnoreCase(attrName)) {
            ((Allergy) target).withDrug((Drug) value);
            return true;
        }

        if ((Allergy.PROPERTY_DRUG + SendableEntityCreatorNoIndex.REMOVE).equalsIgnoreCase(attrName)) {
            ((Allergy) target).withoutDrug((Drug) value);
            return true;
        }

        if (Allergy.PROPERTY_USER.equalsIgnoreCase(attrName)) {
            ((Allergy) target).withUser((User) value);
            return true;
        }

        if ((Allergy.PROPERTY_USER + SendableEntityCreatorNoIndex.REMOVE).equalsIgnoreCase(attrName)) {
            ((Allergy) target).withoutUser((User) value);
            return true;
        }

        return false;
    }

    public static IdMap createIdMap(String sessionID) {
        return com.doccuty.epill.model.util.CreatorCreator.createIdMap(sessionID);
    }

    //==========================================================================
    public void removeObject(Object entity) {
        ((Allergy) entity).removeYou();
    }

}
