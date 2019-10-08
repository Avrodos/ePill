package com.doccuty.epill.userdrugplan;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.doccuty.epill.userdrugplan.FoodToAvoid;



public interface FoodToAvoidRepository extends JpaRepository<FoodToAvoid, Long> {
	
	@Query("SELECT food FROM FoodToAvoid food ORDER BY food.foodToAvoid")
	List<FoodToAvoid> findAllOrderByName();
	
	@Query("SELECT food FROM FoodToAvoid food WHERE food.foodToAvoid = :value")
	List<FoodToAvoid> findByName(@Param(value = "value") String value);

	@Query("SELECT NEW FoodToAvoid(food.id, food.foodToAvoid) FROM FoodToAvoid food ORDER BY food.foodToAvoid")
	List<FoodToAvoid> findAllSimple();
	
}
