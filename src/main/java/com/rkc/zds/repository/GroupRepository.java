package com.rkc.zds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.rkc.zds.dto.GroupDto;

public interface GroupRepository extends JpaRepository<GroupDto, Integer>, JpaSpecificationExecutor<GroupDto> {
  
	Page<GroupDto> findByGroupNameLike(Pageable pageable, String groupName);
    
}
