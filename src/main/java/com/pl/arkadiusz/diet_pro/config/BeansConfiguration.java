package com.pl.arkadiusz.diet_pro.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pl.arkadiusz.diet_pro.services.impl.MyUserDetailsService;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeansConfiguration {

    private final PasswordEncoder passwordEncoder;

    private final MyUserDetailsService myUserDetailService;

    @Autowired
    public BeansConfiguration(PasswordEncoder passwordEncoder, MyUserDetailsService myUserDetailService) {
        this.passwordEncoder = passwordEncoder;
        this.myUserDetailService = myUserDetailService;
    }

    /**
     * Add ModelMapper to project as been. Can be autowired.
     *
     * @return ModelMapper
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }


    /**
     * Default authenticationProvider. Can be autowired.
     *
     * @return DaoAuthenticationProvider
     */

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(myUserDetailService);
        return provider;
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


}
