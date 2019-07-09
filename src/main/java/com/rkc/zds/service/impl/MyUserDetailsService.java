package com.rkc.zds.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rkc.zds.dto.UserDto;
import com.rkc.zds.service.UserService;
import com.rkc.zds.dto.AuthorityDto;

@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException {

		UserDto user = userService.findByUserName(userName);
		if (user != null) {
			List<GrantedAuthority> authorities = buildUserAuthority(user.getAuthorities());

			return buildUserForAuthentication(user, authorities);
		} else {
//			return new User(userName, userName, false, false, false, false, null);
            throw new UsernameNotFoundException("User "+userName+" not found");
		}
	}

	// Converts UserDto user to
	// org.springframework.security.core.userdetails.User
	private User buildUserForAuthentication(UserDto user, List<GrantedAuthority> authorities) {
		return new User(user.getUserName(), user.getPassword(), user.isEnabled(), true, true, true, authorities);
	}
/*
	private List<GrantedAuthority> buildUserAuthority(Set<AuthorityDto> authorityDtos) {

		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

		// Build user's authorities
		for (AuthorityDto authorityDto : authorityDtos) {
			setAuths.add(new SimpleGrantedAuthority(authorityDto.getAuthority()));
		}

		List<GrantedAuthority> result = new ArrayList<GrantedAuthority>(setAuths);

		return result;
	}
*/
	
	private List<GrantedAuthority> buildUserAuthority(List<AuthorityDto> authorityDtos) {

		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

		// Build user's authorities
		for (AuthorityDto authorityDto : authorityDtos) {
			setAuths.add(new SimpleGrantedAuthority(authorityDto.getAuthority()));
		}

		List<GrantedAuthority> result = new ArrayList<GrantedAuthority>(setAuths);

		return result;
	}
}