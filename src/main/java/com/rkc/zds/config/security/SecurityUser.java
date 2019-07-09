package com.rkc.zds.config.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.rkc.zds.dto.Profile;

public class SecurityUser extends User{

	private static final long serialVersionUID = 1L;

	private int id;

    private Profile profile;

    public SecurityUser(String userName, String password, Collection<? extends GrantedAuthority> authorities) {
        super(userName, password, authorities);
    }

    public SecurityUser(Integer id,String userName, String password, Profile profile, Collection<? extends GrantedAuthority> authorities) {
        super(userName, password, authorities);
        this.id = id;
        this.profile = profile;
    }

    public int getId() {
        return id;
    }

    public Profile getProfile() {
        return profile;
    }
}
