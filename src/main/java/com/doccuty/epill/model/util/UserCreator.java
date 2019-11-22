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
   
package com.doccuty.epill.model.util;

import com.doccuty.epill.allergy.Allergy;
import com.doccuty.epill.diabetes.Diabetes;
import com.doccuty.epill.disease.Disease;
import com.doccuty.epill.gender.Gender;
import com.doccuty.epill.iteminvocation.ItemInvocation;
import com.doccuty.epill.language.Language;
import com.doccuty.epill.model.Country;
import com.doccuty.epill.model.DrugFeature;
import com.doccuty.epill.model.PackagingTopic;
import com.doccuty.epill.model.UserQuery;
import com.doccuty.epill.user.User;
import de.uniks.networkparser.IdMap;
import de.uniks.networkparser.interfaces.SendableEntityCreatorNoIndex;

import java.util.Date;

public class UserCreator implements SendableEntityCreatorNoIndex
{
   private final String[] properties = new String[]
   {
      User.PROPERTY_ID,
      User.PROPERTY_FIRSTNAME,
      User.PROPERTY_LASTNAME,
      User.PROPERTY_USERNAME,
      User.PROPERTY_EMAIL,
      User.PROPERTY_DATEOFBIRTH,
      User.PROPERTY_DATEOFREGISTRATION,
      User.PROPERTY_PREFERREDFONTSIZE,
      User.PROPERTY_LEVELOFDETAIL,
      User.PROPERTY_PREFERREDPACKAGINGTOPIC,
      User.PROPERTY_PREFERREDDRUGFEATURE,
      User.PROPERTY_COUNTRY,
      User.PROPERTY_LANGUAGE,
      User.PROPERTY_GENDER,
      //User.PROPERTY_CLICKS,
      //User.PROPERTY_QUERY,
      User.PROPERTY_DISEASE,
      User.PROPERTY_REDGREENCOLORBLIND,
      User.PROPERTY_GID,
      User.PROPERTY_TPA,
      User.PROPERTY_A7ID,
       User.PROPERTY_WEIGHT,
       User.PROPERTY_BREAKFAST_TIME,
       User.PROPERTY_LUNCH_TIME,
           User.PROPERTY_DINNER_TIME,
           User.PROPERTY_ALLERGY,
           User.PROPERTY_DIABETES,
           User.PROPERTY_SMOKER
    };
   
   @Override
   public String[] getProperties()
   {
      return properties;
   }
   
   @Override
   public Object getSendableInstance(boolean reference)
   {
      return new User();
   }
   
   @Override
   public Object getValue(Object target, String attrName)
   {
      int pos = attrName.indexOf('.');
      String attribute = attrName;
      
      if (pos > 0)
      {
         attribute = attrName.substring(0, pos);
      }

      if (User.PROPERTY_ID.equalsIgnoreCase(attribute))
      {
         return ((User) target).getId();
      }

      if (User.PROPERTY_FIRSTNAME.equalsIgnoreCase(attribute))
      {
         return ((User) target).getFirstname();
      }

      if (User.PROPERTY_LASTNAME.equalsIgnoreCase(attribute))
      {
         return ((User) target).getLastname();
      }

      if (User.PROPERTY_USERNAME.equalsIgnoreCase(attribute))
      {
         return ((User) target).getUsername();
      }

      if (User.PROPERTY_PASSWORD.equalsIgnoreCase(attribute))
      {
         return ((User) target).getPassword();
      }

      if (User.PROPERTY_SALT.equalsIgnoreCase(attribute))
      {
         return ((User) target).getSalt();
      }

      if (User.PROPERTY_EMAIL.equalsIgnoreCase(attribute))
      {
         return ((User) target).getEmail();
      }

      if (User.PROPERTY_DATEOFBIRTH.equalsIgnoreCase(attribute))
      {
         return ((User) target).getDateOfBirth();
      }

      if (User.PROPERTY_DATEOFREGISTRATION.equalsIgnoreCase(attribute))
      {
         return ((User) target).getDateOfRegistration();
      }

      if (User.PROPERTY_PREFERREDFONTSIZE.equalsIgnoreCase(attribute))
      {
         return ((User) target).getPreferredFontSize();
      }
      
      if(User.PROPERTY_LEVELOFDETAIL.equalsIgnoreCase(attribute))
      {
          return ((User) target).getLevelOfDetail();
      }
      
      if (User.PROPERTY_REDGREENCOLORBLIND.equalsIgnoreCase(attribute))
      {
          return ((User) target).getRedGreenColorblind();
      }
      
      if (User.PROPERTY_PREFERREDPACKAGINGTOPIC.equalsIgnoreCase(attribute))
      {
         return ((User) target).getPreferredPackagingTopic();
      }
      
      if (User.PROPERTY_PREFERREDDRUGFEATURE.equalsIgnoreCase(attribute))
      {
          return ((User) target).getPreferredDrugFeature();
      }

      if (User.PROPERTY_COUNTRY.equalsIgnoreCase(attribute))
      {
         return ((User) target).getCountry();
      }

      if (User.PROPERTY_LANGUAGE.equalsIgnoreCase(attribute))
      {
         return ((User) target).getLanguage();
      }

      if (User.PROPERTY_GENDER.equalsIgnoreCase(attribute))
      {
         return ((User) target).getGender();
      }

      if (User.PROPERTY_CLICKS.equalsIgnoreCase(attribute))
      {
         return ((User) target).getClicks();
      }

      if (User.PROPERTY_QUERY.equalsIgnoreCase(attribute))
      {
         return ((User) target).getQuery();
      }

      if (User.PROPERTY_DISEASE.equalsIgnoreCase(attribute))
      {
         return ((User) target).getDisease();
      }

      if (User.PROPERTY_GID.equalsIgnoreCase(attribute))
      {
         return ((User) target).getGid();
      }

      if (User.PROPERTY_TPA.equalsIgnoreCase(attribute))
      {
         return ((User) target).getTPA();
      }

      if (User.PROPERTY_A7ID.equalsIgnoreCase(attribute))
      {
         return ((User) target).getA7id();
      }

       if (User.PROPERTY_WEIGHT.equalsIgnoreCase(attribute))
       {
           return ((User) target).getWeight();
       }

      if (User.PROPERTY_BREAKFAST_TIME.equalsIgnoreCase(attribute))
      {
         return ((User) target).getBreakfastTime();
      }

      if (User.PROPERTY_LUNCH_TIME.equalsIgnoreCase(attribute))
      {
         return ((User) target).getLunchTime();
      }

      if (User.PROPERTY_DINNER_TIME.equalsIgnoreCase(attribute))
      {
         return ((User) target).getDinnerTime();
      }

      if (User.PROPERTY_SMOKER.equalsIgnoreCase(attribute)) {
         return ((User) target).getSmoker();
      }

      if (User.PROPERTY_DIABETES.equalsIgnoreCase(attribute)) {
         return ((User) target).getDiabetes();
      }

      if (User.PROPERTY_ALLERGY.equalsIgnoreCase(attribute)) {
         return ((User) target).getAllergy();
      }

      return null;
   }
   
   @Override
   public boolean setValue(Object target, String attrName, Object value, String type)
   {
      if (User.PROPERTY_PREFERREDFONTSIZE.equalsIgnoreCase(attrName))
      {
         ((User) target).setPreferredFontSize((String) value);
         return true;
      }

      if(User.PROPERTY_LEVELOFDETAIL.equalsIgnoreCase(attrName))
      {
          ((User) target).setLevelOfDetail((int) value);
          return true;
       }

      if (User.PROPERTY_DATEOFBIRTH.equalsIgnoreCase(attrName))
      {
         ((User) target).setDateOfBirth((Date) value);
         return true;
      }

      if (User.PROPERTY_DATEOFREGISTRATION.equalsIgnoreCase(attrName))
      {
         ((User) target).setDateOfRegistration((Date) value);
         return true;
      }

      if (User.PROPERTY_EMAIL.equalsIgnoreCase(attrName))
      {
         ((User) target).setEmail((String) value);
         return true;
      }

      if (User.PROPERTY_SALT.equalsIgnoreCase(attrName))
      {
         ((User) target).setSalt((String) value);
         return true;
      }

      if (User.PROPERTY_PASSWORD.equalsIgnoreCase(attrName))
      {
         ((User) target).setPassword((String) value);
         return true;
      }

      if (User.PROPERTY_USERNAME.equalsIgnoreCase(attrName))
      {
         ((User) target).setUsername((String) value);
         return true;
      }

      if (User.PROPERTY_LASTNAME.equalsIgnoreCase(attrName))
      {
         ((User) target).setLastname((String) value);
         return true;
      }

      if (User.PROPERTY_FIRSTNAME.equalsIgnoreCase(attrName))
      {
         ((User) target).setFirstname((String) value);
         return true;
      }

      if (User.PROPERTY_WEIGHT.equalsIgnoreCase(attrName))
      {
         ((User) target).setWeight((int) value);
         return true;
      }

      if (User.PROPERTY_BREAKFAST_TIME.equalsIgnoreCase(attrName))
      {
         ((User) target).setBreakfastTime((int) value);
         return true;
      }

      if (User.PROPERTY_LUNCH_TIME.equalsIgnoreCase(attrName))
      {
         ((User) target).setLunchTime((int) value);
         return true;
      }

      if (User.PROPERTY_DINNER_TIME.equalsIgnoreCase(attrName))
      {
         ((User) target).setDinnerTime((int) value);
         return true;
      }

      if (User.PROPERTY_REDGREENCOLORBLIND.equalsIgnoreCase(attrName))
      {
         ((User) target).setRedGreenColorblind((boolean) value);
         return true;
      }

      if (User.PROPERTY_TPA.equalsIgnoreCase(attrName))
      {
         ((User) target).setTPA((Boolean) value);
         return true;
      }

      if (User.PROPERTY_ID.equalsIgnoreCase(attrName))
      {
         ((User) target).setId(Integer.parseInt(value.toString()));
         return true;
      }

      if (SendableEntityCreatorNoIndex.REMOVE.equals(type) && value != null)
      {
         attrName = attrName + type;
      }

      if (User.PROPERTY_PREFERREDPACKAGINGTOPIC.equalsIgnoreCase(attrName))
      {
         ((User) target).withPreferredPackagingTopic((PackagingTopic) value);
         return true;
      }
      
      if ((User.PROPERTY_PREFERREDPACKAGINGTOPIC + SendableEntityCreatorNoIndex.REMOVE).equalsIgnoreCase(attrName))
      {
         ((User) target).withoutPreferredPackagingTopic((PackagingTopic) value);
         return true;
      }

      if (User.PROPERTY_PREFERREDDRUGFEATURE.equalsIgnoreCase(attrName))
      {
         ((User) target).withPreferredDrugFeature((DrugFeature) value);
         return true;
      }
      
      if ((User.PROPERTY_PREFERREDDRUGFEATURE + SendableEntityCreatorNoIndex.REMOVE).equalsIgnoreCase(attrName))
      {
    	  	 ((User) target).withoutPreferredDrugFeature((DrugFeature) value);
         return true;
      }

      if (User.PROPERTY_COUNTRY.equalsIgnoreCase(attrName))
      {
         ((User) target).setCountry((Country) value);
         return true;
      }

      if (User.PROPERTY_LANGUAGE.equalsIgnoreCase(attrName))
      {
         ((User) target).setLanguage((Language) value);
         return true;
      }

      if (User.PROPERTY_GENDER.equalsIgnoreCase(attrName))
      {
         ((User) target).setGender((Gender) value);
         return true;
      }

      if (User.PROPERTY_CLICKS.equalsIgnoreCase(attrName))
      {
         ((User) target).withClicks((ItemInvocation) value);
         return true;
      }
      
      if ((User.PROPERTY_CLICKS + SendableEntityCreatorNoIndex.REMOVE).equalsIgnoreCase(attrName))
      {
         ((User) target).withoutClicks((ItemInvocation) value);
         return true;
      }

      if (User.PROPERTY_QUERY.equalsIgnoreCase(attrName))
      {
         ((User) target).withQuery((UserQuery) value);
         return true;
      }
      
      if ((User.PROPERTY_QUERY + SendableEntityCreatorNoIndex.REMOVE).equalsIgnoreCase(attrName))
      {
         ((User) target).withoutQuery((UserQuery) value);
         return true;
      }

      if (User.PROPERTY_DISEASE.equalsIgnoreCase(attrName))
      {
         ((User) target).withDisease((Disease) value);
         return true;
      }
      
      if ((User.PROPERTY_DISEASE + SendableEntityCreatorNoIndex.REMOVE).equalsIgnoreCase(attrName))
      {
         ((User) target).withoutDisease((Disease) value);
         return true;
      }

      if (User.PROPERTY_A7ID.equalsIgnoreCase(attrName)) {
         ((User) target).setA7id((String) value);
         return true;
      }

      if (User.PROPERTY_ALLERGY.equalsIgnoreCase(attrName)) {
         ((User) target).withAllergy((Allergy) value);
         return true;
      }

      if (User.PROPERTY_DIABETES.equalsIgnoreCase(attrName)) {
         ((User) target).setDiabetes((Diabetes) value);
         return true;
      }

      if (User.PROPERTY_SMOKER.equalsIgnoreCase(attrName)) {
         ((User) target).setSmoker((Boolean) value);
         return true;
      }

      
      return false;
   }
   public static IdMap createIdMap(String sessionID)
   {
      return com.doccuty.epill.model.util.CreatorCreator.createIdMap(sessionID);
   }
   
   //==========================================================================
      public void removeObject(Object entity)
   {
      ((User) entity).removeYou();
   }
}
