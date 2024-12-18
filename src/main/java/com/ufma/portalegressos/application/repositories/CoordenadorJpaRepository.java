package com.ufma.portalegressos.application.repositories;

import com.ufma.portalegressos.application.domain.Coordenador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoordenadorJpaRepository extends JpaRepository<Coordenador, Integer> {
    Optional<Coordenador> findByLogin(String login);
}
