package com.ufma.portalegresso.application.usecases.coordenador;

import com.ufma.portalegresso.application.domain.Coordenador;
import com.ufma.portalegresso.application.out.SenhaEncoder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface SalvarCoordenadorUC {
    Coordenador salvarCoordenador(Request request, SenhaEncoder senhaEncoder);

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class Request{
        String nome;
        String login;
        String senha;
    }
}
