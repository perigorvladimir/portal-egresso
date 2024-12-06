package com.ufma.portalegressos.database.repositories;

import com.ufma.portalegressos.database.entities.CursoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoJpaRepository extends JpaRepository<CursoEntity, Integer> {
}
