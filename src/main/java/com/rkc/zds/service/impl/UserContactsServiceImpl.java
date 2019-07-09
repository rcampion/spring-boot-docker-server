package com.rkc.zds.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rkc.zds.dto.ContactDto;
import com.rkc.zds.dto.UserContactDto;
import com.rkc.zds.repository.ContactRepository;
import com.rkc.zds.repository.UserContactsRepository;
import com.rkc.zds.service.UserContactsService;

@Service
public class UserContactsServiceImpl implements UserContactsService {

	@Autowired
	private ContactRepository contactRepo;

	@Autowired
	private UserContactsRepository userContactsRepo;

	@Override
	public Page<UserContactDto> findUserContacts(Pageable pageable, int id) {

		Page<UserContactDto> page = userContactsRepo.findByUserId(pageable, id);

		return page;
	}

	@Override
	public List<UserContactDto> findAllUserContacts(int userId) {

		List<UserContactDto> list = userContactsRepo.findByUserId(userId);

		return list;
	}

	@Override
	public Page<ContactDto> findFilteredContacts(Pageable pageable, int userId) {

		List<ContactDto> contacts = contactRepo.findAll();

		List<UserContactDto> userContactsList = userContactsRepo.findByUserId(userId);

		List<ContactDto> testList = new ArrayList<ContactDto>();

		List<ContactDto> filteredList = new ArrayList<ContactDto>();

		// build member list of Contacts
		Optional<ContactDto> contact;
		for (UserContactDto element : userContactsList) {
			contact = contactRepo.findById(element.getContactId());
			testList.add(contact.get());
		}

		// check member list of Contacts
		for (ContactDto element : contacts) {
			// if the contact is in the members list, ignore it
			if (!testList.contains(element)) {
				filteredList.add(element);
			}
		}

		int size = filteredList.size();
		if (size == 0) {
			size = 1;
		}

		PageRequest pageRequest = PageRequest.of(0, size);

		PageImpl<ContactDto> page = new PageImpl<ContactDto>(filteredList, pageRequest, size);

		return page;
	}

	private Sort sortByIdASC() {
		return new Sort(Sort.Direction.ASC, "userId");
	}

	@Override
	public void saveUserContact(UserContactDto userContact) {
		// checking for duplicates
		List<UserContactDto> list = userContactsRepo.findByUserId(userContact.getUserId());

		// return if duplicate found
		for (UserContactDto element : list) {
			if (element.getContactId() == userContact.getContactId()) {
				return;
			}
		}

		userContactsRepo.save(userContact);
	}

	@Override
	public void deleteUserContact(int id) {

		userContactsRepo.deleteById(id);

	}
}
