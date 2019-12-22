package com.doccuty.epill.condition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConditionRepository extends JpaRepository<Condition, Long> {
    @Query("SELECT c FROM Condition c WHERE c.name = :name")
    Condition findByName(@Param("name") String name);
}
