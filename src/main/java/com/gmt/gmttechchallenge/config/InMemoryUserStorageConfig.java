package com.gmt.gmttechchallenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static java.util.Arrays.asList;

// todo maybe replace this with actual users
@Configuration
public class InMemoryUserStorageConfig {


    /*
        In a real world situation password would never be plain text so besides from needing an extra implementation to
        have users stored somewhere else, it would be also required to add configuration to use BCrypt for password
        encryption.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails basicUser = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        UserDetails adminUser = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("password")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(asList(basicUser, adminUser));
    }
}
