package com.doccuty.epill.intolerance;

import com.doccuty.epill.model.util.IntoleranceSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntoleranceService {
    @Autowired
    IntoleranceRepository repository;

    public IntoleranceSet getAllIntolerances() {
        List<Intolerance> list = repository.findAll();
        IntoleranceSet set = new IntoleranceSet();
        set.addAll(list);
        return set;
    }

    public Intolerance getIntoleranceById(long id) {
        return repository.findOne(id);
    }

    public Intolerance getIntoleranceByName(String name) {
        return repository.findByName(name);
    }


    public void addIntolerance(Intolerance intolerance) {
        if (!intoleranceExistsByName(intolerance.getName())) {
            repository.save(intolerance);
        }
    }

    public boolean intoleranceExistsByName(String name) {
        IntoleranceSet allIntolerances = getAllIntolerances();
        allIntolerances = allIntolerances.filterName(name);
        return allIntolerances.size() > 0;
    }
}
