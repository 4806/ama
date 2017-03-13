package org.sysc.ama.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.sysc.ama.model.User;

import java.util.Collection;

import org.springframework.security.core.authority.AuthorityUtils;
import org.sysc.ama.model.User;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private User user;

    public CustomUserDetails(User user) {
        super(user.getName(), user.getPasswordHash(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    public User.Role getRole() {
        return user.getRole();
    }
}