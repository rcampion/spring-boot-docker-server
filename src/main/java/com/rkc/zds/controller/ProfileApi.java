package com.rkc.zds.controller;

import com.rkc.zds.api.exception.ResourceNotFoundException;
import com.rkc.zds.model.ProfileData;
import com.rkc.zds.core.user.FollowRelation;
import com.rkc.zds.dto.ArticleFollowDto;
import com.rkc.zds.dto.UserDto;
import com.rkc.zds.repository.UserRepository;
import com.rkc.zds.repository.ArticleFollowRepository;
import com.rkc.zds.service.ProfileQueryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;
@CrossOrigin(origins = "http://www.zdslogic-development.com:4200")
@RestController
@RequestMapping(path = "/api/profiles/{userName:.+}")
public class ProfileApi {
    private ProfileQueryService profileQueryService;
    private UserRepository userRepository;

    @Autowired
    ArticleFollowRepository followRepository;
    
    @Autowired
    public ProfileApi(ProfileQueryService profileQueryService, UserRepository userRepository) {
        this.profileQueryService = profileQueryService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity getProfile(@PathVariable("userName") String userName) {
    	
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String userLogin = authentication.getName();

		Optional<UserDto> userDto = userRepository.findByUserName(userLogin);
		
		UserDto user = null;
		
		if(userDto.isPresent()) {
			user = userDto.get();
		}
		
    	return profileQueryService.findByUserName(userName, user)
            .map(this::profileResponse)
            .orElseThrow(ResourceNotFoundException::new);
    }

    //@PostMapping(path = "follow")
	@RequestMapping(value = "follow", method = RequestMethod.POST, consumes = {
	"application/json;charset=UTF-8" }, produces = { "application/json;charset=UTF-8" })

    public ResponseEntity follow(@PathVariable("userName") String userName) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String userLogin = authentication.getName();

		Optional<UserDto> userDto = userRepository.findByUserName(userLogin);
		
		UserDto user = null;
		UserDto target = null;
		
		if(userDto.isPresent()) {
			user = userDto.get();
		}
    	
    	Optional<UserDto> targetDto = userRepository.findByUserName(userName);
    	
		if(targetDto.isPresent()) {
			target = targetDto.get();
		}    	

		ArticleFollowDto follow = new ArticleFollowDto();
		follow.setUserId(user.getId());
		follow.setFollowId(target.getId());
		
		followRepository.save(follow);
		
		return profileResponse(profileQueryService.findByUserName(userName, user).get());
		
        /* 
        return userRepository.findByUserName(userName).map(target -> {
            FollowRelation followRelation = new FollowRelation(user.getId(), target.getId());
            userRepository.saveRelation(followRelation);
            return profileResponse(profileQueryService.findByUserName(userName, user).get());
        }).orElseThrow(ResourceNotFoundException::new);
        */
    }

    //@DeleteMapping(path = "follow")
	@RequestMapping(value = "follow", method = RequestMethod.DELETE, consumes = {
	"application/json;charset=UTF-8" }, produces = { "application/json;charset=UTF-8" })
    public ResponseEntity unfollow(@PathVariable("userName") String userName) {
 
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String userLogin = authentication.getName();

		Optional<UserDto> userDto = userRepository.findByUserName(userLogin);
		
		UserDto user = null;
		UserDto target = null;
		
		if(userDto.isPresent()) {
			user = userDto.get();
		}
    	
    	Optional<UserDto> targetDto = userRepository.findByUserName(userName);
    	
		if(targetDto.isPresent()) {
			target = targetDto.get();
		}    	

		ArticleFollowDto follow = followRepository.findByUserIdAndFollowId(user.getId(), target.getId());
		
		followRepository.delete(follow);
		
		return profileResponse(profileQueryService.findByUserName(userName, user).get());
/*
		Optional<UserDto> userOptional = userRepository.findByUserName(userName);
        if (userOptional.isPresent()) {
            UserDto target = userOptional.get();
            return userRepository.findRelation(user.getId(), target.getId())
                .map(relation -> {
                    userRepository.removeRelation(relation);
                    return profileResponse(profileQueryService.findByUserName(userName, user).get());
                }).orElseThrow(ResourceNotFoundException::new);
        } else {
            throw new ResourceNotFoundException();
        }
*/
    }

    private ResponseEntity profileResponse(ProfileData profile) {
        return ResponseEntity.ok(new HashMap<String, Object>() {{
            put("profile", profile);
        }});
    }

}
