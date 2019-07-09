package com.rkc.zds.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rkc.zds.dto.EMailDto;

public interface EMailRepository extends JpaRepository<EMailDto, Integer> {
  
	Page<EMailDto> findByContactId(Pageable pageable, int contactId);

	List<EMailDto> findByContactId(int contactId);
	       
}
