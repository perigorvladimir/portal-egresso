package com.ufma.portalegressos.application.services;

import com.ufma.portalegressos.application.domain.CursoEgresso;
import com.ufma.portalegressos.application.domain.Egresso;
import com.ufma.portalegressos.application.repositories.CursoEgressoJpaRepository;
import com.ufma.portalegressos.application.repositories.EgressoJpaRepository;
import com.ufma.portalegressos.application.usecases.egresso.EgressoUC;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EgressoService implements EgressoUC {
    public final EgressoJpaRepository egressoJpaRepository;
    public final CursoEgressoJpaRepository cursoEgressoJpaRepository;

    public Egresso salvarEgresso(Egresso egresso){
        return egressoJpaRepository.save(egresso);
    }

    @Override
    public Optional<Egresso> buscarEgressoPorId(Integer id) {
        return egressoJpaRepository.findById(id);
    }

    @Override
    public List<Egresso> buscarTodosEgressos() {
        return egressoJpaRepository.findAll();
    }

    @Override
    public void deletarEgressoPorId(Integer id) {
        egressoJpaRepository.deleteById(id);
    }

    @Override
    public List<Egresso> buscarEgressosPorCursoId(Integer cursoId) {
        List<CursoEgresso> cursoEgresso = cursoEgressoJpaRepository.findCursoEgressoByCurso_IdCurso(cursoId);

        return cursoEgresso.stream().map(CursoEgresso::getEgresso).toList();
    }
}
