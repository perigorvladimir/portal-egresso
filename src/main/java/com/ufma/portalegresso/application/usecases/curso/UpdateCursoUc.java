package com.ufma.portalegresso.application.usecases.curso;

import com.ufma.portalegresso.application.domain.Curso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface UpdateCursoUc {
    Curso update(Integer id, Request request);

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class Request{
        String nome;
    }
}
