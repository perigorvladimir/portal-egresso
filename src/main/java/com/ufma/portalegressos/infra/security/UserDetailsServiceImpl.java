package com.ufma.portalegressos.infra.security;

import com.ufma.portalegressos.application.domain.Coordenador;
import com.ufma.portalegressos.application.repositories.CoordenadorJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Collections.emptyList;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final CoordenadorJpaRepository coordenadorJpaRepository;
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Coordenador> response = coordenadorJpaRepository.findByLogin(login);
        if(response.isEmpty()) throw new UsernameNotFoundException(login);

        Coordenador coord = response.get();
        return new org.springframework.security.core.userdetails.User(coord.getUsername(), coord.getPassword(), coord.getAuthorities());
    }

}
