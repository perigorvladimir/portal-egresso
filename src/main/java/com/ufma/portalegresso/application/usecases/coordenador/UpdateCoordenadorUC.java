package com.ufma.portalegresso.application.usecases.coordenador;

import com.ufma.portalegresso.application.domain.Coordenador;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface UpdateCoordenadorUC {
    Coordenador updateCoordenador(Integer id, Request request);

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class Request{
        @NotBlank
        String nome;
    }
}
