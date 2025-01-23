package com.ufma.portalegresso.application.usecases.curso;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface DesignarCoordenadorUC {
    Response designarCoordenador(Integer idCurso, Integer idCoordenador);

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    class Response{
        Integer idNovoCoord;
        String nomeNovoCoord;
        Integer idCurso;
        String nomeCurso;
    }
}
