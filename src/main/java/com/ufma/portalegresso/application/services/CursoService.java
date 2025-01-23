package com.ufma.portalegresso.application.services;

import com.ufma.portalegresso.application.domain.Coordenador;
import com.ufma.portalegresso.application.domain.Curso;
import com.ufma.portalegresso.application.domain.TipoNivel;
import com.ufma.portalegresso.application.out.CoordenadorJpaRepository;
import com.ufma.portalegresso.application.out.CursoJpaRepository;
import com.ufma.portalegresso.application.usecases.curso.CursoUC;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CursoService implements CursoUC {
    private final CursoJpaRepository cursoJpaRepository;
    private final CoordenadorService coordenadorService;
    @Override
    public Curso salvarCurso(Request request) {
        Coordenador coordenador = request.getIdCoordenador() != null ?
                coordenadorService.buscarCoordenadorPorId(request.getIdCoordenador())
                : null;

        Curso curso = Curso.builder()
                .coordenador(coordenador)
                .nome(request.getNome())
                .tipoNivel(TipoNivel.valueOf(request.getTipoNivel()))
                .build();
        return cursoJpaRepository.save(curso);
    }

    @Override
    public Curso buscarPorId(Integer id) {
        Optional<Curso> curso = cursoJpaRepository.findById(id);
        if(curso.isEmpty()){
            throw new EntityNotFoundException("Curso não encontrado com o id inserido");
        }
        return curso.get();
    }

    @Override
    public List<Curso> buscarTodosCursos() {
        return cursoJpaRepository.findAll();
    }

    @Override
    public void deletarCursoPorId(Integer id) {
        cursoJpaRepository.deleteById(id);
    }

    @Override
    public Response designarCoordenador(Integer idCurso, Integer idCoordenador) {
        Curso curso = buscarPorId(idCurso);
        Coordenador coordenador = coordenadorService.buscarCoordenadorPorId(idCoordenador);
        curso.setCoordenador(coordenador);
        Curso cursoSalvo = cursoJpaRepository.save(curso);
        return Response.builder().idNovoCoord(cursoSalvo.getCoordenador().getIdCoordenador()).nomeNovoCoord(cursoSalvo.getCoordenador().getNome()).idCurso(cursoSalvo.getIdCurso()).nomeCurso(cursoSalvo.getNome()).build();
    }
}
