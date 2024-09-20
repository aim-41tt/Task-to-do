package ru.example.task_to_do.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import ru.example.task_to_do.services.auth.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.authorizeHttpRequests((requests) -> requests.requestMatchers("/","/reg")
				.permitAll()
				.anyRequest()
				.authenticated()
				)
				.formLogin((form) -> form.loginPage("/login")
						.permitAll())
				.logout((logout) -> logout.permitAll());
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	 @Bean
	 public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) 
			 throws Exception {
		 return http.getSharedObject(AuthenticationManagerBuilder.class)
	                .userDetailsService(customUserDetailsService)
	                .passwordEncoder(passwordEncoder)
	                .and()
	                .build();
	    }
}

	  
