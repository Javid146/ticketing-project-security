package com.cydeo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TicketingProjectOrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketingProjectOrmApplication.class, args);
	}

	//todo if you did not create a class like 2 below, it should have @Bean annot and should be in TicketingProjectOrmApplication class
	@Bean
	public ModelMapper mapper(){
		return new ModelMapper();
	}

	//todo password encoder class, to encode my password, because Spring only accepts encoded code. it is used only for UI
	//at work will not be used probably
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
}


/*todo to introduce our user we create config package*/