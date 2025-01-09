package com.ufma.portalegresso.application.usecases.depoimento;

import com.ufma.portalegresso.application.domain.Depoimento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface SalvarDepoimentoUC {
    Depoimento salvarDepoimento(Request request);

    @AllArgsConstructor
    @Builder
    @Getter
    class Request {
        private String texto;
        private Integer idEgresso;
    }
}
