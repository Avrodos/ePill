package com.doccuty.epill.user;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface UserRepository extends SimpleUserRepository<User> {

	@Modifying(clearAutomatically = true)
	@Query("UPDATE User u SET u.preferredFontSize = :preferredFontSize WHERE u.id = :id")
	int updateSettings(@Param("id") int id, @Param("preferredFontSize") int preferredFontSize);

	@Query("SELECT u FROM User u WHERE u.id = :id")
	User findUserById(@Param("id") long id);
}