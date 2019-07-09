package com.rkc.zds.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.rkc.zds.dto.AuthorityDto;
import com.rkc.zds.dto.EMailDto;
import com.rkc.zds.dto.LoginDto;
import com.rkc.zds.dto.UserDto;
import com.rkc.zds.error.UserAlreadyExistException;

public interface UserService {
    Page<UserDto> findUsers(Pageable pageable);
    
    @Transactional
//	Optional<UserDto> findByUserName(String userName);
    
	UserDto findByUserName(String userName);
    
	UserDto findById(Integer id);

	List<UserDto> getUsers();
	
    UserDto getUser(int id);  
	
    @Transactional    
    public void updateUser(UserDto user);
    
    @Transactional  
	void deleteUser(int id);
    
    @Transactional    
    public void saveUser(UserDto user);

	UserDto registerNewUserAccount(UserDto accountDto) throws UserAlreadyExistException;

	Page<UserDto> searchUsers(Pageable pageable, Specification<UserDto> spec);

	UserDto changePassword(LoginDto loginDTO, HttpServletRequest request, HttpServletResponse response);

	Page<AuthorityDto> findAuthorities(Pageable pageable, String userName);

	AuthorityDto getAuthority(int id);
	
    public void saveAuthority(AuthorityDto role);
    
    public void updateAuthority(AuthorityDto authority);

	void deleteAuthority(int id);

}