package com.rkc.zds.service;

import com.rkc.zds.model.UserData;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserQueryService  {
    private UserReadService userReadService;

    public UserQueryService(UserReadService userReadService) {
        this.userReadService = userReadService;
    }

    public Optional<UserData> findById(Integer integer) {
        return Optional.ofNullable(userReadService.findById(integer));
    }
}

