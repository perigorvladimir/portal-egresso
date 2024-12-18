package com.ufma.portalegressos.application.repositories;

import com.ufma.portalegressos.application.domain.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoJpaRepository extends JpaRepository<Curso, Integer> {
}
