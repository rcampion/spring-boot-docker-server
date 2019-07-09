package com.rkc.zds.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.rkc.zds.core.user.FollowRelation;
import com.rkc.zds.dto.UserDto;

public interface UserRepository extends JpaRepository<UserDto, Integer>, JpaSpecificationExecutor<UserDto>{
	//UserDto findByUserName(String userName);

	UserDto findByLogin(String login);
	
    // void save(UserDto user);

    Optional<UserDto> findById(String id);

    Optional<UserDto> findByUserName(String userName);

    Optional<UserDto> findByEmail(String email);

//    void saveRelation(FollowRelation followRelation);

//    Optional<FollowRelation> findRelation(Integer userId, Integer targetId);

//    void removeRelation(FollowRelation followRelation);
}
