package com.ufma.portalegresso.application.usecases.curso;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.coyote.Response;

public interface DesignarCoordenadorUC {
    Response designarCoordenador(Integer idCurso, Integer idCoordenador);

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Response{
        String nomeNovoCoord;
        String nomeCurso;
    }
}
