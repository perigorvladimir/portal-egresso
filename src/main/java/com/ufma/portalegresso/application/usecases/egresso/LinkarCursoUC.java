package com.ufma.portalegresso.application.usecases.egresso;

import jakarta.validation.constraints.NotNull;
import lombok.*;

public interface LinkarCursoUC {
    Response linkarCurso(Integer egressoId, Request request);

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

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    class Response{
        Integer idEgresso;
        String nomeEgresso;
        Integer idCursoLinkado;
        String nomeCursoLinkado;
        Integer anoInicioCursoLinkado;
        Integer anoFimCursoLinkado;
    }
}
