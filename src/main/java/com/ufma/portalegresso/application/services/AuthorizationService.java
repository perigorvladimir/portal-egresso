package com.ufma.portalegresso.application.services;

import com.ufma.portalegresso.application.out.CoordenadorJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthorizationService implements UserDetailsService {
    private final CoordenadorJpaRepository coordenadorJpaRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return coordenadorJpaRepository.findByLogin(username);
    }
}
