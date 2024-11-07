package com.application.signup.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
	    return new BCryptPasswordEncoder();
	}

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	return http.csrf(
    				csrf -> csrf.disable())
              .authorizeHttpRequests(
            		   auth -> auth
            		   		.requestMatchers("/", "/v3/**", "/swagger-ui/**","/swagger/**").permitAll()
            		   		.anyRequest().authenticated())
              .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))
              .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
              .httpBasic(Customizer.withDefaults())
              .build();
	}
    
    @Bean
	UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {
	    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
	    manager.createUser(User.withUsername("admin")
	    	.password(bCryptPasswordEncoder.encode("admin"))
	    	.roles("ADMIN")
	      	.build());
	    return manager;
	}

}
