package com.doccuty.epill.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SimpleUserRepository<T extends SimpleUser> extends CrudRepository<T, Long> {
	
	@Query("SELECT new SimpleUser(u.id, u.firstname, u.lastname, u.username, u.password, u.salt, u.preferredFontSize, u.levelOfDetail, u.redGreenColorblind, u.gid, u.tpa, u.a7id, u.firstSignIn, u.sleepTime, u.dinnerTime, u.breakfastTime, u.lunchTime) FROM SimpleUser u WHERE u.username LIKE :username")
	T findByUsername(@Param(value = "username") String username);

    //	@Query("SELECT u FROM SimpleUser u WHERE u.username LIKE :username")
//	T findByUsername(@Param(value = "username") String username);

    @Query("SELECT u FROM SimpleUser u WHERE u.email = :email")
    T findByEmail(@Param("email") String email);

    //TODO: Can I use the other constructor?
    @Query("SELECT new SimpleUser(u.id, u.firstname, u.lastname, u.username, u.password, u.salt, u.preferredFontSize, u.levelOfDetail, u.redGreenColorblind, u.gid, u.tpa, u.a7id, u.firstSignIn, u.sleepTime, u.dinnerTime, u.breakfastTime, u.lunchTime) FROM SimpleUser u WHERE u.gid LIKE :gid")
    T findByGID(@Param(value = "gid") String gid);

    @Query("SELECT new SimpleUser(u.id, u.firstname, u.lastname, u.username, u.password, u.salt, u.preferredFontSize, u.levelOfDetail, u.redGreenColorblind, u.gid, u.tpa, u.a7id, u.firstSignIn, u.sleepTime, u.dinnerTime, u.breakfastTime, u.lunchTime) FROM SimpleUser u WHERE u.a7id LIKE :a7id")
    T findByA7ID(@Param(value = "a7id") String a7id);

    @Query("SELECT new SimpleUser(u.id, u.firstname, u.lastname) FROM SimpleUser u WHERE u.id = :id")
	T findOneSimple(@Param("id") long id);
}
