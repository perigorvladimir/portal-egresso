package com.ufma.portalegresso.application.usecases.coordenador;

import com.ufma.portalegresso.application.domain.Coordenador;
import com.ufma.portalegresso.application.out.SenhaEncoder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface SalvarCoordenadorUC {
    Coordenador salvarCoordenador(Request request, String algortimoCriptografia);

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class Request{
        @NotBlank
        String nome;
        @NotBlank
        String login;
        @NotBlank
        String senha;
    }
}
