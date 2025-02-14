package com.example.mytodo.infra.security;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class RestAuthenticationToken implements Authentication {

    private final transient Object principal;
    private final String credentials;
    private boolean authenticated = false;

    public static RestAuthenticationToken unauthenticated(String username, String credentials) {
        return new RestAuthenticationToken(username, credentials);
    }

    public static RestAuthenticationToken authenticated(UserSessionInfo principal) {
        RestAuthenticationToken restAuthenticationToken = new RestAuthenticationToken(principal, null);
        restAuthenticationToken.setAuthenticated(true);
        return restAuthenticationToken;
    }

    private RestAuthenticationToken(Object principal, String credentials) {
        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public String getName() {
        if (principal instanceof String username) {
            return username;
        }
        if (principal instanceof UserSessionInfo userSessionInfo) {
            return userSessionInfo.username();
        }
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }
}
