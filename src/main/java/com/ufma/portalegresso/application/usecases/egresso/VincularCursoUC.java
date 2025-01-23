package com.ufma.portalegresso.application.usecases.egresso;

import com.ufma.portalegresso.application.domain.CursoEgresso;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public interface VincularCursoUC {
    Response vincularCurso(Integer egressoId, Request request);

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    class Request{
        @NotNull
        Integer idCurso;
        @NotNull
        Integer anoInicio;
        Integer anoFim;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    class Response{
        Integer idCurso;
        String nomeCurso;
        Integer idEgressoVinculado;
        String nomeEgressoVinculado;
    }
}
