package com.cydeo.config;

import com.cydeo.service.SecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    private final SecurityService securityService;
    private final AuthSuccessHandler authSuccessHandler;

    public SecurityConfig(SecurityService securityService, AuthSuccessHandler authSuccessHandler) {
        this.securityService = securityService;
        this.authSuccessHandler = authSuccessHandler;
    }

    //code below is just example. todo user needs to come from repository db. so user data should not be in this class
//    @Bean //spring created service for us by Spring, takes user details from db and uses it for login and else
    //encoder comes from TicketingProjectOrmApplication class as Bean
//    public UserDetailsService userDetailsService(PasswordEncoder encoder){
//
//        List<UserDetails> userList =  new ArrayList<>();
//
//        userList.add(//User comes from Spring's org.springframework.security.core.userdetails.User;
//                new User("mike", encoder.encode("password"), List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));
//        userList.add(
//                new User("ozzy", encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER"))));
//
//        //to keep users in memory not database
//        return new InMemoryUserDetailsManager(userList);
//    }

    @Bean //what page should have restriction, where certain users has access and stuff, basically filter for access
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeRequests()
                .antMatchers("/user/**").hasRole("Admin")
                .antMatchers("/user/**").hasAuthority("Admin")
                .antMatchers("/project/**").hasAuthority("Manager")
                .antMatchers("/task/employee/**").hasAuthority("Employee")
                .antMatchers("/task/**").hasAuthority("Manager")
//                .antMatchers("/task/**").hasAnyRole("EMPLOYEE","ADMIN")
//                .antMatchers("task/**").hasAuthority("ROLE_EMPLOYEE")

                //everybody should have access to these pages (endpoints from controller)
                .antMatchers(
                       "/",
                       "/login",
                       "/fragments/**",
                       "/assets/**",
                       "/images/**"
                ).permitAll() //everybody should have access to these pages (endpoints from controller)
                .anyRequest().authenticated()
                .and()
//                .httpBasic()
                .formLogin()//means use this login page when website opens. which is in LoginController class
                    .loginPage("/login")
//                    .defaultSuccessUrl("/welcome") //if login successful go to /welcome page
                    .successHandler(authSuccessHandler)
                    .failureUrl("/login?error=true") //if failed go to this page
                    .permitAll()
                .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login")
                .and()
                .rememberMe()
                    .tokenValiditySeconds(120)
                    .key("cydeo")
                    .userDetailsService(securityService)
                .and().build();

    }



















}
