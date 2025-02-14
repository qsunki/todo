package com.example.mytodo.infra.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

public class RestAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public RestAuthenticationFilter(HttpSecurity http, ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher("/api/login", "POST"));
        this.objectMapper = objectMapper;
        setSecurityContextRepository(getSecurityContextRepository(http));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {
        UserLoginReq userLoginReq = objectMapper.readValue(request.getReader(), UserLoginReq.class);

        if (!StringUtils.hasText(userLoginReq.username()) || !StringUtils.hasText(userLoginReq.password())) {
            throw new AuthenticationServiceException("Username or Password not provided");
        }

        return this.getAuthenticationManager()
                .authenticate(
                        RestAuthenticationToken.unauthenticated(userLoginReq.username(), userLoginReq.password()));
    }

    private SecurityContextRepository getSecurityContextRepository(HttpSecurity http) {
        SecurityContextRepository securityContextRepository = http.getSharedObject(SecurityContextRepository.class);
        if (securityContextRepository == null) {
            securityContextRepository = new DelegatingSecurityContextRepository(
                    new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository());
        }
        return securityContextRepository;
    }
}
