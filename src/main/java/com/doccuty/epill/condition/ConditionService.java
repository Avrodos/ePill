package com.doccuty.epill.condition;

import com.doccuty.epill.model.util.ConditionSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConditionService {
    @Autowired
    ConditionRepository repository;

    public ConditionSet getAllAllergies() {
        return (ConditionSet) repository.findAll();
    }


    public Condition getConditionById(long id) {
        return repository.findOne(id);
    }

    public void addCondition(Condition condition) {
        repository.save(condition);
    }
}
