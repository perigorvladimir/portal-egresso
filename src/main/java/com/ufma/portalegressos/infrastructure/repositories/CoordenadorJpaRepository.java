package com.ufma.portalegressos.infrastructure.repositories;

import com.ufma.portalegressos.application.domain.Coordenador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordenadorJpaRepository extends JpaRepository<Coordenador, Integer> {
}
