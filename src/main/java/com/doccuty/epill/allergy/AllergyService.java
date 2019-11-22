package com.doccuty.epill.allergy;

import com.doccuty.epill.model.util.AllergySet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AllergyService {
    @Autowired
    AllergyRepository repository;

    public AllergySet getAllAllergies() {
        return (AllergySet) repository.findAll();
    }


    public Allergy getAllergyById(long id) {
        return repository.findOne(id);
    }

    public void addAllergy(Allergy allergy) {
        repository.save(allergy);
    }
}
