package com.ufma.portalegresso.application.usecases.egresso;

import com.ufma.portalegresso.application.domain.Egresso;
import lombok.*;

public interface UpdateEgressoUC {
    Egresso updateEgresso(Integer id, Request request);

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
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
