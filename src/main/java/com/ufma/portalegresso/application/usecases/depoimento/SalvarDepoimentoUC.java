package com.ufma.portalegresso.application.usecases.depoimento;

import com.ufma.portalegresso.application.domain.Depoimento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface SalvarDepoimentoUC {
    Depoimento salvarDepoimento(Request request);

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Request {
        private String texto;
        private Integer idEgresso;
    }
}
