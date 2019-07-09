package com.rkc.zds.controller;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.rkc.zds.api.exception.InvalidRequestException;
import com.rkc.zds.model.UserWithToken;
import com.rkc.zds.model.UserData;
import com.rkc.zds.dto.UserDto;
import com.rkc.zds.repository.UserRepository;
import com.rkc.zds.service.UserQueryService;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://www.zdslogic-development.com:4200")
@RestController
@RequestMapping(path = "/api/user")
public class CurrentUserApi {
	private UserQueryService userQueryService;
	private UserRepository userRepository;

	@Autowired
	public CurrentUserApi(UserQueryService userQueryService, UserRepository userRepository) {
		this.userQueryService = userQueryService;
		this.userRepository = userRepository;
	}

	// @GetMapping
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity currentUser(@AuthenticationPrincipal UserDto currentUser,
//                                      @RequestHeader(value = "Authorization") String authorization) {
	public ResponseEntity currentUser(@RequestHeader(value = "Authorization") String authorization) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String userLogin = authentication.getName();

		Optional<UserDto> userDto = userRepository.findByUserName(userLogin);
		
		UserDto user = null;
		
		if(userDto.isPresent()) {
			user = userDto.get();
		}

		UserData userData = userQueryService.findById(user.getId()).get();

		//return ResponseEntity.ok(userResponse(new UserWithToken(userData, authorization.split(" ")[1])));
		return ResponseEntity.ok(user);
	}

	@PutMapping
	public ResponseEntity updateProfile(@AuthenticationPrincipal UserDto currentUser,
			@RequestHeader("Authorization") String token, @Valid @RequestBody UpdateUserParam updateUserParam,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException(bindingResult);
		}
		checkUniquenessOfUserNameAndEmail(currentUser, updateUserParam, bindingResult);

		currentUser.update(updateUserParam.getEmail(), updateUserParam.getUserName(), updateUserParam.getPassword(),
				updateUserParam.getBio(), updateUserParam.getImage());
		userRepository.save(currentUser);
		UserData userData = userQueryService.findById(currentUser.getId()).get();
		return ResponseEntity.ok(userResponse(new UserWithToken(userData, token.split(" ")[1])));
	}

	private void checkUniquenessOfUserNameAndEmail(UserDto currentUser, UpdateUserParam updateUserParam,
			BindingResult bindingResult) {
		
		UserDto byUserName = null;
		
		if (!"".equals(updateUserParam.getUserName())) {
			Optional<UserDto> userDto = userRepository.findByUserName(updateUserParam.getUserName());

			if(userDto.isPresent()) {
				byUserName = userDto.get();
			}
					
			if ((byUserName != null) && !byUserName.equals(currentUser)) {
				bindingResult.rejectValue("userName", "DUPLICATED", "userName already exist");
			}
		}

		if (!"".equals(updateUserParam.getEmail())) {
			Optional<UserDto> byEmail = userRepository.findByEmail(updateUserParam.getEmail());
			if (byEmail.isPresent() && !byEmail.get().equals(currentUser)) {
				bindingResult.rejectValue("email", "DUPLICATED", "email already exist");
			}
		}

		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException(bindingResult);
		}
	}

	private Map<String, Object> userResponse(UserWithToken userWithToken) {
		return new HashMap<String, Object>() {
			{
				put("user", userWithToken);
			}
		};
	}
}
/*
 * @Getter
 * 
 * @JsonRootName("user")
 * 
 * @NoArgsConstructor class UpdateUserParam {
 * 
 * @Email(message = "should be an email") private String email = ""; private
 * String password = ""; private String userName = ""; private String bio = "";
 * private String image = ""; }
 */
