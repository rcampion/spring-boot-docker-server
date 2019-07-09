package com.rkc.zds.dto;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the PCM_GROUP_MEMBERS database table.
 * 
 */
@Entity
@Table(name="PCM_USER_CONTACTS")
public class UserContactDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="USER_ID")
	private int userId;

	@Column(name="CONTACT_ID")
	private int contactId;

    public UserContactDto() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getContactId() {
		return this.contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}
	
	@Override
	public int hashCode() {
	    int hash = 3;
	    hash = 53 * hash + (this.id);
	    return hash;
	}

	@Override
	public boolean equals(Object other) {
	    boolean result;
	    if((other == null) || (getClass() != other.getClass())){
	        result = false;
	    } // end if
	    else{
	        UserContactDto otherContact = (UserContactDto)other;
	        result = (id == (otherContact.id));
	    } // end else

	    return result;
	}
}
