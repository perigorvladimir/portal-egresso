package com.ufma.portalegresso.application.services;

import com.ufma.portalegresso.application.domain.Coordenador;
import com.ufma.portalegresso.application.domain.Curso;
import com.ufma.portalegresso.application.domain.TipoNivel;
import com.ufma.portalegresso.application.out.CursoJpaRepository;
import com.ufma.portalegresso.application.usecases.curso.CursoUC;
import com.ufma.portalegresso.application.usecases.curso.SalvarCursoUC;
import com.ufma.portalegresso.application.usecases.curso.UpdateCursoUc;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class CursoService implements CursoUC {
    private final CursoJpaRepository cursoJpaRepository;
    private final CoordenadorService coordenadorService;
    @Override
    @Transactional
    public Curso salvarCurso(SalvarCursoUC.Request request) {
        Coordenador coordenador = request.getIdCoordenador() != null ?
                coordenadorService.buscarCoordenadorPorId(request.getIdCoordenador())
                : null;

        Curso curso = Curso.builder()
                .coordenador(coordenador)
                .nome(request.getNome())
                .tipoNivel(TipoNivel.buscarPorNome(request.getTipoNivel()))
                .build();
        return cursoJpaRepository.save(curso);
    }

    @Override
    public Curso buscarPorId(Integer id) {
        if(id == null){
            throw new IllegalArgumentException("O id do curso nao pode ser nulo");
        }
        Curso curso = cursoJpaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Curso nao encontrado com o id inserido"));
        return curso;
    }

    @Override
    public List<Curso> buscarTodosCursos() {
        return cursoJpaRepository.findAll();
    }
    @Override
    @Transactional
    public Curso update(Integer id, UpdateCursoUc.Request request) {
        Curso curso = buscarPorId(id);
        curso.setNome(request.getNome());
        return cursoJpaRepository.saveAndFlush(curso);
    }

    @Override
    @Transactional
    public void deletarCursoPorId(Integer id) {
        cursoJpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Response designarCoordenador(Integer idCurso, Integer idCoordenador) {
        Curso curso = buscarPorId(idCurso);
        Coordenador coordenador = coordenadorService.buscarCoordenadorPorId(idCoordenador);
        curso.setCoordenador(coordenador);
        Curso cursoSalvo = cursoJpaRepository.saveAndFlush(curso);
        return Response.builder().idNovoCoord(cursoSalvo.getCoordenador().getIdCoordenador()).nomeNovoCoord(cursoSalvo.getCoordenador().getNome()).idCurso(cursoSalvo.getIdCurso()).nomeCurso(cursoSalvo.getNome()).build();
    }
}
