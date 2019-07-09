package com.rkc.zds.service.impl;

import com.rkc.zds.dto.UserDto;
import com.rkc.zds.model.UserData;
import com.rkc.zds.repository.UserRepository;
import com.rkc.zds.service.UserReadService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserReadServiceImpl implements UserReadService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserData findByUserName(String userName) {

		Optional<UserDto> userDto = userRepository.findByUserName(userName);

		UserData data = new UserData();

		UserDto user = null;

		if (userDto.isPresent()) {
			user = userDto.get();

			data.setId(user.getId());
			data.setBio(user.getBio());
			data.setEmail(user.getEmail());
			data.setImage(user.getImage());
			data.setUserName(user.getUserName());
		}
		return data;

	}

	@Override
	public UserData findById(Integer id) {
		Optional<UserDto> user = userRepository.findById(id);

		UserDto dto = null;

		UserData data = new UserData();

		if (user.isPresent()) {
			dto = user.get();
			data.setId(dto.getId());
			data.setBio(dto.getBio());
			data.setEmail(dto.getEmail());
			data.setImage(dto.getImage());
			data.setUserName(dto.getUserName());
		}
		return data;
	}

}
