package com.ufma.portalegressos.application.services;

import com.ufma.portalegressos.application.domain.Curso;
import com.ufma.portalegressos.application.domain.CursoEgresso;
import com.ufma.portalegressos.application.domain.Egresso;
import com.ufma.portalegressos.application.repositories.CursoJpaRepository;
import com.ufma.portalegressos.application.usecases.curso.CursoUC;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class CursoService implements CursoUC {
    private final CursoJpaRepository cursoJpaRepository;
    @Override
    public Curso salvarCurso(Curso curso) {
        return cursoJpaRepository.save(curso);
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
