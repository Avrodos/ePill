package com.doccuty.epill.allergy;

import com.doccuty.epill.model.util.AllergySet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllergyService {
    @Autowired
    AllergyRepository repository;

    public AllergySet getAllAllergies() {
        List<Allergy> list = repository.findAll();
        AllergySet set = new AllergySet();
        set.addAll(list);
        return set;
    }


    public Allergy getAllergyById(long id) {
        return repository.findOne(id);
    }

    public Allergy getAllergyByName(String name) {
        return repository.findByName(name);
    }

    public void addAllergy(Allergy allergy) {
        if (!allergyExistsByName(allergy.getName())) {
            repository.save(allergy);
        }
    }

    public boolean allergyExistsByName(String name) {
        /*AllergySet allAllergies = getAllAllergies();
        allAllergies = allAllergies.filterName(name);
        if (allAllergies.size() > 0) {
            return true;
        }
        return false;

         */
        return getAllergyByName(name) != null;
    }

}
