package com.ufma.portalegressos.application.repositories;

import com.ufma.portalegressos.application.domain.Egresso;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EgressoJpaRepository extends JpaRepository<Egresso, Integer> {
    @Query("SELECT e FROM Egresso e JOIN e.cursoEgressos ce WHERE ce.curso.idCurso = :idCurso")
    List<Egresso> findEgressosByCursoId(@Param("idCurso") Integer idCurso);
}
