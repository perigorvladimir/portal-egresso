package com.ufma.portalegressos.infrastructure.repositories;

import com.ufma.portalegressos.infrastructure.entities.CursoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoJpaRepository extends JpaRepository<CursoEntity, Integer> {
}
