package ru.akbarsdigital.restapi.configurations.root.security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private final Long id;
    private final String email;
    private final LocalDateTime lastPasswordChange;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String email, LocalDateTime lastPasswordChange) {
        this.id = id;
        this.email = email;
        this.lastPasswordChange = lastPasswordChange;
        this.authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("authenticated");
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getLastPasswordChange() {
        return lastPasswordChange;
    }
}
