package com.hts.htsApp.service;

import com.hts.htsApp.model.User;
import com.hts.htsApp.repo.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepo usuarioRepo;

    public CustomUserDetailService(UserRepo usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        Optional<User> usuarioOptional;
        usuarioOptional = usuarioRepo.findByUsername(input);
        return usuarioOptional.orElseThrow(() ->
                new UsernameNotFoundException("Usuario no encontrado: " + input)
        );

    }
}
