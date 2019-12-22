package com.doccuty.epill.allergy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AllergyRepository extends JpaRepository<Allergy, Long> {

    @Query("SELECT a FROM Allergy a WHERE a.name = :name")
    Allergy findByName(@Param("name") String name);
}
