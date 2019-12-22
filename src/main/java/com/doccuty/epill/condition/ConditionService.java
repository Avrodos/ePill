package com.doccuty.epill.condition;

import com.doccuty.epill.model.util.ConditionSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConditionService {
    @Autowired
    ConditionRepository repository;

    public ConditionSet getAllConditions() {
        List<Condition> list = repository.findAll();
        ConditionSet set = new ConditionSet();
        set.addAll(list);
        return set;
    }


    public Condition getConditionById(long id) {
        return repository.findOne(id);
    }

    public Condition getConditionByName(String name) {
        return repository.findByName(name);
    }

    public void addCondition(Condition condition) {
        if (!conditionExistsByName(condition.getName())) {
            repository.save(condition);
        }
    }

    public boolean conditionExistsByName(String name) {
        ConditionSet allConditions = getAllConditions();
        allConditions = allConditions.filterName(name);
        return allConditions.size() > 0;
    }
}
