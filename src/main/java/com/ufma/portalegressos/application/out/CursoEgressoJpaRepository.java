package com.ufma.portalegressos.application.out;

import com.ufma.portalegressos.application.domain.CursoEgresso;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CursoEgressoJpaRepository extends JpaRepository<CursoEgresso, Integer> {
    @EntityGraph(attributePaths = {"egresso"})
    List<CursoEgresso> findCursoEgressoByCurso_IdCurso(Integer idCurso);
}
