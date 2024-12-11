package com.ufma.portalegressos.infrastructure.repositories;

import com.ufma.portalegressos.infrastructure.entities.CoordenadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordenadorJpaRepository extends JpaRepository<CoordenadorEntity, Integer> {
}
