package com.ufma.portalegressos.application.usecases.depoimento;

import com.ufma.portalegressos.application.domain.Depoimento;
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
