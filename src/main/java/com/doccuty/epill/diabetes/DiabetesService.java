package com.doccuty.epill.diabetes;

import com.doccuty.epill.authentication.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Handle all CRUD operations for posts.
 */
@Service
public class DiabetesService {

    private static final Logger LOG = LoggerFactory.getLogger(DiabetesService.class);

    @Autowired
    DiabetesRepository repository;


    @Autowired
    AuthenticationService authenticationService;

    public List<Diabetes> getAllDiabetes() {
        return (List<Diabetes>) repository.findAll();
    }


    public Diabetes saveDiabetes(Diabetes diabetes) {
        LOG.info("Saved diabetes={}", diabetes);
        return repository.save(diabetes);
    }

    public Diabetes getDiabetesById(int id) {
        return repository.findOne(id);
    }
}
