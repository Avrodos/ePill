package com.doccuty.epill.model.util;

import de.uniks.networkparser.EPILLIdMap;
import de.uniks.networkparser.IdMap;

class CreatorCreator{

   public static IdMap createIdMap(String sessionID)
   {
	  IdMap jsonIdMap = new EPILLIdMap().withSession(sessionID);
      jsonIdMap.with(new TagCreator());
      jsonIdMap.with(new DrugCreator());
      jsonIdMap.with(new PackagingTopicCreator());
      jsonIdMap.with(new PackagingSectionCreator());
      jsonIdMap.with(new PackagingCreator());
      jsonIdMap.with(new ProductGroupCreator());
      jsonIdMap.with(new ActiveSubstanceCreator());
      jsonIdMap.with(new SubstanceGroupCreator());
      jsonIdMap.with(new PharmaceuticalFormCreator());
      jsonIdMap.with(new AdverseEffectCreator());
      jsonIdMap.with(new IndicationGroupCreator());
      jsonIdMap.with(new InteractionCreator());
      jsonIdMap.with(new IntakeInformationCreator());
      jsonIdMap.with(new CountryCreator());
      jsonIdMap.with(new LanguageCreator());
      jsonIdMap.with(new GenderCreator());
      jsonIdMap.with(new ItemInvocationCreator());
      jsonIdMap.with(new UserQueryCreator());
      jsonIdMap.with(new UserCreator());
      jsonIdMap.with(new DiseaseCreator());
      jsonIdMap.with(new DrugFeatureCreator());
      jsonIdMap.with(new ImageCreator());
      jsonIdMap.with(new UserDrugPlanCreator());
      return jsonIdMap;
   }
}
