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

    //method below is example, we manually create user (ozzy, mike) and password. At real work user comes from db
    @Bean                                        //todo PasswordEncoder comes from TicketingProjectOrmApplication class bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder){ //todo this spring interface has loadByUsername method that takes user you created from db and adds to UI

        List<UserDetails> userList =  new ArrayList<>(); //spring interface that is implemented by spring generic class object User. takes our user details and maps to Spring user object and sends to UI

        userList.add(//new User is from spring, not created by us.
                new User("mike", encoder.encode("password"), List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));
        userList.add( //encoder used because spring does not accept plain passsword
                new User("ozzy", encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER"))));

        //user details are kept in memory not in db. this is usually not case in real job
        return new InMemoryUserDetailsManager(userList);
    }

    //filter that decides who can have access of which pages, which role and which restrictions
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeRequests()//authorizeRequests work with endpoints in controllers or directories
//                .antMatchers("/user/**").hasRole("Admin")
                .antMatchers("/user/**").hasAuthority("Admin")//admin has access to all method within user controller
                .antMatchers("/project/**").hasAuthority("Manager")
                .antMatchers("/task/employee/**").hasAuthority("Employee")
                .antMatchers("/task/**").hasAuthority("Manager")
//                .antMatchers("/task/**").hasAnyRole("EMPLOYEE","ADMIN") //can be access by employee and admin
//                .antMatchers("task/**").hasAuthority("ROLE_EMPLOYEE")

                .antMatchers( //everything inside following dirs. accessible to all
                       "/",
                       "/login",
                       "/fragments/**",
                       "/assets/**",
                       "/images/**"
                ).permitAll() //everyone can have access to dirs above
                .anyRequest().authenticated()
                .and()
//                .httpBasic() //this is just popup box from spring that asks for credentials for authentication
                .formLogin().loginPage("/login")  //todo use this login page for authentication
//                    .defaultSuccessUrl("/welcome") //in successful login go to this page
                    .successHandler(authSuccessHandler)
                    .failureUrl("/login?error=true") //brings this page is login fails
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
