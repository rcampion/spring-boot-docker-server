package com.rkc.zds.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rkc.zds.dto.PhoneDto;

public interface PhoneRepository extends JpaRepository<PhoneDto, Integer> {
  
	Page<PhoneDto> findByContactId(Pageable pageable, int contactId);

	List<PhoneDto> findByContactId(int contactId);
	       
}
