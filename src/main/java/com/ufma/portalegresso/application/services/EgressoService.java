package com.ufma.portalegresso.application.services;

import com.ufma.portalegresso.application.domain.Curso;
import com.ufma.portalegresso.application.domain.CursoEgresso;
import com.ufma.portalegresso.application.domain.Egresso;
import com.ufma.portalegresso.application.domain.relacionamentos.CursoEgressoId;
import com.ufma.portalegresso.application.out.CursoEgressoJpaRepository;
import com.ufma.portalegresso.application.out.EgressoJpaRepository;
import com.ufma.portalegresso.application.usecases.egresso.EgressoUC;
import com.ufma.portalegresso.application.usecases.egresso.VincularCursoUC;
import com.ufma.portalegresso.application.usecases.egresso.SalvarEgressoUC;
import com.ufma.portalegresso.application.usecases.egresso.UpdateEgressoUC;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class EgressoService implements EgressoUC {
    public final EgressoJpaRepository egressoJpaRepository;
    public final CursoEgressoJpaRepository cursoEgressoJpaRepository;
    public final CursoService cursoService;

    public Egresso salvarEgresso(SalvarEgressoUC.Request request){
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
    public Egresso buscarEgressoPorId(Integer id) {
        if(id == null){
            throw new IllegalArgumentException("O id do egresso nao pode ser nulo");
        }
        Egresso egresso = egressoJpaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Egresso nao encontrado com o id inserido"));
        return egresso;
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

    @Override
    public Egresso updateEgresso(Integer id, UpdateEgressoUC.Request request) {
        Egresso egresso = buscarEgressoPorId(id);
        egresso.setNome(request.getNome());
        egresso.setEmail(request.getEmail());
        egresso.setDescricao(request.getDescricao());
        egresso.setFoto(request.getFoto());
        egresso.setLinkedin(request.getLinkedin());
        egresso.setInstagram(request.getInstagram());
        egresso.setCurriculo(request.getCurriculo());
        return egressoJpaRepository.save(egresso);
    }

    @Override
    public VincularCursoUC.Response vincularCurso(Integer egressoId, VincularCursoUC.Request request) {
        Egresso egresso = buscarEgressoPorId(egressoId);
        Curso curso = cursoService.buscarPorId(request.getIdCurso());

        CursoEgressoId id = new CursoEgressoId(egressoId, request.getIdCurso());
        cursoEgressoJpaRepository.findById(id).ifPresent(cursoEgresso -> {
            throw new IllegalArgumentException("O egresso já está linkado com o curso especificado.");
        });

        CursoEgresso cursoEgresso = new CursoEgresso();
        cursoEgresso.setId(id);
        cursoEgresso.setAnoInicio(request.getAnoInicio());
        cursoEgresso.setAnoFim(request.getAnoFim());
        cursoEgresso.setEgresso(egresso);
        cursoEgresso.setCurso(curso);
        CursoEgresso cursoEgressoSalvo = cursoEgressoJpaRepository.save(cursoEgresso);

        return VincularCursoUC.Response.builder()
                .idEgressoVinculado(cursoEgressoSalvo.getEgresso().getIdEgresso())
                .nomeEgressoVinculado(cursoEgressoSalvo.getEgresso().getNome())
                .idCurso(cursoEgressoSalvo.getCurso().getIdCurso())
                .nomeCurso(cursoEgressoSalvo.getCurso().getNome())
                .build();
    }
}
