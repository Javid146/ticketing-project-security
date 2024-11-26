package com.cydeo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

//todo this class is to define your page accessibility based on your role.
@Configuration
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //brings authority to users for page access
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if(roles.contains("Admin")){
            response.sendRedirect("/user/create");
        }

        if(roles.contains("Manager")){
            response.sendRedirect("/project/create");
        }

        if(roles.contains("Employee")){
            response.sendRedirect("/task/employee/pending-tasks");
        }
    }
}


















