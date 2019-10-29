package com.doccuty.epill.userdrugplan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.doccuty.epill.userdrugplan.DrugInstructions;

public interface DrugInstructionsRepository extends JpaRepository<DrugInstructions, Long>{
	@Query("SELECT instr FROM DrugInstructions instr ORDER BY instr.instructions")
	List<FoodToAvoid> findAllOrderByName();
	
	@Query("SELECT instr FROM DrugInstructions instr WHERE instr.instructions = :value")
	List<DrugInstructions> findByName(@Param(value = "value") String value);

	@Query("SELECT NEW DrugInstructions(instr.id, instr.instructions) FROM DrugInstructions instr ORDER BY instr.instructions")
	List<DrugInstructions> findAllSimple();
}
