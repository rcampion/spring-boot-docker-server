package com.rkc.zds.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.rkc.zds.dto.ContactDto;

import com.rkc.zds.util.SearchCriteria;

public interface ContactService {

    Page<ContactDto> findContacts(Pageable pageable);

    Page<ContactDto> searchContacts(String name);
    
    Page<ContactDto> searchContacts(Pageable pageable, List<SearchCriteria> params);
    
	Page<ContactDto> searchContacts(Pageable pageable, Specification<ContactDto> spec);

    Page<ContactDto> findFilteredContacts(Pageable pageable, int groupId);
    
    @Transactional     
    ContactDto getContact(int id);    

    @Transactional    
    public void saveContact(ContactDto contact);

    @Transactional    
    public void updateContact(ContactDto contact);

    @Transactional  
	void deleteContact(int id);

}
