package com.rkc.zds.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.rkc.zds.dto.ContactDto;
import com.rkc.zds.dto.UserContactDto;

public interface UserContactsService {
    Page<UserContactDto> findUserContacts(Pageable pageable, int userId);

    Page<ContactDto> findFilteredContacts(Pageable pageable, int userId);    

    List<UserContactDto> findAllUserContacts(int userId);

    @Transactional    
    public void saveUserContact(UserContactDto userContact);    

    @Transactional  
	void deleteUserContact(int id);
}
