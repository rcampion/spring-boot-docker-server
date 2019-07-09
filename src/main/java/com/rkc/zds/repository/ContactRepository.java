package com.rkc.zds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.rkc.zds.dto.ContactDto;

public interface ContactRepository extends JpaRepository<ContactDto, Integer>, JpaSpecificationExecutor<ContactDto> {
  
	Page<ContactDto> findByLastNameIgnoreCaseLike(Pageable pageable, String lastName);
	 
}
