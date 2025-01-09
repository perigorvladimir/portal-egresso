package com.ufma.portalegresso.application.usecases.curso;

import com.ufma.portalegresso.application.domain.Curso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface SalvarCursoUC {
    Curso salvarCurso(Request request);

    @AllArgsConstructor
    @Builder
    @Getter
    class Request {
        private String nome;
        private String nivel;
        private Integer idCoordenador;
    }
}
