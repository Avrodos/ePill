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

import de.uniks.networkparser.interfaces.SendableEntityCreator;
import de.uniks.networkparser.interfaces.SendableEntityCreatorNoIndex;

import com.doccuty.epill.model.DrugFeature;

import de.uniks.networkparser.IdMap;

import java.util.Date;

import com.doccuty.epill.disease.Disease;
import com.doccuty.epill.drug.Drug;
import com.doccuty.epill.image.Image;
import com.doccuty.epill.iteminvocation.ItemInvocation;
import com.doccuty.epill.model.ActiveSubstance;
import com.doccuty.epill.model.ProductGroup;
import com.doccuty.epill.packagingsection.PackagingSection;
import com.doccuty.epill.model.IndicationGroup;
import com.doccuty.epill.model.PharmaceuticalForm;
import com.doccuty.epill.model.AdverseEffect;
import com.doccuty.epill.model.Interaction;
import com.doccuty.epill.model.Packaging;
import com.doccuty.epill.userdrugplan.FoodToAvoid;

public class DrugCreator implements SendableEntityCreatorNoIndex
{
   private final String[] properties = new String[]
   {
      Drug.PROPERTY_ID,
      Drug.PROPERTY_NAME,
      Drug.PROPERTY_PACKAGING,
      Drug.PROPERTY_PACKAGINGSECTION,
      Drug.PROPERTY_ADVERSEEFFECTS,
      Drug.PROPERTY_ACTIVESUBSTANCE,
      Drug.PROPERTY_PHARMACEUTICALFORM,
      Drug.PROPERTY_PRODUCTGROUP,
      Drug.PROPERTY_INDICATIONGROUP,
      //Drug.PROPERTY_INTERACTION,
      //Drug.PROPERTY_CLICKS,
      Drug.PROPERTY_DISEASE,
      Drug.PROPERTY_DRUGFEATURE,
      Drug.PROPERTY_TAILOREDSUMMARY,
      Drug.PROPERTY_IMAGE,
      Drug.PROPERTY_REMEMBERED,
      Drug.PROPERTY_TAKEN,
      Drug.PROPERTY_YEAR,
      Drug.PROPERTY_STATUS,
      Drug.PROPERTY_VERSION,
      Drug.PROPERTY_HALF_TIME_PERIOD,
      Drug.PROPERTY_TAKE_ON_EMPTY_STOMACH,
      Drug.PROPERTY_TAKE_ON_FULL_STOMACH,
      Drug.PROPERTY_TAKE_TO_MEALS,
      Drug.PROPERTY_FOOD_TO_AVOID
   };
   
   @Override
   public String[] getProperties()
   {
      return properties;
   }
   
   @Override
   public Object getSendableInstance(boolean reference)
   {
      return new Drug();
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

      if (Drug.PROPERTY_ID.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getId();
      }

      if (Drug.PROPERTY_NAME.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getName();
      }

      if (Drug.PROPERTY_TAILOREDSUMMARY.equalsIgnoreCase(attribute))
      {
          return ((Drug) target).getTailoredSummary();
       }
      
      if (Drug.PROPERTY_PACKAGINGSECTION.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getPackagingSection();
      }

      if (Drug.PROPERTY_PACKAGING.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getPackaging();
      }

      if (Drug.PROPERTY_ACTIVESUBSTANCE.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getActiveSubstance();
      }

      if (Drug.PROPERTY_PRODUCTGROUP.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getProductGroup();
      }

      if (Drug.PROPERTY_INDICATIONGROUP.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getIndicationGroup();
      }

      if (Drug.PROPERTY_PHARMACEUTICALFORM.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getPharmaceuticalForm();
      }

      if (Drug.PROPERTY_ADVERSEEFFECTS.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getAdverseEffects();
      }

      if (Drug.PROPERTY_INTERACTION.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getInteraction();
      }

      if (Drug.PROPERTY_CLICKS.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getClicks();
      }

      if (Drug.PROPERTY_DISEASE.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getDisease();
      }
      
      if (Drug.PROPERTY_IMAGE.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getImage();
      }
      
      if (Drug.PROPERTY_REMEMBERED.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getIsRemembered();
      }
      
      if (Drug.PROPERTY_TAKEN.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getIsTaken();
      }
      
      if (Drug.PROPERTY_YEAR.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getYear();
      }
      
      if (Drug.PROPERTY_STATUS.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getStatus();
      }
      
      if (Drug.PROPERTY_VERSION.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getVersion();
      }
      
      if (Drug.PROPERTY_DRUGFEATURE.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getDrugFeature();
      }
      
      if (Drug.PROPERTY_HALF_TIME_PERIOD.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getPeriod();
      }
      
      if (Drug.PROPERTY_TAKE_ON_EMPTY_STOMACH.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getTakeOnEmptyStomach();
      }
      
      if (Drug.PROPERTY_TAKE_ON_FULL_STOMACH.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getTakeOnFullStomach();
      }
      
      if (Drug.PROPERTY_TAKE_TO_MEALS.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getTakeToMeals();
      }
      
      if (Drug.PROPERTY_COUNT_PER_DAY.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getCountPerDay();
      }
      
      if (Drug.PROPERTY_FOOD_TO_AVOID.equalsIgnoreCase(attribute))
      {
         return ((Drug) target).getFoodToAvoid();
      }
      
      return null;
   }
   
   @Override
   public boolean setValue(Object target, String attrName, Object value, String type)
   {
      if (Drug.PROPERTY_NAME.equalsIgnoreCase(attrName))
      {
         ((Drug) target).setName((String) value);
         return true;
      }

      if (Drug.PROPERTY_TAILOREDSUMMARY.equalsIgnoreCase(attrName))
      {
          ((Drug) target).setTailoredSummary((String) value);
          return true;
       }


      if (Drug.PROPERTY_REMEMBERED.equalsIgnoreCase(attrName))
      {
          ((Drug) target).setIsRemembered((boolean) value);
          return true;
       }      

      if (Drug.PROPERTY_STATUS.equalsIgnoreCase(attrName))
      {
          ((Drug) target).setStatus((Date) value);
          return true;
       }
      
      if (Drug.PROPERTY_YEAR.equalsIgnoreCase(attrName))
      {
          ((Drug) target).setYear((Date) value);
          return true;
       }
      
      if (Drug.PROPERTY_VERSION.equalsIgnoreCase(attrName))
      {
          ((Drug) target).setVersion((String) value);
          return true;
       }
      
      if (Drug.PROPERTY_TAKEN.equalsIgnoreCase(attrName))
      {
          ((Drug) target).setIsTaken((boolean) value);
          return true;
       }
      
      if (Drug.PROPERTY_ID.equalsIgnoreCase(attrName))
      {
         ((Drug) target).setId(Integer.parseInt(value.toString()));
         return true;
      }
  
      if (Drug.PROPERTY_IMAGE.equalsIgnoreCase(attrName))
      {
         ((Drug) target).setImage((Image) value);
         return true;
      }

      if (SendableEntityCreator.REMOVE.equals(type) && value != null)
      {
         attrName = attrName + type;
      }

      if (Drug.PROPERTY_PACKAGINGSECTION.equalsIgnoreCase(attrName))
      {
         ((Drug) target).withPackagingSection((PackagingSection) value);
         return true;
      }
      
      if ((Drug.PROPERTY_PACKAGINGSECTION + SendableEntityCreator.REMOVE).equalsIgnoreCase(attrName))
      {
         ((Drug) target).withoutPackagingSection((PackagingSection) value);
         return true;
      }

      if (Drug.PROPERTY_PACKAGING.equalsIgnoreCase(attrName))
      {
         ((Drug) target).withPackaging((Packaging) value);
         return true;
      }
      
      if ((Drug.PROPERTY_PACKAGING + SendableEntityCreator.REMOVE).equalsIgnoreCase(attrName))
      {
         ((Drug) target).withoutPackaging((Packaging) value);
         return true;
      }
   
      if (Drug.PROPERTY_ACTIVESUBSTANCE.equalsIgnoreCase(attrName))
      {
         ((Drug) target).withActiveSubstance((ActiveSubstance) value);
         return true;
      }
      
      if ((Drug.PROPERTY_ACTIVESUBSTANCE + SendableEntityCreator.REMOVE).equalsIgnoreCase(attrName))
      {
         ((Drug) target).withoutActiveSubstance((ActiveSubstance) value);
         return true;
      }

      if (Drug.PROPERTY_PRODUCTGROUP.equalsIgnoreCase(attrName))
      {
         ((Drug) target).setProductGroup((ProductGroup) value);
         return true;
      }

      if (Drug.PROPERTY_INDICATIONGROUP.equalsIgnoreCase(attrName))
      {
         ((Drug) target).setIndicationGroup((IndicationGroup) value);
         return true;
      }

      if (Drug.PROPERTY_PHARMACEUTICALFORM.equalsIgnoreCase(attrName))
      {
         ((Drug) target).withPharmaceuticalForm((PharmaceuticalForm) value);
         return true;
      }
      
      if ((Drug.PROPERTY_PHARMACEUTICALFORM + SendableEntityCreator.REMOVE).equalsIgnoreCase(attrName))
      {
         ((Drug) target).withoutPharmaceuticalForm((PharmaceuticalForm) value);
         return true;
      }

      if (Drug.PROPERTY_ADVERSEEFFECTS.equalsIgnoreCase(attrName))
      {
         ((Drug) target).withAdverseEffects((AdverseEffect) value);
         return true;
      }
      
      if ((Drug.PROPERTY_ADVERSEEFFECTS + SendableEntityCreator.REMOVE).equalsIgnoreCase(attrName))
      {
         ((Drug) target).withoutAdverseEffects((AdverseEffect) value);
         return true;
      }

      if (Drug.PROPERTY_INTERACTION.equalsIgnoreCase(attrName))
      {
         ((Drug) target).withInteraction((Interaction) value);
         return true;
      }
      
      if ((Drug.PROPERTY_INTERACTION + SendableEntityCreator.REMOVE).equalsIgnoreCase(attrName))
      {
         ((Drug) target).withoutInteraction((Interaction) value);
         return true;
      }

      if (Drug.PROPERTY_CLICKS.equalsIgnoreCase(attrName))
      {
         ((Drug) target).withClicks((ItemInvocation) value);
         return true;
      }
      
      if ((Drug.PROPERTY_CLICKS + SendableEntityCreator.REMOVE).equalsIgnoreCase(attrName))
      {
         ((Drug) target).withoutClicks((ItemInvocation) value);
         return true;
      }

      if (Drug.PROPERTY_DISEASE.equalsIgnoreCase(attrName))
      {
         ((Drug) target).withDisease((Disease) value);
         return true;
      }
      
      if ((Drug.PROPERTY_DISEASE + SendableEntityCreator.REMOVE).equalsIgnoreCase(attrName))
      {
         ((Drug) target).withoutDisease((Disease) value);
         return true;
      }
    
      if (Drug.PROPERTY_DRUGFEATURE.equalsIgnoreCase(attrName))
      {
         ((Drug) target).withDrugFeature((DrugFeature) value);
         return true;
      }
      
      if (Drug.PROPERTY_HALF_TIME_PERIOD.equalsIgnoreCase(attrName))
      {
         ((Drug) target).setPeriod((int) value);
         return true;
      }
      
      if (Drug.PROPERTY_TAKE_ON_EMPTY_STOMACH.equalsIgnoreCase(attrName))
      {
    	  ((Drug) target).setTakeOnEmptyStomach((boolean) value);
          return true;
      }
      
      if (Drug.PROPERTY_TAKE_ON_FULL_STOMACH.equalsIgnoreCase(attrName))
      {
    	  ((Drug) target).setTakeOnFullStomach((boolean) value);
          return true;
      }
      
      if (Drug.PROPERTY_TAKE_TO_MEALS.equalsIgnoreCase(attrName))
      {
    	  ((Drug) target).setTakeToMeals((boolean) value);
          return true;
      }
      
      if (Drug.PROPERTY_COUNT_PER_DAY.equalsIgnoreCase(attrName))
      {
         ((Drug) target).setCountPerDay((int) value);
         return true;
      }
      
      if (Drug.PROPERTY_FOOD_TO_AVOID.equalsIgnoreCase(attrName))
      {
         ((Drug) target).withFoodToAvoid((FoodToAvoid) value);
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
      ((Drug) entity).removeYou();
   }
}
