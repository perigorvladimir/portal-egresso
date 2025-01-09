package com.ufma.portalegresso.application.out;

import com.ufma.portalegresso.application.domain.Coordenador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordenadorJpaRepository extends JpaRepository<Coordenador, Integer> {
    UserDetails findByLogin(String login);

    boolean existsByLogin(String login);
}
