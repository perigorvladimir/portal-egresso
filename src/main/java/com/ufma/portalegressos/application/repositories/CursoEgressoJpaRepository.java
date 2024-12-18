package com.ufma.portalegressos.application.repositories;

import com.ufma.portalegressos.application.domain.CursoEgresso;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CursoEgressoJpaRepository extends JpaRepository<CursoEgresso, Integer> {
    @EntityGraph(attributePaths = {"egresso"})
    List<CursoEgresso> findCursoEgressoByCurso_IdCurso(Integer idCurso);
}
