package com.ufma.portalegressos.application.services;

import com.ufma.portalegressos.application.domain.CursoEgresso;
import com.ufma.portalegressos.application.domain.Egresso;
import com.ufma.portalegressos.application.out.CursoEgressoJpaRepository;
import com.ufma.portalegressos.application.out.EgressoJpaRepository;
import com.ufma.portalegressos.application.usecases.egresso.EgressoUC;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class EgressoService implements EgressoUC {
    public final EgressoJpaRepository egressoJpaRepository;
    public final CursoEgressoJpaRepository cursoEgressoJpaRepository;

    public Egresso salvarEgresso(Request request){
        Egresso egresso = Egresso.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .descricao(request.getDescricao())
                .foto(request.getFoto())
                .linkedin(request.getLinkedin())
                .instagram(request.getInstagram())
                .curriculo(request.getCurriculo())
                .build();

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
