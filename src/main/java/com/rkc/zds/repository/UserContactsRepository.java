package com.rkc.zds.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rkc.zds.dto.UserContactDto;

public interface UserContactsRepository extends JpaRepository<UserContactDto, Integer> {
  
	Page<UserContactDto> findByUserId(Pageable pageable, int userId);

	List<UserContactDto> findByUserId(int userId);
	       
}