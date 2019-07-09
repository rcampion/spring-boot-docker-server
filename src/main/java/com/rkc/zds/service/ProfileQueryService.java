package com.rkc.zds.service;

import com.rkc.zds.model.ProfileData;
import com.rkc.zds.model.UserData;
import com.rkc.zds.dto.UserDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProfileQueryService {
    private UserReadService userReadService;
    private UserRelationshipQueryService userRelationshipQueryService;

    @Autowired
    public ProfileQueryService(UserReadService userReadService, UserRelationshipQueryService userRelationshipQueryService) {
        this.userReadService = userReadService;
        this.userRelationshipQueryService = userRelationshipQueryService;
    }

    public Optional<ProfileData> findByUserName(String userName, UserDto currentUser) {
        UserData userData = userReadService.findByUserName(userName);
        if (userData == null) {
            return Optional.empty();
        } else {
            ProfileData profileData = new ProfileData(
                userData.getId(),
                userData.getUserName(),
                userData.getBio(),
                userData.getImage(),
                userRelationshipQueryService.isUserFollowing(currentUser.getId(), userData.getId()));
            return Optional.of(profileData);
        }
    }
}
