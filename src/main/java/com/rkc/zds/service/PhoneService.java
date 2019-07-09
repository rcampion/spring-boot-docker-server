package com.rkc.zds.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.rkc.zds.dto.PhoneDto;

public interface PhoneService {
    Page<PhoneDto> findPhones(Pageable pageable, int contactId);
    
    @Transactional     
    PhoneDto getPhone(int id);  

    @Transactional    
    public void savePhone(PhoneDto phone);
    
    @Transactional    
    public void updatePhone(PhoneDto phone);

    @Transactional  
	void deletePhone(int id);
}
