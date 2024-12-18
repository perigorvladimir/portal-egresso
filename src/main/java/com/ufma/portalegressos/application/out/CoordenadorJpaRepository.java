package com.ufma.portalegressos.application.out;

import com.ufma.portalegressos.application.domain.Coordenador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CoordenadorJpaRepository extends JpaRepository<Coordenador, Integer> {
    Optional<Coordenador> findByLogin(String login);

    boolean existsByLogin(String login);
}
