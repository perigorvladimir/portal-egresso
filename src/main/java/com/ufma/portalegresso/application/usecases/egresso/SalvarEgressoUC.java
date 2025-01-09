package com.ufma.portalegresso.application.usecases.egresso;

import com.ufma.portalegresso.application.domain.Egresso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface SalvarEgressoUC {
    Egresso salvarEgresso(Request request);

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    class Request{
        String nome;
        String email;
        String descricao;
        String foto;
        String linkedin;
        String instagram;
        String curriculo;
    }
}
