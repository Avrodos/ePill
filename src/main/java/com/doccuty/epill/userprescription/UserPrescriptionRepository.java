package com.doccuty.epill.userprescription;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.doccuty.epill.drug.Drug;
import com.doccuty.epill.user.User;

@Repository
@Transactional
public interface UserPrescriptionRepository extends JpaRepository<UserPrescription, Long> {

	/**
	 * find prescription by user and drug 
	 * @param user
	 * @param drug
	 * @return
	 */
	@Query("SELECT p FROM UserPrescription p WHERE p.user.id = :userid AND p.drug.id = :drugid")
	public List<UserPrescription> findPrescriptions(@Param("userid") long userId, @Param("drugid") long drugId);
	//public List<UserPrescription> findByUser(User user, Drug drug);
	
	/**
	 * delete prescription items by userprescriptionid
	 * @param userPrescriptionId
	 * @return
	 */
	@Transactional
	@Modifying
	@Query("DELETE FROM UserPrescriptionItem i WHERE i.userPrescription.id = :userprescriptionid")
	public void deleteUserPrescriptionItems(@Param("userprescriptionid") long userPrescriptionId);
	
	/**
	 * delete prescription items by userprescriptionid
	 * @param userPrescriptionId
	 * @return
	 */
	@Transactional
	@Modifying
	@Query("DELETE FROM UserPrescription p WHERE p.id = :userprescriptionid")
	public void deletePrescription(@Param("userprescriptionid") long userPrescriptionId);
}

