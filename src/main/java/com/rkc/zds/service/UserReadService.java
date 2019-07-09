package com.rkc.zds.service;

import com.rkc.zds.model.UserData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Mapper
@Service
public interface UserReadService {

    UserData findByUserName(@Param("userName") String userName);

    UserData findById(@Param("id") Integer integer);
}

