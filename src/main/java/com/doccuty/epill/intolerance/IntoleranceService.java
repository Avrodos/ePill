package com.doccuty.epill.intolerance;

import com.doccuty.epill.model.util.IntoleranceSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntoleranceService {
    @Autowired
    IntoleranceRepository repository;

    public IntoleranceSet getAllAllergies() {
        return (IntoleranceSet) repository.findAll();
    }


    public Intolerance getIntoleranceById(long id) {
        return repository.findOne(id);
    }

    public void addIntolerance(Intolerance intolerance) {
        repository.save(intolerance);
    }
}
