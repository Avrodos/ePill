package com.doccuty.epill.drug;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doccuty.epill.iteminvocation.ItemInvocation;
import com.doccuty.epill.iteminvocation.ItemInvocationRepository;
import com.doccuty.epill.model.DrugFeature;
import com.doccuty.epill.model.Interaction;
import com.doccuty.epill.tailoredtext.TailoredText;
import com.doccuty.epill.tailoredtext.TailoredTextService;
import com.doccuty.epill.user.User;
import com.doccuty.epill.user.UserService;
import com.doccuty.epill.userdrugplan.Instruction;
import com.doccuty.epill.userdrugplan.UserDrugPlanItemRepository;

@Service
public class DrugService {

	private static final Logger LOG = LoggerFactory.getLogger(DrugService.class);

	private static final String PROPERTY_DRUG_TAKING = "taking";

	private static final String PROPERTY_DRUG_REMEMBER = "remember";

	@Autowired
	DrugRepository repository;

	@Autowired
	UserService userService;

	@Autowired
	ItemInvocationRepository invocationRepository;

	@Autowired
	UserDrugPlanItemRepository userDrugPlanRepository;

	@Autowired
	DrugFeatureRepository featureRepository;

	@Autowired
	TailoredTextService tailoringService;
	
	public List<Drug> findAllDrugs() {
		final List<Drug> drugs = repository.findAllOrderByName();

		if (!userService.isAnonymous()) {
			final User user = userService.getUserById(userService.getCurrentUser().getId());

			if (user == null)
				return drugs;

			final List<Drug> taking = repository.findUserDrugsTaking(user.getId());
			final List<Drug> remember = repository.findUserDrugsRemembered(user.getId());

			for (Drug drug : drugs) {
				if (taking.contains(drug))
					drug.setIsTaken(true);

				if (remember.contains(drug))
					drug.setIsRemembered(true);

				drug = tailoringService.tailorDrugToUser(drug, user);

				// load tailored summary
				final TailoredText summary = tailoringService.getTailoredMinimumSummaryByDrugAndUser(drug, user);

				if (summary != null) {
					drug.setTailoredSummary(summary.getText());
				}
			}
		}

		LOG.info("Found {} drugs.", drugs.size());

		return drugs;
	}

	public Drug saveDrug(Drug drug) {

		LOG.info("Saved drug={}", drug);

		return repository.save(drug);
	}

	public Drug findDrugById(long id) {

		Drug drug = repository.findOne(id);

		if (drug != null && !userService.isAnonymous()) {

			// save invocation of the drug

			User user = userService.getCurrentUser();

			final ItemInvocation invocation = new ItemInvocation();
			invocation.withDrug(drug).withUser(user);

			user = userService.saveItemInvocation(invocation);

			// check if drug is already in remember or taking list
			for (final User usr : drug.getUserRemembering()) {
				if (usr.getId() == user.getId()) {
					drug.setIsRemembered(true);
					break;
				}
			}

			for (final User usr : drug.getUser()) {
				if (usr.getId() == user.getId()) {
					drug.setIsTaken(true);
					break;
				}
			}

			// tailor drug information to user characteristics
			drug = this.tailoringService.tailorDrugToUser(drug, user);
		}

		return drug;
	}

	public List<Drug> findDrugByName(String exp) {
		final List<Drug> list = repository.findByNameContainingIgnoreCase(exp);
		return list;
	}

	public List<Drug> getDrugMinimized(String value) {
		return repository.findByNameMinimized(value);
	}

	public List<DrugFeature> findAllDrugFeaturesSimple() {
		return featureRepository.findAllSimple();
	}
	
	/**
	 * check
	 * 
	 * @return
	 */

	public String checkUserDrugsInteractions(String listname) {

		final StringBuilder interactionText = new StringBuilder();

		List<Drug> list = null;

		if (listname.equals(DrugService.PROPERTY_DRUG_TAKING)) {
			list = repository.findUserDrugsTaking(userService.getCurrentUser().getId());
		} else if (listname.equals(DrugService.PROPERTY_DRUG_REMEMBER)) {
			list = repository.findUserDrugsRemembered(userService.getCurrentUser().getId());
		} else {
			list = new ArrayList<Drug>();
		}

		for (final Drug drug : list) {
			for (final Interaction interaction : drug.getInteraction()) {
				for (final Drug drugCompare : list) {
					if (interaction.getInteractionDrug().contains(drugCompare)) {
						interactionText.append("<p>" + drug.getName() + "</p> - " + drugCompare.getName() + ": "
								+ interaction.getInteraction() + "</p>");
					}
				}
			}
		}

		return interactionText.toString();
	}

	public List<Drug> findUserDrugsTaking(User user) {

		user = userService.getUserById(user.getId());

		final List<Drug> drugs = repository.findUserDrugsTaking(user.getId());
		final List<Drug> remembered = repository.findUserDrugsRemembered(user.getId());

		for (Drug drug : drugs) {
			drug.setIsTaken(true);

			if (remembered.contains(drug)) {
				drug.setIsRemembered(true);
			}

			drug = this.tailoringService.tailorDrugToUser(drug, user);
		}

		return drugs;
	}

	public List<Drug> findUserDrugsRemembered(User user) {

		user = userService.getUserById(user.getId());

		final List<Drug> taking = repository.findUserDrugsTaking(user.getId());
		final List<Drug> drugs = repository.findUserDrugsRemembered(user.getId());

		for (Drug drug : drugs) {
			drug.setIsRemembered(true);

			if (taking.contains(drug)) {
				drug.setIsTaken(true);
			}

			drug = tailoringService.tailorDrugToUser(drug, user);
		}

		return drugs;
	}

	public List<ItemInvocation> getClicksByUserId() {

		final List<ItemInvocation> list = invocationRepository.findInvocedDrugs(userService.getCurrentUser());
		LOG.info("Retreived last visited items={}", list);

		// sort by Date

		Collections.sort(list, new Comparator<ItemInvocation>() {
			@Override
			public int compare(ItemInvocation invocation1, ItemInvocation invocation2) {

				if (invocation1.getCounter() == invocation2.getCounter())
					return invocation1.getTimestamp().compareTo(invocation2.getTimestamp());

				if (invocation1.getCounter() < invocation2.getCounter())
					return 1;

				return -1;
			}
		});

		if (list.size() > 6)
			return list.subList(0, 6);

		return list;
	}
}