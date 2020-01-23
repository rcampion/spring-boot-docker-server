package com.rkc.zds.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rkc.zds.dto.ContactDto;
import com.rkc.zds.dto.GroupDto;
import com.rkc.zds.dto.GroupMemberDto;
import com.rkc.zds.repository.GroupRepository;
import com.rkc.zds.repository.GroupMemberRepository;
import com.rkc.zds.service.GroupService;

@Service
public class GroupServiceImpl implements GroupService {
	private static final int PAGE_SIZE = 50;

	@Autowired
	private GroupRepository groupRepo;

	@Autowired
	private GroupMemberRepository groupMemberRepo;
	
	@Override
	public Page<GroupDto> findGroups(Pageable pageable) {

		int pageNumber = pageable.getPageNumber();

		PageRequest request = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.DESC, "groupName");

		return groupRepo.findAll(pageable);
	}

	@Override
	public GroupDto getGroup(int id) {
		
		Optional<GroupDto> group = groupRepo.findById(id);
		
		return group.get();

	}

	@Override
	public Page<GroupMemberDto> findGroupMembers(int id) {
		GroupDto group = groupRepo.getOne(id);

		final PageRequest pageRequest = PageRequest.of(0, 10, sortByNameASC());

		Page page = groupMemberRepo.findByGroupId(pageRequest, id );

		return page;
	}
	
	@Override
	public Page<GroupDto> searchGroups(String name) {

		final PageRequest pageRequest = PageRequest.of(0, 10, sortByNameASC());

		return groupRepo.findByGroupNameLike(pageRequest, "%" + name + "%");
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void saveGroup(GroupDto group) {
		groupRepo.save(group);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void updateGroup(GroupDto group) {
		groupRepo.saveAndFlush(group);
	}

	// @Transactional
	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteGroup(int groupId) {
		
		//delete all group members for this group prior to deleting group.		
		List<GroupMemberDto> list = groupMemberRepo.findByGroupId(groupId);
		
		for(GroupMemberDto element : list){
			groupMemberRepo.delete(element);
		}
		
		groupRepo.deleteById(groupId);
	}

	private Sort sortByNameASC() {
		return Sort.by(Sort.Direction.ASC, "groupName");
	}
	
	@Override
	public Page<GroupDto> searchGroups(Pageable pageable, Specification<GroupDto> spec) {
		return groupRepo.findAll(spec, pageable);
	}
}
