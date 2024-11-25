package com.cydeo.entity.common;

import com.cydeo.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//todo this class takes user credentials from your db and maps it to generic User class by Spring
//so spring allows you to use those credentials in UI.
public class UserPrincipal implements UserDetails {

    //todo my entity, my user
    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorityList = new ArrayList<>();
        //we get user role from db and add in generic User from Spring
        GrantedAuthority authority = new SimpleGrantedAuthority(this.user.getRole().getDescription());
        authorityList.add(authority);
        return authorityList;
    }

    //get password from db and map to Spring password
    @Override
    public String getPassword() {
        return this.user.getPassWord();
    }

    //get username from db and map to Spring username
    @Override
    public String getUsername() {
        return this.user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled();
    }

    public Long getId(){
        return this.user.getId();
    }
}
