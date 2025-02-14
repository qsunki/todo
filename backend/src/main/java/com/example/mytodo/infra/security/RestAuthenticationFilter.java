package com.example.mytodo.infra.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

public class RestAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public RestAuthenticationFilter(ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher("/api/login", "POST"));
        this.objectMapper = objectMapper;
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
}
