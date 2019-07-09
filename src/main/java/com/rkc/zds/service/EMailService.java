package com.rkc.zds.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.rkc.zds.dto.ContactDto;
import com.rkc.zds.dto.EMailDto;
import com.rkc.zds.model.EMailSend;

public interface EMailService {
    Page<EMailDto> findEMails(Pageable pageable, int contactId);
    
    @Transactional     
    EMailDto getEMail(int id);  

    @Transactional    
    public void saveEMail(EMailDto email);
    
    @Transactional    
    public void updateEMail(EMailDto email);

    @Transactional  
	void deleteEMail(int id);

	void sendEMail(EMailSend emailSend);
}
