package com.doccuty.epill.intolerance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IntoleranceRepository extends JpaRepository<Intolerance, Long> {

    @Query("SELECT i FROM Intolerance i WHERE i.name = :name")
    Intolerance findByName(@Param("name") String name);
}
