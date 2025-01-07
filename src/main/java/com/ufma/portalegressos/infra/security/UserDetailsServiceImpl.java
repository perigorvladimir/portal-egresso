package com.ufma.portalegressos.infra.security;

import com.ufma.portalegressos.application.domain.Coordenador;
import com.ufma.portalegressos.application.out.CoordenadorJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final CoordenadorJpaRepository coordenadorJpaRepository;
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return coordenadorJpaRepository.findByLogin(login);
    }

}
