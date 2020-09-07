package com.pl.arkadiusz.diet_pro.Fisters;

import com.auth0.jwt.algorithms.Algorithm;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.pl.arkadiusz.diet_pro.services.impl.MyUserDetailsService;
import gherkin.lexer.Da;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.pl.arkadiusz.diet_pro.security.SecurityConstants.*;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {


    MyUserDetailsService myUserDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, MyUserDetailsService myUserDetailsService) {
        super(authenticationManager);
        this.myUserDetailsService = myUserDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(HEADER_NAME);

        if (header == null || !header.startsWith(PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        if (authenticationToken != null)
            response.setHeader(HEADER_NAME, header);
        chain.doFilter(request, response);
    }


    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_NAME);

        if (token != null) {

            // parse the token.

            DecodedJWT verify = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token.replace(PREFIX, ""));
            String username = verify.getSubject();
            Date expiresAt = verify.getExpiresAt();



            if (username != null && expiresAt.after(new Date())) {
                UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
                if (userDetails != null) {
                    return new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
                }
            }
            return null;
        }
        return null;

    }
}
