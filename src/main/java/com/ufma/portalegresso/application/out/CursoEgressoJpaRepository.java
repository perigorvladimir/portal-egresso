package com.ufma.portalegresso.application.out;

import com.ufma.portalegresso.application.domain.CursoEgresso;
import com.ufma.portalegresso.application.domain.relacionamentos.CursoEgressoId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CursoEgressoJpaRepository extends JpaRepository<CursoEgresso, Integer> {
    @EntityGraph(attributePaths = {"egresso"})
    List<CursoEgresso> findCursoEgressoByCurso_IdCurso(Integer idCurso);

    Optional<CursoEgresso> findById(CursoEgressoId id);
}
