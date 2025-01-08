package com.ufma.portalegressos.application.services;

import com.ufma.portalegressos.application.domain.Coordenador;
import com.ufma.portalegressos.application.domain.Curso;
import com.ufma.portalegressos.application.out.CoordenadorJpaRepository;
import com.ufma.portalegressos.application.out.CursoJpaRepository;
import com.ufma.portalegressos.application.usecases.curso.CursoUC;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CursoService implements CursoUC {
    private final CursoJpaRepository cursoJpaRepository;
    private final CoordenadorJpaRepository coordenadorJpaRepository;
    @Override
    public Curso salvarCurso(Request request) {
        if(request.getIdCoordenador() == null) throw new IllegalArgumentException("Coordenador do curso precisa ser informado");

        Coordenador coordEncontrado = coordenadorJpaRepository.findById(request.getIdCoordenador()).orElseThrow(EntityNotFoundException::new);

        Curso curso = Curso.builder()
                .coordenador(coordEncontrado)
                .nome(request.getNome())
                .nivel(request.getNivel())
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
    public void deletarCurso(Integer id) {
        cursoJpaRepository.deleteById(id);
    }
}
