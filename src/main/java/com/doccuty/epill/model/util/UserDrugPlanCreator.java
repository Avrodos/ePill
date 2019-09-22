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

import com.doccuty.epill.drug.Drug;
import com.doccuty.epill.iteminvocation.ItemInvocation;
import com.doccuty.epill.packagingsection.PackagingSection;
import com.doccuty.epill.user.User;
import com.doccuty.epill.userdrugplan.UserDrugPlan;
import de.uniks.networkparser.IdMap;
import de.uniks.networkparser.interfaces.SendableEntityCreatorNoIndex;

import java.util.Date;

public class UserDrugPlanCreator implements SendableEntityCreatorNoIndex
{
   private final String[] properties = new String[]
   {
      UserDrugPlan.PROPERTY_ID, UserDrugPlan.PROPERTY_DATETIME_INTAKE_PLANNED,
           UserDrugPlan.PROPERTY_DRUG,
           UserDrugPlan.PROPERTY_USER
   };

   @Override
   public String[] getProperties()
   {
      return properties;
   }

   @Override
   public Object getSendableInstance(boolean reference)
   {
      return new UserDrugPlan();
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

      if (UserDrugPlan.PROPERTY_ID.equalsIgnoreCase(attribute))
      {
         return ((UserDrugPlan) target).getId();
      }

      if (UserDrugPlan.PROPERTY_DATETIME_INTAKE_PLANNED.equalsIgnoreCase(attribute))
      {
         return ((UserDrugPlan) target).getDatetimeIntakePlanned();
      }

      if (UserDrugPlan.PROPERTY_DRUG.equalsIgnoreCase(attribute))
      {
         return ((UserDrugPlan) target).getDrug();
      }

      if (UserDrugPlan.PROPERTY_USER.equalsIgnoreCase(attribute))
      {
         return ((UserDrugPlan) target).getUser();
      }

      return null;
   }

   @Override
   public boolean setValue(Object target, String attrName, Object value, String type)
   {
      if (UserDrugPlan.PROPERTY_DATETIME_INTAKE_PLANNED.equalsIgnoreCase(attrName))
      {
         ((UserDrugPlan) target).setDateTimePlanned((Date) value);
         return true;
      }

      if (UserDrugPlan.PROPERTY_ID.equalsIgnoreCase(attrName))
      {
         ((UserDrugPlan) target).setId(Long.parseLong(value.toString()));
         return true;
      }

      if (SendableEntityCreatorNoIndex.REMOVE.equals(type) && value != null)
      {
         attrName = attrName + type;
      }

      if (UserDrugPlan.PROPERTY_DRUG.equalsIgnoreCase(attrName))
      {
         ((UserDrugPlan) target).setDrug((Drug) value);
         return true;
      }

      if (UserDrugPlan.PROPERTY_USER.equalsIgnoreCase(attrName))
      {
         ((UserDrugPlan) target).setUser((User) value);
         return true;
      }

      return false;
   }
   public static IdMap createIdMap(String sessionID)
   {
      return CreatorCreator.createIdMap(sessionID);
   }
   
   //==========================================================================
      public void removeObject(Object entity)
   {
      ((UserDrugPlan) entity).removeYou();
   }
}
