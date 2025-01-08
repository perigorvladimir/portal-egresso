package com.ufma.portalegressos.application.usecases.curso;

import com.ufma.portalegressos.application.domain.Curso;

import java.util.Optional;

public interface BuscarCursoPorIdUC {
    Curso buscarPorId(Integer id);

}
