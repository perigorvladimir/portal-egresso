package com.ufma.portalegressos.application.usecases.coordenador;

import com.ufma.portalegressos.application.domain.Coordenador;
import com.ufma.portalegressos.application.out.SenhaEncoder;
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
