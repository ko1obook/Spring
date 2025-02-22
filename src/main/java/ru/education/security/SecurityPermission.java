package ru.education.security;

import org.springframework.security.core.GrantedAuthority;

public class SecurityPermission implements GrantedAuthority {

    private String name;

    public SecurityPermission(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
