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
        if(request.getIdCoordenador()!=null){
            Coordenador coordEncontrado = coordenadorJpaRepository.findById(request.getIdCoordenador()).orElseThrow(EntityNotFoundException::new);

            Curso curso = Curso.builder()
                    .coordenador(coordEncontrado)
                    .nome(request.getNome())
                    .nivel(request.getNivel())
                    .build();
            return cursoJpaRepository.save(curso);
        }
        return null;
    }

    @Override
    public Optional<Curso> buscarPorId(Integer id) {
        return cursoJpaRepository.findById(id);
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
