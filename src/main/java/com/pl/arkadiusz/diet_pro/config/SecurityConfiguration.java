package com.pl.arkadiusz.diet_pro.config;

import com.pl.arkadiusz.diet_pro.Fisters.JWTAuthenticationFilter;
import com.pl.arkadiusz.diet_pro.Fisters.JWTAuthorizationFilter;
import com.pl.arkadiusz.diet_pro.services.impl.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final MyUserDetailsService myUserDetailsService;


    private final DaoAuthenticationProvider authenticationProvider;


    public SecurityConfiguration(MyUserDetailsService myUserDetailsService,  DaoAuthenticationProvider authenticationProvider) {
        this.myUserDetailsService = myUserDetailsService;
        this.authenticationProvider = authenticationProvider;
    }

    /**
     *  set user authentication via authentication provider as DaoAuthenticationProvider
     * @param  auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/h2-console", "/h2-console/**").permitAll() //permit h2 console for development
                .antMatchers("/", "/registration/**", "/account" , "/account/**").permitAll()
                .antMatchers("/user/**").permitAll()
                .anyRequest().authenticated().and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), myUserDetailsService))

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // disable spring session management


        http.csrf().disable();
        http.headers().frameOptions().disable();
    }


    /**
     * Open api on different platform access
     * @return
     */

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

}
