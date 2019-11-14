package com.doccuty.epill.userdrugplan;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.doccuty.epill.user.User;

@Repository
@Transactional
public interface UserDrugPlanRepository extends JpaRepository<UserDrugPlan, Long> {

	/**
	 * find all planned drugs for user
	 *
	 * @param user
	 * @return
	 */
	List<UserDrugPlan> findByUser(User user);

	/**
	 * find all planned drugs for user between two dates
	 * 
	 * @param user
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("SELECT p FROM UserDrugPlan p WHERE p.user.id = :userid AND p.dateTimePlanned >= :startDate AND p.dateTimePlanned <= :endDate ORDER BY p.dateTimePlanned")
	List<UserDrugPlan> findByUserBetweenDates(@Param("userid") long id, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate);

	/**
	 * delete all planned drugs for user between two dates
	 * 
	 * @param user
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Transactional
	@Modifying
	@Query("DELETE FROM UserDrugPlan p WHERE p.user.id = :userid AND p.dateTimePlanned >= :startDate AND p.dateTimePlanned <= :endDate")
	void deleteByUserBetweenDates(@Param("userid") long id, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate);
}