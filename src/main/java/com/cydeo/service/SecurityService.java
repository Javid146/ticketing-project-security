package com.cydeo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

//create security to pass user entity from db to Spring UserDto and from there to UI
@Service
public interface SecurityService extends UserDetailsService {
//need to extend UserDetailsService because Spring understands this way
}
