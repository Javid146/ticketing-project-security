package com.cydeo.service;
import org.springframework.security.core.userdetails.UserDetailsService;

//create security to pass user entity from db to Spring UserDto and from there to UI
public interface SecurityService extends UserDetailsService {
    //need to extend UserDetailsService because Spring understands this way
}
