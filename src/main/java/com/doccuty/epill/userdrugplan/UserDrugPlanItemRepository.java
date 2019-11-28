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
public interface UserDrugPlanItemRepository extends JpaRepository<UserDrugPlanItem, Long> {

	/**
	 * find all planned drugs for user
	 *
	 * @param user
	 * @return
	 */
	List<UserDrugPlanItem> findByUser(User user);

	/**
	 * find all planned drugs for user between two dates
	 * 
	 * @param user
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("SELECT p FROM UserDrugPlanItem p WHERE p.user.id = :userid AND p.dateTimePlanned >= :startDate AND p.dateTimePlanned <= :endDate ORDER BY p.dateTimePlanned")
	List<UserDrugPlanItem> findByUserBetweenDates(@Param("userid") long userid, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate);
	
	
	
	/**
	 * find not taken drugs for user between two date times 
	 * 
	 * @param user
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("SELECT p FROM UserDrugPlanItem p WHERE p.drugTaken=false AND p.user.id = :userid AND p.dateTimePlanned >= :startDate AND p.dateTimePlanned <= :endDate ORDER BY p.dateTimePlanned")
	List<UserDrugPlanItem> findNotTakenBetweenDates(@Param("userid") long userid, @Param("startDate") Date startDate,
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
	@Query("DELETE FROM UserDrugPlanItem p WHERE p.user.id = :userid AND p.dateTimePlanned >= :startDate AND p.dateTimePlanned <= :endDate")
	void deleteByUserBetweenDates(@Param("userid") long id, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate);
	
	/**
	 * update drug taken for UserDrugPlanItem 
	 * 
	 * @param userDrugPlanItemId
	 * @param isTaken
	 * @return
	 */
	@Transactional
	@Modifying
	@Query("UPDATE UserDrugPlanItem SET drugTaken= :isTaken WHERE id = :userDrugPlanItemId")
	void updateDrugTaken(@Param("userDrugPlanItemId") long userDrugPlanItemId, @Param("isTaken") Boolean isTaken);
}