package com.rkc.zds.service;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Mapper
@Service
public interface UserRelationshipQueryService {
    boolean isUserFollowing(@Param("userId") Integer integer, @Param("anotherUserId") Integer integer2);

    Set<Integer> followingAuthors(@Param("userId") Integer userId, @Param("ids") List<Integer> list);

    List<Integer> followedUsers(@Param("userId") Integer integer);
}
