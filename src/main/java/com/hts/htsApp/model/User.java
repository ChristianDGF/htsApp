package com.hts.htsApp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
public class User implements UserDetails {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Setter
    private String username;
    @Setter
    @Getter
    private String password;
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    @Getter
    @Setter
    private Empresa empresa;
    @Getter
    @Setter
    private String role = "USER"; // default role

    public User(String username, String password, Empresa empresa) {
        this.username = username;
        this.password = password;
        this.empresa = empresa;
    }

    public User(String username, String password, Empresa empresa, String role) {
        this.username = username;
        this.password = password;
        this.empresa = empresa;
        this.role = role;
    }

    public User(){}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return a single role as a Spring Security authority (ROLE_<role>)
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + (this.role == null ? "USER" : this.role)));
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

}
