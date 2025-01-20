package com.ufma.portalegresso.application.usecases.cargo;

import com.ufma.portalegresso.application.domain.Cargo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface SalvarCargoUC {
    Cargo salvar(Request request);

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class Request{
        private String descricao;
        private String local;
        private Integer anoInicio;
        private Integer anoFim;
        private Integer idEgresso;
    }
}
